package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.InstallmentStatus;
import com.cambofreelance.webbackend.constants.LoanApplicationStatus;
import com.cambofreelance.webbackend.constants.PaymentMethod;
import com.cambofreelance.webbackend.constants.PaymentTransactionStatus;
import com.cambofreelance.webbackend.constants.PaymentType;
import com.cambofreelance.webbackend.dto.request.PaymentCreateRequest;
import com.cambofreelance.webbackend.dto.request.PaymentReverseRequest;
import com.cambofreelance.webbackend.dto.response.PaymentResponse;
import com.cambofreelance.webbackend.entities.LoanApplicationEntity;
import com.cambofreelance.webbackend.entities.PaymentEntity;
import com.cambofreelance.webbackend.entities.RepaymentInstallmentEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.LoanApplicationRepository;
import com.cambofreelance.webbackend.repository.PaymentRepository;
import com.cambofreelance.webbackend.repository.RepaymentInstallmentRepository;
import com.cambofreelance.webbackend.services.PaymentService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final RepaymentInstallmentRepository installmentRepository;

    @Override
    @Transactional
    @Auditable(action = "RECEIVE_PAYMENT", module = "PAYMENT")
    public PaymentResponse receivePayment(PaymentCreateRequest request, String userId) {
        LoanApplicationEntity loan = loanApplicationRepository.findById(request.getLoanApplicationId())
            .orElseThrow(() -> notFound("LOAN_NOT_FOUND", "Loan application not found"));

        if (!LoanApplicationStatus.APPROVED.equals(loan.getApplicationStatus())) {
            throw badRequest("LOAN_NOT_APPROVED",
                "Payment can only be received for approved loans");
        }

        if (!PaymentMethod.isValid(request.getPaymentMethod().toUpperCase())) {
            throw badRequest("INVALID_PAYMENT_METHOD",
                "Payment method must be CASH or TRANSFER");
        }

        if (!PaymentType.isValid(request.getPaymentType().toUpperCase())) {
            throw badRequest("INVALID_PAYMENT_TYPE",
                "Payment type must be PARTIAL or FULL_SETTLEMENT");
        }

        Long seq = paymentRepository.nextReceiptSequence();
        String receiptNumber = String.format("RCP-%d-%06d", LocalDate.now().getYear(), seq);

        if (PaymentType.FULL_SETTLEMENT.equals(request.getPaymentType().toUpperCase())) {
            settleAllInstallments(loan.getId(), request.getPaymentDate(), userId);
        }

        PaymentEntity entity = new PaymentEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setReceiptNumber(receiptNumber);
        entity.setLoanApplicationId(loan.getId());
        entity.setLoanNumber(loan.getLoanNumber());
        entity.setCurrency(loan.getCurrency());
        entity.setCustomerId(loan.getCustomerId());
        entity.setCustomerCode(loan.getCustomerCode());
        entity.setCustomerFirstName(loan.getCustomerFirstName());
        entity.setCustomerLastName(loan.getCustomerLastName());
        entity.setPaymentDate(request.getPaymentDate());
        entity.setPrincipalPaid(nvl(request.getPrincipalPaid()));
        entity.setInterestPaid(nvl(request.getInterestPaid()));
        entity.setPenaltyFee(nvl(request.getPenaltyFee()));
        entity.setTotalPaid(request.getTotalPaid());
        entity.setPaymentMethod(request.getPaymentMethod().toUpperCase());
        entity.setPaymentType(request.getPaymentType().toUpperCase());
        entity.setReceivedById(userId);
        entity.setNotes(request.getNotes());
        entity.setPaymentStatus(PaymentTransactionStatus.ACTIVE);
        entity.setCreatedBy(userId);
        entity.setCreatedAt(new Date());
        entity.setStatus("ACT");

        paymentRepository.save(entity);
        return PaymentResponse.from(entity);
    }

    @Override
    public PaymentResponse getById(String id) {
        PaymentEntity entity = paymentRepository.findById(id)
            .orElseThrow(() -> notFound("PAYMENT_NOT_FOUND", "Payment not found"));
        return PaymentResponse.from(entity);
    }

    @Override
    public Page<PaymentResponse> search(String loanApplicationId, String paymentStatus,
                                        String paymentType, String currency, int page, int size) {
        return paymentRepository.search(
            blankToNull(loanApplicationId),
            blankToNull(paymentStatus),
            blankToNull(paymentType),
            blankToNull(currency),
            PageRequest.of(page, size)
        ).map(PaymentResponse::from);
    }

    @Override
    public List<PaymentResponse> getByLoan(String loanApplicationId) {
        return paymentRepository.findByLoanApplicationIdOrderByCreatedAtDesc(loanApplicationId)
            .stream().map(PaymentResponse::from).toList();
    }

    @Override
    @Transactional
    @Auditable(action = "REVERSE_PAYMENT", module = "PAYMENT")
    public PaymentResponse reversePayment(String id, PaymentReverseRequest request, String userId) {
        PaymentEntity entity = paymentRepository.findById(id)
            .orElseThrow(() -> notFound("PAYMENT_NOT_FOUND", "Payment not found"));

        if (!PaymentTransactionStatus.ACTIVE.equals(entity.getPaymentStatus())) {
            throw badRequest("PAYMENT_ALREADY_REVERSED",
                "This payment has already been reversed");
        }

        entity.setPaymentStatus(PaymentTransactionStatus.REVERSED);
        entity.setReversedAt(new Date());
        entity.setReversedById(userId);
        entity.setReversalReason(request.getReversalReason());
        entity.setUpdatedBy(userId);
        entity.setUpdatedAt(new Date());

        paymentRepository.save(entity);
        return PaymentResponse.from(entity);
    }

    private void settleAllInstallments(String loanApplicationId, Date paymentDate, String userId) {
        List<RepaymentInstallmentEntity> all =
            installmentRepository.findByLoanApplicationIdOrderByInstallmentNoAsc(loanApplicationId);
        for (RepaymentInstallmentEntity inst : all) {
            if (!InstallmentStatus.PAID.equals(inst.getInstallmentStatus())) {
                inst.setInstallmentStatus(InstallmentStatus.PAID);
                inst.setPaidDate(paymentDate);
                inst.setUpdatedBy(userId);
                inst.setUpdatedAt(new Date());
                installmentRepository.save(inst);
            }
        }
    }

    private static BigDecimal nvl(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    private AppException notFound(String code, String message) {
        AppException ex = new AppException(code, message);
        ex.setHttpStatus(HttpStatus.NOT_FOUND);
        return ex;
    }

    private AppException badRequest(String code, String message) {
        AppException ex = new AppException(code, message);
        ex.setHttpStatus(HttpStatus.BAD_REQUEST);
        return ex;
    }
}
