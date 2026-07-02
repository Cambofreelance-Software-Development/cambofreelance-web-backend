package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.LoanApplicationStatus;
import com.cambofreelance.webbackend.constants.LoanCurrency;
import com.cambofreelance.webbackend.constants.LoanDocumentType;
import com.cambofreelance.webbackend.constants.LoanPaymentType;
import com.cambofreelance.webbackend.constants.LoanPurpose;
import com.cambofreelance.webbackend.constants.TenorUnit;
import com.cambofreelance.webbackend.dto.request.LoanApplicationCreateRequest;
import com.cambofreelance.webbackend.dto.request.LoanApplicationStatusActionRequest;
import com.cambofreelance.webbackend.dto.request.LoanApplicationUpdateRequest;
import com.cambofreelance.webbackend.dto.request.LoanDocumentUploadRequest;
import com.cambofreelance.webbackend.dto.response.LoanApplicationDocumentResponse;
import com.cambofreelance.webbackend.dto.response.LoanApplicationResponse;
import com.cambofreelance.webbackend.dto.response.MediaFileResponse;
import com.cambofreelance.webbackend.dto.response.RepaymentInstallmentResponse;
import com.cambofreelance.webbackend.dto.response.RepaymentScheduleResponse;
import com.cambofreelance.webbackend.entities.CustomerEntity;
import com.cambofreelance.webbackend.entities.LoanApplicationDocumentEntity;
import com.cambofreelance.webbackend.entities.LoanApplicationEntity;
import com.cambofreelance.webbackend.entities.UserEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.CustomerRepository;
import com.cambofreelance.webbackend.repository.LoanApplicationDocumentRepository;
import com.cambofreelance.webbackend.repository.LoanApplicationRepository;
import com.cambofreelance.webbackend.repository.UserRepository;
import com.cambofreelance.webbackend.services.LoanApplicationService;
import com.cambofreelance.webbackend.services.MediaService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private static final BigDecimal DEFAULT_INTEREST_RATE    = new BigDecimal("12.00");
    private static final BigDecimal DEFAULT_SERVICE_FEE_RATE = new BigDecimal("5.00");

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanApplicationDocumentRepository loanApplicationDocumentRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final MediaService mediaService;

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "LOAN_APPLICATION")
    public LoanApplicationResponse create(LoanApplicationCreateRequest request, String createdByUserId) {
        CustomerEntity customer = findCustomerOrThrow(request.getCustomerId());

        validateEnums(request.getCurrency(), request.getTenorUnit(), request.getPaymentType(), request.getPurpose());
        assertMinLoanAmount(request.getLoanAmount(), request.getCurrency());

        LoanApplicationEntity entity = new LoanApplicationEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setLoanNumber(nextLoanNumber());
        entity.setCustomerId(customer.getId());
        entity.setCustomerCode(customer.getCustomerCode());
        entity.setCustomerFirstName(customer.getFirstName());
        entity.setCustomerLastName(customer.getLastName());
        entity.setLoanAmount(request.getLoanAmount());
        entity.setCurrency(request.getCurrency().toUpperCase());
        entity.setTenorValue(request.getTenorValue());
        entity.setTenorUnit(request.getTenorUnit().toUpperCase());
        entity.setPaymentType(request.getPaymentType().toUpperCase());
        entity.setInterestRate(request.getInterestRate() != null ? request.getInterestRate() : DEFAULT_INTEREST_RATE);
        entity.setServiceFeeRate(request.getServiceFeeRate() != null ? request.getServiceFeeRate() : DEFAULT_SERVICE_FEE_RATE);
        entity.setApplicationDate(today());
        entity.setFirstPaymentDate(request.getFirstPaymentDate());
        entity.setPurpose(request.getPurpose().toUpperCase());
        entity.setLoanOfficerId(StringUtils.hasText(createdByUserId) ? createdByUserId : Constants.SYSTEM);
        entity.setApplicationStatus(LoanApplicationStatus.DRAFT);
        entity.setCreatedBy(StringUtils.hasText(createdByUserId) ? createdByUserId : Constants.SYSTEM);
        recalculate(entity);

        loanApplicationRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "LOAN_APPLICATION")
    public LoanApplicationResponse update(String loanApplicationId, LoanApplicationUpdateRequest request, String updatedByUserId) {
        LoanApplicationEntity entity = findOrThrow(loanApplicationId);
        assertTransition(entity, LoanApplicationStatus.DRAFT);

        if (StringUtils.hasText(request.getCustomerId())) {
            CustomerEntity customer = findCustomerOrThrow(request.getCustomerId());
            entity.setCustomerId(customer.getId());
            entity.setCustomerCode(customer.getCustomerCode());
            entity.setCustomerFirstName(customer.getFirstName());
            entity.setCustomerLastName(customer.getLastName());
        }
        if (request.getLoanAmount() != null) {
            entity.setLoanAmount(request.getLoanAmount());
        }
        if (StringUtils.hasText(request.getCurrency())) {
            if (!LoanCurrency.isValid(request.getCurrency())) {
                throw badRequest("INVALID_CURRENCY", "Currency must be one of USD, KHR");
            }
            entity.setCurrency(request.getCurrency().toUpperCase());
        }
        if (request.getTenorValue() != null) {
            entity.setTenorValue(request.getTenorValue());
        }
        if (StringUtils.hasText(request.getTenorUnit())) {
            if (!TenorUnit.isValid(request.getTenorUnit())) {
                throw badRequest("INVALID_TENOR_UNIT", "Tenor unit must be one of DAY, MONTH, YEAR");
            }
            entity.setTenorUnit(request.getTenorUnit().toUpperCase());
        }
        if (StringUtils.hasText(request.getPaymentType())) {
            if (!LoanPaymentType.isValid(request.getPaymentType())) {
                throw badRequest("INVALID_PAYMENT_TYPE", "Payment type must be one of DAILY, WEEKLY, MONTHLY");
            }
            entity.setPaymentType(request.getPaymentType().toUpperCase());
        }
        if (request.getInterestRate() != null) {
            entity.setInterestRate(request.getInterestRate());
        }
        if (request.getServiceFeeRate() != null) {
            entity.setServiceFeeRate(request.getServiceFeeRate());
        }
        if (request.getFirstPaymentDate() != null) {
            entity.setFirstPaymentDate(request.getFirstPaymentDate());
        }
        if (StringUtils.hasText(request.getPurpose())) {
            if (!LoanPurpose.isValid(request.getPurpose())) {
                throw badRequest("INVALID_PURPOSE", "Purpose must be one of BUSINESS, FARMING, PERSONAL");
            }
            entity.setPurpose(request.getPurpose().toUpperCase());
        }
        assertMinLoanAmount(entity.getLoanAmount(), entity.getCurrency());

        entity.setUpdatedBy(updatedByUserId);
        entity.setUpdatedAt(new Date());
        recalculate(entity);

        loanApplicationRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public LoanApplicationResponse getById(String loanApplicationId) {
        return toResponse(findOrThrow(loanApplicationId));
    }

    @Override
    public Page<LoanApplicationResponse> search(String search, String applicationStatus, String customerId, int page, int size) {
        Specification<LoanApplicationEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(applicationStatus)) {
                predicates.add(cb.equal(root.get("applicationStatus"), applicationStatus.trim().toUpperCase()));
            }
            if (StringUtils.hasText(customerId)) {
                predicates.add(cb.equal(root.get("customerId"), customerId.trim()));
            }
            if (StringUtils.hasText(search)) {
                String like = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("loanNumber")), like),
                    cb.like(cb.lower(root.get("customerCode")), like)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        Page<LoanApplicationEntity> result = loanApplicationRepository.findAll(spec, PageRequest.of(page, size, sort));
        Map<String, String> userNames = resolveUserNames(result.getContent());
        return result.map(e -> LoanApplicationResponse.from(e, userNames));
    }

    @Override
    @Transactional
    @Auditable(action = "SUBMIT", module = "LOAN_APPLICATION")
    public LoanApplicationResponse submit(String loanApplicationId, String userId) {
        LoanApplicationEntity entity = findOrThrow(loanApplicationId);
        assertTransition(entity, LoanApplicationStatus.DRAFT);
        entity.setApplicationStatus(LoanApplicationStatus.PENDING_APPROVAL);
        entity.setSubmittedBy(userId);
        entity.setSubmittedAt(new Date());
        entity.setUpdatedAt(new Date());
        loanApplicationRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional
    @Auditable(action = "APPROVE", module = "LOAN_APPLICATION")
    public LoanApplicationResponse approve(String loanApplicationId, LoanApplicationStatusActionRequest request, String userId) {
        LoanApplicationEntity entity = findOrThrow(loanApplicationId);
        assertTransition(entity, LoanApplicationStatus.PENDING_APPROVAL);
        Date now = new Date();
        entity.setApplicationStatus(LoanApplicationStatus.APPROVED);
        entity.setApprovedBy(userId);
        entity.setApprovedAt(now);
        entity.setApprovalNote(request.getNote());
        entity.setUpdatedAt(now);
        loanApplicationRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional
    @Auditable(action = "REJECT", module = "LOAN_APPLICATION")
    public LoanApplicationResponse reject(String loanApplicationId, LoanApplicationStatusActionRequest request, String userId) {
        LoanApplicationEntity entity = findOrThrow(loanApplicationId);
        assertTransition(entity, LoanApplicationStatus.PENDING_APPROVAL);
        Date now = new Date();
        entity.setApplicationStatus(LoanApplicationStatus.REJECTED);
        entity.setRejectedBy(userId);
        entity.setRejectedAt(now);
        entity.setRejectionReason(request.getNote());
        entity.setUpdatedAt(now);
        loanApplicationRepository.save(entity);
        return toResponse(entity);
    }

    // ── Supporting documents ──────────────────────────────────────────────────

    @Override
    @Transactional
    @Auditable(action = "UPLOAD", module = "LOAN_APPLICATION_DOCUMENT")
    public LoanApplicationDocumentResponse uploadDocument(String loanApplicationId,
                                                          LoanDocumentUploadRequest request,
                                                          String userId) {
        findOrThrow(loanApplicationId);

        if (!LoanDocumentType.isValid(request.getDocumentType())) {
            throw badRequest("INVALID_DOCUMENT_TYPE",
                "Document type must be one of APPLICATION_FORM, ID_FRONT, ID_BACK, " +
                "INCOME_PROOF, COLLATERAL_PHOTO, BUSINESS_LICENSE, LAND_TITLE, OTHER");
        }

        MediaFileResponse media = mediaService.getById(request.getMediaId());

        // Replace any existing doc of the same type (soft-delete the old one)
        loanApplicationDocumentRepository
            .findByLoanApplicationIdAndDocumentTypeAndStatus(
                loanApplicationId, request.getDocumentType().toUpperCase(), Constants.STATUS_ACTIVE)
            .ifPresent(existing -> {
                existing.setStatus(Constants.STATUS_DELETE);
                existing.setUpdatedAt(new Date());
                loanApplicationDocumentRepository.save(existing);
            });

        LoanApplicationDocumentEntity doc = new LoanApplicationDocumentEntity();
        doc.setId(UUID.randomUUID().toString());
        doc.setLoanApplicationId(loanApplicationId);
        doc.setMediaId(request.getMediaId());
        doc.setDocumentType(request.getDocumentType().toUpperCase());
        doc.setCreatedBy(StringUtils.hasText(userId) ? userId : Constants.SYSTEM);
        loanApplicationDocumentRepository.save(doc);

        return LoanApplicationDocumentResponse.from(doc, media);
    }

    @Override
    public List<LoanApplicationDocumentResponse> listDocuments(String loanApplicationId) {
        findOrThrow(loanApplicationId);
        return loanApplicationDocumentRepository
            .findByLoanApplicationIdAndStatus(loanApplicationId, Constants.STATUS_ACTIVE)
            .stream()
            .map(doc -> LoanApplicationDocumentResponse.from(doc, mediaService.getById(doc.getMediaId())))
            .toList();
    }

    @Override
    @Transactional
    public void deleteDocument(String loanApplicationId, String documentId, String userId) {
        LoanApplicationDocumentEntity doc = loanApplicationDocumentRepository.findById(documentId)
            .filter(d -> d.getLoanApplicationId().equals(loanApplicationId))
            .orElseThrow(() -> notFound("LOAN_DOCUMENT_NOT_FOUND",
                "Document not found: " + documentId));
        doc.setStatus(Constants.STATUS_DELETE);
        doc.setUpdatedBy(userId);
        doc.setUpdatedAt(new Date());
        loanApplicationDocumentRepository.save(doc);
    }

    // ── Repayment schedule ────────────────────────────────────────────────────

    @Override
    public RepaymentScheduleResponse calculateRepaymentSchedule(String loanApplicationId) {
        LoanApplicationEntity loan = findOrThrow(loanApplicationId);

        int n = numberOfPayments(loan.getTenorValue(), loan.getTenorUnit(), loan.getPaymentType());

        BigDecimal principal    = loan.getLoanAmount();
        BigDecimal totalInterest = loan.getInterestAmount();
        BigDecimal totalRepayment = principal.add(totalInterest);
        BigDecimal hundred = BigDecimal.valueOf(100);

        // Distribute principal and interest equally; last installment absorbs rounding remainder
        BigDecimal principalPerPmt = principal.divide(BigDecimal.valueOf(n), 2, RoundingMode.DOWN);
        BigDecimal interestPerPmt  = totalInterest.divide(BigDecimal.valueOf(n), 2, RoundingMode.DOWN);

        BigDecimal principalRemainder = principal.subtract(principalPerPmt.multiply(BigDecimal.valueOf(n - 1)));
        BigDecimal interestRemainder  = totalInterest.subtract(interestPerPmt.multiply(BigDecimal.valueOf(n - 1)));

        List<RepaymentInstallmentResponse> schedule = new ArrayList<>();
        BigDecimal balance = principal;
        LocalDate dueDate  = toLocalDate(loan.getFirstPaymentDate());

        for (int i = 1; i <= n; i++) {
            BigDecimal pmt = (i == n) ? principalRemainder : principalPerPmt;
            BigDecimal int_ = (i == n) ? interestRemainder  : interestPerPmt;
            balance = balance.subtract(pmt);

            schedule.add(RepaymentInstallmentResponse.builder()
                .installmentNo(i)
                .dueDate(toDate(dueDate))
                .principalAmount(pmt)
                .interestAmount(int_)
                .totalPayment(pmt.add(int_))
                .remainingBalance(balance)
                .build());

            dueDate = nextDueDate(dueDate, loan.getPaymentType());
        }

        return RepaymentScheduleResponse.builder()
            .loanApplicationId(loan.getId())
            .loanNumber(loan.getLoanNumber())
            .customerCode(loan.getCustomerCode())
            .currency(loan.getCurrency())
            .loanAmount(principal)
            .interestRate(loan.getInterestRate())
            .serviceFeeRate(loan.getServiceFeeRate())
            .interestAmount(totalInterest)
            .serviceFeeAmount(loan.getServiceFeeAmount())
            .netDisbursement(loan.getNetDisbursement())
            .totalRepayment(totalRepayment)
            .numberOfPayments(n)
            .paymentType(loan.getPaymentType())
            .firstPaymentDate(loan.getFirstPaymentDate())
            .schedule(schedule)
            .build();
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /** Recomputes the three derived money fields from the current entity state. */
    private void recalculate(LoanApplicationEntity entity) {
        BigDecimal amount      = entity.getLoanAmount();
        BigDecimal feeRate     = entity.getServiceFeeRate();
        BigDecimal intRate     = entity.getInterestRate();
        BigDecimal hundred     = BigDecimal.valueOf(100);

        BigDecimal serviceFee  = amount.multiply(feeRate).divide(hundred, 2, RoundingMode.HALF_UP);
        BigDecimal interest    = amount.multiply(intRate).divide(hundred, 2, RoundingMode.HALF_UP);
        BigDecimal netDisb     = amount.subtract(serviceFee);

        entity.setServiceFeeAmount(serviceFee);
        entity.setInterestAmount(interest);
        entity.setNetDisbursement(netDisb);
    }

    /** Generates the next loan number in the format LN-{YYYY}{seq:06d}. */
    private String nextLoanNumber() {
        long seq  = loanApplicationRepository.nextLoanApplicationSequence();
        int  year = LocalDate.now().getYear();
        return String.format("LN-%d%06d", year, seq);
    }

    private LoanApplicationResponse toResponse(LoanApplicationEntity entity) {
        return LoanApplicationResponse.from(entity, resolveUserNames(List.of(entity)));
    }

    private Map<String, String> resolveUserNames(List<LoanApplicationEntity> entities) {
        Set<String> ids = entities.stream()
            .flatMap(e -> Arrays.stream(new String[]{
                e.getLoanOfficerId(), e.getSubmittedBy(), e.getApprovedBy(), e.getRejectedBy()
            }))
            .filter(StringUtils::hasText)
            .collect(Collectors.toSet());
        if (ids.isEmpty()) {
            return Map.of();
        }
        return userRepository.findAllById(ids).stream()
            .collect(Collectors.toMap(UserEntity::getUserId, UserEntity::getUsername));
    }

    private LoanApplicationEntity findOrThrow(String loanApplicationId) {
        return loanApplicationRepository.findById(loanApplicationId)
            .orElseThrow(() -> notFound("LOAN_APPLICATION_NOT_FOUND", "Loan application not found: " + loanApplicationId));
    }

    private CustomerEntity findCustomerOrThrow(String customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> badRequest("CUSTOMER_NOT_FOUND", "Customer not found: " + customerId));
    }

    private void validateEnums(String currency, String tenorUnit, String paymentType, String purpose) {
        if (!LoanCurrency.isValid(currency)) {
            throw badRequest("INVALID_CURRENCY", "Currency must be one of USD, KHR");
        }
        if (!TenorUnit.isValid(tenorUnit)) {
            throw badRequest("INVALID_TENOR_UNIT", "Tenor unit must be one of DAY, MONTH, YEAR");
        }
        if (!LoanPaymentType.isValid(paymentType)) {
            throw badRequest("INVALID_PAYMENT_TYPE", "Payment type must be one of DAILY, WEEKLY, MONTHLY");
        }
        if (!LoanPurpose.isValid(purpose)) {
            throw badRequest("INVALID_PURPOSE", "Purpose must be one of BUSINESS, FARMING, PERSONAL");
        }
    }

    private void assertMinLoanAmount(BigDecimal loanAmount, String currency) {
        BigDecimal minAmount = LoanCurrency.minLoanAmount(currency);
        if (loanAmount == null || loanAmount.compareTo(minAmount) < 0) {
            throw badRequest("LOAN_AMOUNT_TOO_LOW",
                "Loan amount must be at least " + minAmount + " " + currency.toUpperCase());
        }
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
            default -> switch (paymentType) {           // YEAR
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
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private void assertTransition(LoanApplicationEntity entity, String... allowedFrom) {
        if (Arrays.stream(allowedFrom).noneMatch(s -> s.equals(entity.getApplicationStatus()))) {
            throw badRequest("INVALID_LOAN_APPLICATION_STATUS_TRANSITION",
                "Loan application is in status " + entity.getApplicationStatus()
                    + "; expected one of " + Arrays.toString(allowedFrom));
        }
    }

    private Date today() {
        return toDate(LocalDate.now());
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
