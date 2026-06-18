package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.InstallmentStatus;
import com.cambofreelance.webbackend.constants.LoanPaymentType;
import com.cambofreelance.webbackend.constants.TenorUnit;
import com.cambofreelance.webbackend.dto.request.RepaymentInstallmentUpdateRequest;
import com.cambofreelance.webbackend.dto.response.InstallmentResponse;
import com.cambofreelance.webbackend.dto.response.PersistedScheduleResponse;
import com.cambofreelance.webbackend.entities.LoanApplicationEntity;
import com.cambofreelance.webbackend.entities.RepaymentInstallmentEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.LoanApplicationRepository;
import com.cambofreelance.webbackend.repository.RepaymentInstallmentRepository;
import com.cambofreelance.webbackend.services.RepaymentScheduleService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RepaymentScheduleServiceImpl implements RepaymentScheduleService {

    private final RepaymentInstallmentRepository installmentRepository;
    private final LoanApplicationRepository      loanApplicationRepository;

    @Override
    @Transactional
    @Auditable(action = "GENERATE", module = "REPAYMENT_SCHEDULE")
    public PersistedScheduleResponse generate(String loanApplicationId, String userId) {
        LoanApplicationEntity loan = findLoanOrThrow(loanApplicationId);

        if (installmentRepository.countByLoanApplicationId(loanApplicationId) > 0) {
            throw badRequest("SCHEDULE_ALREADY_EXISTS",
                "A repayment schedule already exists. Use recalculate to regenerate.");
        }

        List<RepaymentInstallmentEntity> installments = buildInstallments(loan, userId);
        installments.forEach(installmentRepository::save);
        return toResponse(loan, installments);
    }

    @Override
    @Transactional
    @Auditable(action = "RECALCULATE", module = "REPAYMENT_SCHEDULE")
    public PersistedScheduleResponse recalculate(String loanApplicationId, String userId) {
        LoanApplicationEntity loan = findLoanOrThrow(loanApplicationId);

        boolean hasPayments = installmentRepository.existsByLoanApplicationIdAndInstallmentStatusIn(
            loanApplicationId, new ArrayList<>(List.of(InstallmentStatus.PAID, InstallmentStatus.PARTIAL_PAID))
        );
        if (hasPayments) {
            throw badRequest("SCHEDULE_HAS_PAYMENTS",
                "Cannot recalculate: one or more installments have already been paid.");
        }

        installmentRepository.deleteByLoanApplicationId(loanApplicationId);
        List<RepaymentInstallmentEntity> installments = buildInstallments(loan, userId);
        installments.forEach(installmentRepository::save);
        return toResponse(loan, installments);
    }

    @Override
    public PersistedScheduleResponse getSchedule(String loanApplicationId) {
        LoanApplicationEntity loan = findLoanOrThrow(loanApplicationId);
        List<RepaymentInstallmentEntity> installments =
            installmentRepository.findByLoanApplicationIdOrderByInstallmentNoAsc(loanApplicationId);
        return toResponse(loan, installments);
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE_INSTALLMENT", module = "REPAYMENT_SCHEDULE")
    public InstallmentResponse updateInstallment(String installmentId,
                                                 RepaymentInstallmentUpdateRequest request,
                                                 String userId) {
        if (!InstallmentStatus.isValid(request.getInstallmentStatus())) {
            throw badRequest("INVALID_INSTALLMENT_STATUS",
                "Invalid status: " + request.getInstallmentStatus());
        }
        RepaymentInstallmentEntity entity = installmentRepository.findById(installmentId)
            .orElseThrow(() -> notFound("INSTALLMENT_NOT_FOUND", "Installment not found: " + installmentId));

        entity.setInstallmentStatus(request.getInstallmentStatus().toUpperCase());
        if (request.getPaidAmount() != null) {
            entity.setPaidAmount(request.getPaidAmount());
        }
        if (request.getPaidDate() != null) {
            entity.setPaidDate(request.getPaidDate());
        }
        if (StringUtils.hasText(request.getNotes())) {
            entity.setNotes(request.getNotes());
        }
        entity.setUpdatedBy(userId);
        entity.setUpdatedAt(new Date());
        installmentRepository.save(entity);
        return InstallmentResponse.from(entity);
    }

    @Override
    @Transactional
    @Auditable(action = "MARK_OVERDUE", module = "REPAYMENT_SCHEDULE")
    public int markOverdue(String loanApplicationId, String userId) {
        findLoanOrThrow(loanApplicationId);
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<RepaymentInstallmentEntity> overdueList =
            installmentRepository.findOverdueInstallments(loanApplicationId, today);
        Date now = new Date();
        for (RepaymentInstallmentEntity e : overdueList) {
            e.setInstallmentStatus(InstallmentStatus.OVERDUE);
            e.setUpdatedBy(userId);
            e.setUpdatedAt(now);
        }
        overdueList.forEach(installmentRepository::save);
        return overdueList.size();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private List<RepaymentInstallmentEntity> buildInstallments(LoanApplicationEntity loan, String userId) {
        int n = numberOfPayments(loan.getTenorValue(), loan.getTenorUnit(), loan.getPaymentType());

        BigDecimal principal     = loan.getLoanAmount();
        BigDecimal totalInterest = loan.getInterestAmount();

        BigDecimal principalPerPmt = principal.divide(BigDecimal.valueOf(n), 2, RoundingMode.DOWN);
        BigDecimal interestPerPmt  = totalInterest.divide(BigDecimal.valueOf(n), 2, RoundingMode.DOWN);

        BigDecimal principalRemainder = principal.subtract(principalPerPmt.multiply(BigDecimal.valueOf(n - 1)));
        BigDecimal interestRemainder  = totalInterest.subtract(interestPerPmt.multiply(BigDecimal.valueOf(n - 1)));

        List<RepaymentInstallmentEntity> installments = new ArrayList<>();
        BigDecimal balance = principal;
        LocalDate dueDate  = toLocalDate(loan.getFirstPaymentDate());

        for (int i = 1; i <= n; i++) {
            BigDecimal pmt  = (i == n) ? principalRemainder : principalPerPmt;
            BigDecimal intr = (i == n) ? interestRemainder  : interestPerPmt;
            balance = balance.subtract(pmt);

            RepaymentInstallmentEntity entity = new RepaymentInstallmentEntity();
            entity.setId(UUID.randomUUID().toString());
            entity.setLoanApplicationId(loan.getId());
            entity.setLoanNumber(loan.getLoanNumber());
            entity.setInstallmentNo(i);
            entity.setDueDate(toDate(dueDate));
            entity.setPrincipalAmount(pmt);
            entity.setInterestAmount(intr);
            entity.setTotalPayment(pmt.add(intr));
            entity.setPaidAmount(BigDecimal.ZERO);
            entity.setRemainingBalance(balance);
            entity.setInstallmentStatus(InstallmentStatus.PENDING);
            entity.setCreatedBy(StringUtils.hasText(userId) ? userId : "SYS");
            installments.add(entity);

            dueDate = nextDueDate(dueDate, loan.getPaymentType());
        }
        return installments;
    }

    private PersistedScheduleResponse toResponse(LoanApplicationEntity loan,
                                                  List<RepaymentInstallmentEntity> installments) {
        BigDecimal totalRepayment = loan.getLoanAmount().add(loan.getInterestAmount());
        return PersistedScheduleResponse.builder()
            .loanApplicationId(loan.getId())
            .loanNumber(loan.getLoanNumber())
            .customerCode(loan.getCustomerCode())
            .currency(loan.getCurrency())
            .loanAmount(loan.getLoanAmount())
            .interestRate(loan.getInterestRate())
            .serviceFeeRate(loan.getServiceFeeRate())
            .interestAmount(loan.getInterestAmount())
            .serviceFeeAmount(loan.getServiceFeeAmount())
            .netDisbursement(loan.getNetDisbursement())
            .totalRepayment(totalRepayment)
            .numberOfPayments(installments.size())
            .paymentType(loan.getPaymentType())
            .firstPaymentDate(loan.getFirstPaymentDate())
            .installments(installments.stream().map(InstallmentResponse::from).toList())
            .build();
    }

    private LoanApplicationEntity findLoanOrThrow(String loanApplicationId) {
        return loanApplicationRepository.findById(loanApplicationId)
            .orElseThrow(() -> notFound("LOAN_APPLICATION_NOT_FOUND",
                "Loan application not found: " + loanApplicationId));
    }

    private int numberOfPayments(int tenorValue, String tenorUnit, String paymentType) {
        return switch (tenorUnit) {
            case TenorUnit.DAY -> switch (paymentType) {
                case LoanPaymentType.DAILY  -> tenorValue;
                case LoanPaymentType.WEEKLY -> (int) Math.ceil(tenorValue / 7.0);
                default                     -> (int) Math.ceil(tenorValue / 30.0);
            };
            case TenorUnit.MONTH -> switch (paymentType) {
                case LoanPaymentType.DAILY  -> tenorValue * 30;
                case LoanPaymentType.WEEKLY -> (int) Math.ceil(tenorValue * 30.0 / 7.0);
                default                     -> tenorValue;
            };
            default -> switch (paymentType) {
                case LoanPaymentType.DAILY  -> tenorValue * 365;
                case LoanPaymentType.WEEKLY -> tenorValue * 52;
                default                     -> tenorValue * 12;
            };
        };
    }

    private LocalDate nextDueDate(LocalDate current, String paymentType) {
        return switch (paymentType) {
            case LoanPaymentType.DAILY  -> current.plusDays(1);
            case LoanPaymentType.WEEKLY -> current.plusWeeks(1);
            default                     -> current.plusMonths(1);
        };
    }

    private LocalDate toLocalDate(Date date) {
        // java.sql.Date.toInstant() throws UnsupportedOperationException; use getTime() instead
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
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
