package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.constants.CollectionNoteType;
import com.cambofreelance.webbackend.constants.CollectionStatus;
import com.cambofreelance.webbackend.constants.InstallmentStatus;
import com.cambofreelance.webbackend.constants.PtpStatus;
import com.cambofreelance.webbackend.dto.request.CollectionAssignRequest;
import com.cambofreelance.webbackend.dto.request.CollectionNoteCreateRequest;
import com.cambofreelance.webbackend.dto.request.CollectionStatusUpdateRequest;
import com.cambofreelance.webbackend.dto.request.PromiseToPayCreateRequest;
import com.cambofreelance.webbackend.dto.request.PtpStatusUpdateRequest;
import com.cambofreelance.webbackend.dto.response.CollectionCaseResponse;
import com.cambofreelance.webbackend.dto.response.CollectionNoteResponse;
import com.cambofreelance.webbackend.dto.response.PromiseToPayResponse;
import com.cambofreelance.webbackend.entities.CollectionCaseEntity;
import com.cambofreelance.webbackend.entities.CollectionNoteEntity;
import com.cambofreelance.webbackend.entities.LoanApplicationEntity;
import com.cambofreelance.webbackend.entities.PromiseToPayEntity;
import com.cambofreelance.webbackend.entities.RepaymentInstallmentEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.CollectionCaseRepository;
import com.cambofreelance.webbackend.repository.CollectionNoteRepository;
import com.cambofreelance.webbackend.repository.LoanApplicationRepository;
import com.cambofreelance.webbackend.repository.PromiseToPayRepository;
import com.cambofreelance.webbackend.repository.RepaymentInstallmentRepository;
import com.cambofreelance.webbackend.services.CollectionService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private static final BigDecimal DEFAULT_PENALTY_RATE = new BigDecimal("0.001");
    private static final Set<String> NOTE_TYPES = Set.of(
        CollectionNoteType.CALL, CollectionNoteType.VISIT,
        CollectionNoteType.SMS, CollectionNoteType.EMAIL, CollectionNoteType.OTHER
    );
    private static final Set<String> COLLECTION_STATUSES = Set.of(
        CollectionStatus.CURRENT, CollectionStatus.DELINQUENT,
        CollectionStatus.OVERDUE, CollectionStatus.WRITTEN_OFF
    );
    private static final Set<String> PTP_STATUSES = Set.of(
        PtpStatus.PENDING, PtpStatus.KEPT, PtpStatus.BROKEN, PtpStatus.CANCELLED
    );

    private final CollectionCaseRepository caseRepository;
    private final CollectionNoteRepository noteRepository;
    private final PromiseToPayRepository ptpRepository;
    private final LoanApplicationRepository loanRepository;
    private final RepaymentInstallmentRepository installmentRepository;

    @Override
    @Transactional
    public int syncOverdueCases(String userId) {
        List<String> overdueLoanIds = installmentRepository
            .findDistinctLoanApplicationIdsByInstallmentStatus(InstallmentStatus.OVERDUE);

        int count = 0;
        for (String loanId : overdueLoanIds) {
            loanRepository.findById(loanId).ifPresent(loan -> syncCase(loan, userId));
            count++;
        }
        return count;
    }

    @Override
    public Page<CollectionCaseResponse> search(String collectionStatus, String assignedOfficerId,
                                               String currency, int page, int size) {
        return caseRepository.search(
            blankToNull(collectionStatus),
            blankToNull(assignedOfficerId),
            blankToNull(currency),
            PageRequest.of(page, size)
        ).map(CollectionCaseResponse::from);
    }

    @Override
    public CollectionCaseResponse getById(String id) {
        return CollectionCaseResponse.from(requireCase(id));
    }

    @Override
    @Transactional
    public CollectionCaseResponse refreshCase(String id, String userId) {
        CollectionCaseEntity entity = requireCase(id);
        loanRepository.findById(entity.getLoanApplicationId())
            .ifPresent(loan -> applyMetrics(entity, userId));
        caseRepository.save(entity);
        return CollectionCaseResponse.from(entity);
    }

    @Override
    @Transactional
    public CollectionCaseResponse updateStatus(String id, CollectionStatusUpdateRequest req, String userId) {
        if (!COLLECTION_STATUSES.contains(req.getCollectionStatus())) {
            throw badRequest("INVALID_STATUS", "Invalid collection status: " + req.getCollectionStatus());
        }
        CollectionCaseEntity entity = requireCase(id);
        entity.setCollectionStatus(req.getCollectionStatus());
        if (req.getRemarks() != null) entity.setRemarks(req.getRemarks());
        entity.setUpdatedBy(userId);
        entity.setUpdatedAt(new Date());
        caseRepository.save(entity);
        return CollectionCaseResponse.from(entity);
    }

    @Override
    @Transactional
    public CollectionCaseResponse assign(String id, CollectionAssignRequest req, String userId) {
        CollectionCaseEntity entity = requireCase(id);
        entity.setAssignedOfficerId(req.getAssignedOfficerId());
        entity.setUpdatedBy(userId);
        entity.setUpdatedAt(new Date());
        caseRepository.save(entity);
        return CollectionCaseResponse.from(entity);
    }

    @Override
    @Transactional
    public CollectionNoteResponse addNote(String id, CollectionNoteCreateRequest req, String userId) {
        CollectionCaseEntity caseEntity = requireCase(id);
        if (!NOTE_TYPES.contains(req.getNoteType().toUpperCase())) {
            throw badRequest("INVALID_NOTE_TYPE", "Invalid note type: " + req.getNoteType());
        }
        CollectionNoteEntity note = new CollectionNoteEntity();
        note.setId(UUID.randomUUID().toString());
        note.setCollectionCaseId(id);
        note.setLoanApplicationId(caseEntity.getLoanApplicationId());
        note.setNoteType(req.getNoteType().toUpperCase());
        note.setContent(req.getContent());
        note.setContactPerson(req.getContactPerson());
        note.setContactResult(req.getContactResult());
        note.setCreatedBy(userId);
        note.setCreatedAt(new Date());
        note.setStatus("ACT");
        noteRepository.save(note);
        return CollectionNoteResponse.from(note);
    }

    @Override
    public List<CollectionNoteResponse> getNotes(String id) {
        requireCase(id);
        return noteRepository.findByCollectionCaseIdOrderByCreatedAtDesc(id)
            .stream().map(CollectionNoteResponse::from).toList();
    }

    @Override
    @Transactional
    public PromiseToPayResponse addPromise(String id, PromiseToPayCreateRequest req, String userId) {
        CollectionCaseEntity caseEntity = requireCase(id);
        PromiseToPayEntity ptp = new PromiseToPayEntity();
        ptp.setId(UUID.randomUUID().toString());
        ptp.setCollectionCaseId(id);
        ptp.setLoanApplicationId(caseEntity.getLoanApplicationId());
        ptp.setPromiseDate(req.getPromiseDate());
        ptp.setPromiseAmount(req.getPromiseAmount());
        ptp.setPtpStatus(PtpStatus.PENDING);
        ptp.setNotes(req.getNotes());
        ptp.setCreatedBy(userId);
        ptp.setCreatedAt(new Date());
        ptp.setStatus("ACT");
        ptpRepository.save(ptp);
        return PromiseToPayResponse.from(ptp);
    }

    @Override
    public List<PromiseToPayResponse> getPromises(String id) {
        requireCase(id);
        return ptpRepository.findByCollectionCaseIdOrderByCreatedAtDesc(id)
            .stream().map(PromiseToPayResponse::from).toList();
    }

    @Override
    @Transactional
    public PromiseToPayResponse updatePtpStatus(String ptpId, PtpStatusUpdateRequest req, String userId) {
        if (!PTP_STATUSES.contains(req.getPtpStatus())) {
            throw badRequest("INVALID_PTP_STATUS", "Invalid PTP status: " + req.getPtpStatus());
        }
        PromiseToPayEntity ptp = ptpRepository.findById(ptpId)
            .orElseThrow(() -> notFound("PTP_NOT_FOUND", "Promise to pay not found"));
        ptp.setPtpStatus(req.getPtpStatus());
        ptp.setUpdatedBy(userId);
        ptp.setUpdatedAt(new Date());
        ptpRepository.save(ptp);
        return PromiseToPayResponse.from(ptp);
    }

    private void syncCase(LoanApplicationEntity loan, String userId) {
        CollectionCaseEntity entity = caseRepository
            .findByLoanApplicationId(loan.getId())
            .orElseGet(() -> {
                CollectionCaseEntity c = new CollectionCaseEntity();
                c.setId(UUID.randomUUID().toString());
                c.setLoanApplicationId(loan.getId());
                c.setLoanNumber(loan.getLoanNumber());
                c.setCurrency(loan.getCurrency());
                c.setCustomerId(loan.getCustomerId());
                c.setCustomerCode(loan.getCustomerCode());
                c.setCustomerFirstName(loan.getCustomerFirstName());
                c.setCustomerLastName(loan.getCustomerLastName());
                c.setPenaltyRate(DEFAULT_PENALTY_RATE);
                c.setTotalOverdueAmount(BigDecimal.ZERO);
                c.setPenaltyAmount(BigDecimal.ZERO);
                c.setCreatedBy(userId);
                c.setCreatedAt(new Date());
                c.setStatus("ACT");
                return c;
            });
        applyMetrics(entity, userId);
        caseRepository.save(entity);
    }

    private void applyMetrics(CollectionCaseEntity entity, String userId) {
        int dpd = computeDpd(entity.getLoanApplicationId());
        BigDecimal overdue = installmentRepository.sumRemainingBalanceByStatus(
            entity.getLoanApplicationId(), InstallmentStatus.OVERDUE);
        BigDecimal penaltyRate = entity.getPenaltyRate() != null
            ? entity.getPenaltyRate() : DEFAULT_PENALTY_RATE;
        BigDecimal penalty = overdue.multiply(penaltyRate)
            .multiply(BigDecimal.valueOf(dpd))
            .setScale(2, RoundingMode.HALF_UP);

        entity.setDpd(dpd);
        entity.setTotalOverdueAmount(overdue);
        entity.setPenaltyRate(penaltyRate);
        entity.setPenaltyAmount(penalty);
        entity.setCollectionStatus(CollectionStatus.fromDpd(dpd));
        entity.setUpdatedBy(userId);
        entity.setUpdatedAt(new Date());
    }

    private int computeDpd(String loanApplicationId) {
        return installmentRepository
            .findFirstByLoanApplicationIdAndInstallmentStatusOrderByDueDateAsc(
                loanApplicationId, InstallmentStatus.OVERDUE)
            .map(inst -> {
                LocalDate due = Instant.ofEpochMilli(inst.getDueDate().getTime())
                    .atZone(ZoneId.systemDefault()).toLocalDate();
                return (int) ChronoUnit.DAYS.between(due, LocalDate.now());
            })
            .orElse(0);
    }

    private CollectionCaseEntity requireCase(String id) {
        return caseRepository.findById(id)
            .orElseThrow(() -> notFound("CASE_NOT_FOUND", "Collection case not found"));
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
