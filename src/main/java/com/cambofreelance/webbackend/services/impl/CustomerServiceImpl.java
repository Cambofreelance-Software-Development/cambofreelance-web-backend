package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.CustomerDocumentType;
import com.cambofreelance.webbackend.constants.CustomerStatus;
import com.cambofreelance.webbackend.constants.Gender;
import com.cambofreelance.webbackend.dto.request.CustomerCreateRequest;
import com.cambofreelance.webbackend.dto.request.CustomerDocumentUploadRequest;
import com.cambofreelance.webbackend.dto.request.CustomerStatusActionRequest;
import com.cambofreelance.webbackend.dto.request.CustomerUpdateRequest;
import com.cambofreelance.webbackend.dto.response.CustomerDocumentResponse;
import com.cambofreelance.webbackend.dto.response.CustomerResponse;
import com.cambofreelance.webbackend.dto.response.MediaFileResponse;
import com.cambofreelance.webbackend.entities.CustomerDocumentEntity;
import com.cambofreelance.webbackend.entities.CustomerEntity;
import com.cambofreelance.webbackend.entities.UserEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.CustomerDocumentRepository;
import com.cambofreelance.webbackend.repository.CustomerRepository;
import com.cambofreelance.webbackend.repository.UserRepository;
import com.cambofreelance.webbackend.services.CustomerService;
import com.cambofreelance.webbackend.services.MediaService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
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
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerDocumentRepository customerDocumentRepository;
    private final MediaService mediaService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "CUSTOMER")
    public CustomerResponse create(CustomerCreateRequest request, String createdByUserId) {
        assertNoDuplicate(request.getPhoneNumber(), request.getIdentityCardNumber(), null);

        CustomerEntity entity = new CustomerEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCustomerCode(nextCustomerCode());
        applyFields(entity, request.getFirstName(), request.getLastName(), request.getGender(),
            request.getDateOfBirth(), request.getPhoneNumber(), request.getAlternatePhone(),
            request.getIdentityCardNumber(), request.getAddress(), request.getOccupation(),
            request.getEmployerName(), request.getMonthlyIncome(), request.getGuarantorName(),
            request.getGuarantorPhone());
        entity.setOnboardingStatus(CustomerStatus.DRAFT);
        entity.setCreatedBy(StringUtils.hasText(createdByUserId) ? createdByUserId : Constants.SYSTEM);
        customerRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional
    public CustomerResponse update(String customerId, CustomerUpdateRequest request, String updatedByUserId) {
        CustomerEntity entity = findOrThrow(customerId);
        assertTransition(entity, CustomerStatus.DRAFT, CustomerStatus.REJECTED, CustomerStatus.ACTIVE, CustomerStatus.INACTIVE);
        assertNoDuplicate(request.getPhoneNumber(), request.getIdentityCardNumber(), customerId);

        applyFields(entity, request.getFirstName(), request.getLastName(), request.getGender(),
            request.getDateOfBirth(), request.getPhoneNumber(), request.getAlternatePhone(),
            request.getIdentityCardNumber(), request.getAddress(), request.getOccupation(),
            request.getEmployerName(), request.getMonthlyIncome(), request.getGuarantorName(),
            request.getGuarantorPhone());
        entity.setUpdatedBy(updatedByUserId);
        entity.setUpdatedAt(new Date());
        customerRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    public CustomerResponse getById(String customerId) {
        return toResponse(findOrThrow(customerId));
    }

    @Override
    public Page<CustomerResponse> search(String search, String onboardingStatus, int page, int size) {
        Specification<CustomerEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(onboardingStatus)) {
                predicates.add(cb.equal(root.get("onboardingStatus"), onboardingStatus.trim().toUpperCase()));
            }
            if (StringUtils.hasText(search)) {
                String like = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("firstName")), like),
                    cb.like(cb.lower(root.get("lastName")), like),
                    cb.like(cb.lower(root.get("customerCode")), like),
                    cb.like(cb.lower(root.get("phoneNumber")), like)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        Page<CustomerEntity> result = customerRepository.findAll(spec, PageRequest.of(page, size, sort));
        Map<String, String> userNames = resolveUserNames(result.getContent());
        return result.map(e -> CustomerResponse.from(e, userNames));
    }

    @Override
    @Transactional
    @Auditable(action = "UPLOAD", module = "CUSTOMER_DOCUMENT")
    public CustomerDocumentResponse uploadDocument(String customerId, CustomerDocumentUploadRequest request, String uploadedByUserId) {
        findOrThrow(customerId);
        if (!CustomerDocumentType.isValid(request.getDocumentType())) {
            throw badRequest("INVALID_DOCUMENT_TYPE",
                "Document type must be one of NATIONAL_ID_FRONT, NATIONAL_ID_BACK, CUSTOMER_PHOTO, ADDRESS_VERIFICATION, INCOME_VERIFICATION, GUARANTOR_ID");
        }
        MediaFileResponse media = mediaService.getById(request.getMediaId());

        customerDocumentRepository
            .findByCustomerIdAndDocumentTypeAndStatus(customerId, request.getDocumentType().toUpperCase(), Constants.STATUS_ACTIVE)
            .ifPresent(existing -> {
                existing.setStatus(Constants.STATUS_DELETE);
                existing.setUpdatedAt(new Date());
                customerDocumentRepository.save(existing);
            });

        CustomerDocumentEntity doc = new CustomerDocumentEntity();
        doc.setId(UUID.randomUUID().toString());
        doc.setCustomerId(customerId);
        doc.setMediaId(request.getMediaId());
        doc.setDocumentType(request.getDocumentType().toUpperCase());
        doc.setCreatedBy(StringUtils.hasText(uploadedByUserId) ? uploadedByUserId : Constants.SYSTEM);
        customerDocumentRepository.save(doc);
        return CustomerDocumentResponse.from(doc, media);
    }

    @Override
    public List<CustomerDocumentResponse> listDocuments(String customerId) {
        findOrThrow(customerId);
        return customerDocumentRepository.findByCustomerIdAndStatus(customerId, Constants.STATUS_ACTIVE).stream()
            .map(doc -> CustomerDocumentResponse.from(doc, mediaService.getById(doc.getMediaId())))
            .toList();
    }

    @Override
    @Transactional
    public void deleteDocument(String customerId, String documentId, String userId) {
        CustomerDocumentEntity doc = customerDocumentRepository.findById(documentId)
            .filter(d -> d.getCustomerId().equals(customerId))
            .orElseThrow(() -> notFound("CUSTOMER_DOCUMENT_NOT_FOUND", "Customer document not found: " + documentId));
        doc.setStatus(Constants.STATUS_DELETE);
        doc.setUpdatedBy(userId);
        doc.setUpdatedAt(new Date());
        customerDocumentRepository.save(doc);
    }

    @Override
    @Transactional
    @Auditable(action = "SUBMIT", module = "CUSTOMER")
    public CustomerResponse submitForVerification(String customerId, String userId) {
        CustomerEntity entity = findOrThrow(customerId);
        assertTransition(entity, CustomerStatus.DRAFT, CustomerStatus.REJECTED);
        entity.setOnboardingStatus(CustomerStatus.PENDING_VERIFICATION);
        entity.setSubmittedBy(userId);
        entity.setSubmittedAt(new Date());
        entity.setUpdatedAt(new Date());
        customerRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional
    @Auditable(action = "VERIFY", module = "CUSTOMER")
    public CustomerResponse verify(String customerId, CustomerStatusActionRequest request, String userId) {
        CustomerEntity entity = findOrThrow(customerId);
        assertTransition(entity, CustomerStatus.PENDING_VERIFICATION);
        entity.setOnboardingStatus(CustomerStatus.VERIFIED);
        entity.setVerifiedBy(userId);
        entity.setVerifiedAt(new Date());
        entity.setVerificationNote(request.getNote());
        entity.setUpdatedAt(new Date());
        customerRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional
    @Auditable(action = "APPROVE", module = "CUSTOMER")
    public CustomerResponse approve(String customerId, CustomerStatusActionRequest request, String userId) {
        CustomerEntity entity = findOrThrow(customerId);
        assertTransition(entity, CustomerStatus.VERIFIED);
        Date now = new Date();
        entity.setApprovedBy(userId);
        entity.setApprovedAt(now);
        entity.setApprovalNote(request.getNote());
        entity.setOnboardingStatus(CustomerStatus.ACTIVE);
        entity.setUpdatedAt(now);
        customerRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional
    @Auditable(action = "REJECT", module = "CUSTOMER")
    public CustomerResponse reject(String customerId, CustomerStatusActionRequest request, String userId) {
        CustomerEntity entity = findOrThrow(customerId);
        assertTransition(entity, CustomerStatus.PENDING_VERIFICATION);
        entity.setOnboardingStatus(CustomerStatus.REJECTED);
        entity.setRejectedBy(userId);
        entity.setRejectedAt(new Date());
        entity.setRejectionReason(request.getNote());
        entity.setUpdatedAt(new Date());
        customerRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional
    public CustomerResponse activate(String customerId, String userId) {
        CustomerEntity entity = findOrThrow(customerId);
        assertTransition(entity, CustomerStatus.INACTIVE);
        entity.setOnboardingStatus(CustomerStatus.ACTIVE);
        entity.setUpdatedBy(userId);
        entity.setUpdatedAt(new Date());
        customerRepository.save(entity);
        return toResponse(entity);
    }

    @Override
    @Transactional
    public CustomerResponse deactivate(String customerId, String userId) {
        CustomerEntity entity = findOrThrow(customerId);
        assertTransition(entity, CustomerStatus.ACTIVE);
        entity.setOnboardingStatus(CustomerStatus.INACTIVE);
        entity.setUpdatedBy(userId);
        entity.setUpdatedAt(new Date());
        customerRepository.save(entity);
        return toResponse(entity);
    }

    private void applyFields(CustomerEntity entity, String firstName, String lastName, String gender,
        Date dateOfBirth, String phoneNumber, String alternatePhone, String identityCardNumber,
        String address, String occupation, String employerName, BigDecimal monthlyIncome,
        String guarantorName, String guarantorPhone) {
        if (!Gender.isValid(gender)) {
            throw badRequest("INVALID_GENDER", "Gender must be one of MALE, FEMALE, OTHER");
        }
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setGender(gender.toUpperCase());
        entity.setDateOfBirth(dateOfBirth);
        entity.setPhoneNumber(phoneNumber);
        entity.setAlternatePhone(alternatePhone);
        entity.setIdentityCardNumber(identityCardNumber);
        entity.setAddress(address);
        entity.setOccupation(occupation);
        entity.setEmployerName(employerName);
        entity.setMonthlyIncome(monthlyIncome);
        entity.setGuarantorName(guarantorName);
        entity.setGuarantorPhone(guarantorPhone);
    }

    private void assertNoDuplicate(String phoneNumber, String identityCardNumber, String excludeId) {
        boolean phoneExists = excludeId == null
            ? customerRepository.existsByPhoneNumber(phoneNumber)
            : customerRepository.existsByPhoneNumberAndIdNot(phoneNumber, excludeId);
        if (phoneExists) {
            throw conflict("PHONE_ALREADY_EXISTS", "A customer with this phone number already exists: " + phoneNumber);
        }
        if (StringUtils.hasText(identityCardNumber)) {
            boolean idExists = excludeId == null
                ? customerRepository.existsByIdentityCardNumber(identityCardNumber)
                : customerRepository.existsByIdentityCardNumberAndIdNot(identityCardNumber, excludeId);
            if (idExists) {
                throw conflict("IDENTITY_CARD_ALREADY_EXISTS", "A customer with this identity card number already exists: " + identityCardNumber);
            }
        }
    }

    private void assertTransition(CustomerEntity entity, String... allowedFrom) {
        if (Arrays.stream(allowedFrom).noneMatch(s -> s.equals(entity.getOnboardingStatus()))) {
            throw badRequest("INVALID_CUSTOMER_STATUS_TRANSITION",
                "Customer is in status " + entity.getOnboardingStatus() + "; expected one of " + Arrays.toString(allowedFrom));
        }
    }

    private CustomerResponse toResponse(CustomerEntity entity) {
        return CustomerResponse.from(entity, resolveUserNames(List.of(entity)));
    }

    private Map<String, String> resolveUserNames(List<CustomerEntity> entities) {
        Set<String> ids = entities.stream()
            .flatMap(e -> Arrays.stream(new String[] {
                e.getSubmittedBy(), e.getVerifiedBy(), e.getApprovedBy(), e.getRejectedBy()
            }))
            .filter(StringUtils::hasText)
            .collect(Collectors.toSet());
        if (ids.isEmpty()) {
            return Map.of();
        }
        return userRepository.findAllById(ids).stream()
            .collect(Collectors.toMap(UserEntity::getUserId, UserEntity::getUsername));
    }

    private String nextCustomerCode() {
        long seq = customerRepository.nextCustomerSequence();
        return String.format("CUS-%08d", seq);
    }

    private CustomerEntity findOrThrow(String customerId) {
        return customerRepository.findById(customerId)
            .orElseThrow(() -> notFound("CUSTOMER_NOT_FOUND", "Customer not found: " + customerId));
    }

    private AppException notFound(String code, String message) {
        AppException ex = new AppException(code, message);
        ex.setHttpStatus(HttpStatus.NOT_FOUND);
        return ex;
    }

    private AppException conflict(String code, String message) {
        AppException ex = new AppException(code, message);
        ex.setHttpStatus(HttpStatus.CONFLICT);
        return ex;
    }

    private AppException badRequest(String code, String message) {
        AppException ex = new AppException(code, message);
        ex.setHttpStatus(HttpStatus.BAD_REQUEST);
        return ex;
    }
}
