package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.DisbursementStatus;
import com.cambofreelance.webbackend.constants.LoanApplicationStatus;
import com.cambofreelance.webbackend.dto.request.LoanDisbursementContractRequest;
import com.cambofreelance.webbackend.dto.request.LoanDisbursementCreateRequest;
import com.cambofreelance.webbackend.dto.request.LoanDisbursementDisburseRequest;
import com.cambofreelance.webbackend.dto.response.LoanDisbursementResponse;
import com.cambofreelance.webbackend.dto.response.MediaFileResponse;
import com.cambofreelance.webbackend.entities.LoanApplicationEntity;
import com.cambofreelance.webbackend.entities.LoanDisbursementEntity;
import com.cambofreelance.webbackend.entities.UserEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.LoanApplicationRepository;
import com.cambofreelance.webbackend.repository.LoanDisbursementRepository;
import com.cambofreelance.webbackend.repository.UserRepository;
import com.cambofreelance.webbackend.services.LoanDisbursementService;
import com.cambofreelance.webbackend.services.MediaService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
public class LoanDisbursementServiceImpl implements LoanDisbursementService {

    private final LoanDisbursementRepository loanDisbursementRepository;
    private final LoanApplicationRepository  loanApplicationRepository;
    private final UserRepository             userRepository;
    private final MediaService               mediaService;

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "LOAN_DISBURSEMENT")
    public LoanDisbursementResponse create(LoanDisbursementCreateRequest request, String userId) {
        LoanApplicationEntity loan = loanApplicationRepository.findById(request.getLoanApplicationId())
            .orElseThrow(() -> badRequest("LOAN_APPLICATION_NOT_FOUND",
                "Loan application not found: " + request.getLoanApplicationId()));

        if (!LoanApplicationStatus.APPROVED.equals(loan.getApplicationStatus())) {
            throw badRequest("LOAN_NOT_APPROVED",
                "Loan must be APPROVED before generating a disbursement. Current status: " + loan.getApplicationStatus());
        }

        if (loanDisbursementRepository.existsByLoanApplicationId(loan.getId())) {
            throw badRequest("DISBURSEMENT_ALREADY_EXISTS",
                "A disbursement record already exists for this loan application.");
        }

        LoanDisbursementEntity entity = new LoanDisbursementEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setLoanApplicationId(loan.getId());
        entity.setLoanNumber(loan.getLoanNumber());
        entity.setCustomerId(loan.getCustomerId());
        entity.setCustomerCode(loan.getCustomerCode());
        entity.setCustomerFirstName(loan.getCustomerFirstName());
        entity.setCustomerLastName(loan.getCustomerLastName());
        entity.setDisbursementAmount(
            request.getDisbursementAmount() != null ? request.getDisbursementAmount() : loan.getNetDisbursement()
        );
        entity.setCurrency(loan.getCurrency());
        entity.setDisbursementStatus(DisbursementStatus.PENDING);
        entity.setNotes(request.getNotes());
        entity.setCreatedBy(StringUtils.hasText(userId) ? userId : Constants.SYSTEM);
        loanDisbursementRepository.save(entity);

        return toResponse(entity);
    }

    @Override
    public LoanDisbursementResponse getById(String disbursementId) {
        return toResponse(findOrThrow(disbursementId));
    }

    @Override
    public LoanDisbursementResponse getByLoanApplicationId(String loanApplicationId) {
        LoanDisbursementEntity entity = loanDisbursementRepository.findByLoanApplicationId(loanApplicationId)
            .orElseThrow(() -> notFound("DISBURSEMENT_NOT_FOUND",
                "No disbursement found for loan application: " + loanApplicationId));
        return toResponse(entity);
    }

    @Override
    public Page<LoanDisbursementResponse> search(String search, String disbursementStatus, int page, int size) {
        Specification<LoanDisbursementEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(disbursementStatus)) {
                predicates.add(cb.equal(root.get("disbursementStatus"), disbursementStatus.trim().toUpperCase()));
            }
            if (StringUtils.hasText(search)) {
                String like = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("loanNumber")),     like),
                    cb.like(cb.lower(root.get("customerCode")),   like),
                    cb.like(cb.lower(root.get("customerFirstName")), like),
                    cb.like(cb.lower(root.get("customerLastName")),  like)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<LoanDisbursementEntity> result = loanDisbursementRepository.findAll(
            spec, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")))
        );
        return result.map(this::toResponse);
    }

    @Override
    @Transactional
    @Auditable(action = "UPLOAD_CONTRACT", module = "LOAN_DISBURSEMENT")
    public LoanDisbursementResponse uploadContract(String disbursementId,
                                                   LoanDisbursementContractRequest request,
                                                   String userId) {
        LoanDisbursementEntity entity = findOrThrow(disbursementId);
        mediaService.getById(request.getMediaId()); // validate media exists
        entity.setContractMediaId(request.getMediaId());
        entity.setUpdatedBy(userId);
        entity.setUpdatedAt(new Date());
        loanDisbursementRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional
    @Auditable(action = "DISBURSE", module = "LOAN_DISBURSEMENT")
    public LoanDisbursementResponse disburse(String disbursementId,
                                             LoanDisbursementDisburseRequest request,
                                             String userId) {
        LoanDisbursementEntity entity = findOrThrow(disbursementId);

        if (!DisbursementStatus.PENDING.equals(entity.getDisbursementStatus())) {
            throw badRequest("DISBURSEMENT_ALREADY_COMPLETED",
                "Disbursement is already " + entity.getDisbursementStatus());
        }

        Date now = new Date();
        entity.setDisbursementStatus(DisbursementStatus.COMPLETED);
        entity.setDisbursementDate(request.getDisbursementDate());
        entity.setDisbursedById(userId);
        if (StringUtils.hasText(request.getNotes())) {
            entity.setNotes(request.getNotes());
        }
        entity.setUpdatedBy(userId);
        entity.setUpdatedAt(now);
        loanDisbursementRepository.save(entity);
        return toResponse(entity);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private LoanDisbursementResponse toResponse(LoanDisbursementEntity entity) {
        String disbursedByName = null;
        if (StringUtils.hasText(entity.getDisbursedById())) {
            disbursedByName = userRepository.findById(entity.getDisbursedById())
                .map(UserEntity::getUsername)
                .orElse(null);
        }
        MediaFileResponse contract = null;
        if (StringUtils.hasText(entity.getContractMediaId())) {
            try {
                contract = mediaService.getById(entity.getContractMediaId());
            } catch (Exception ignored) {}
        }
        return LoanDisbursementResponse.from(entity, disbursedByName, contract);
    }

    private LoanDisbursementEntity findOrThrow(String disbursementId) {
        return loanDisbursementRepository.findById(disbursementId)
            .orElseThrow(() -> notFound("DISBURSEMENT_NOT_FOUND",
                "Disbursement not found: " + disbursementId));
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
