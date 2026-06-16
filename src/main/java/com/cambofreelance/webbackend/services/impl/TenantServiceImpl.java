package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.TenantCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantUpdateRequest;
import com.cambofreelance.webbackend.dto.response.TenantResponse;
import com.cambofreelance.webbackend.entities.TenantEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.TenantRepository;
import com.cambofreelance.webbackend.services.TenantService;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    @Override
    public Page<TenantResponse> list(String search, String status, String tenantType, int page, int size) {
        Specification<TenantEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.notEqual(root.get("status"), Constants.STATUS_DELETE));

            if (search != null && !search.isBlank()) {
                String like = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("code")), like),
                    cb.like(cb.lower(root.get("companyName")), like)
                ));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status.trim()));
            }
            if (tenantType != null && !tenantType.isBlank()) {
                predicates.add(cb.equal(root.get("tenantType"), tenantType.trim()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Sort sort = Sort.by(Sort.Order.asc("name"));
        return tenantRepository.findAll(spec, PageRequest.of(page, size, sort)).map(TenantResponse::from);
    }

    @Override
    public TenantResponse getById(String id) {
        return TenantResponse.from(findOrThrow(id));
    }

    @Override
    @Transactional
    public TenantResponse create(TenantCreateRequest request) {
        if (tenantRepository.existsByCode(request.getCode())) {
            throw new AppException("TENANT_CODE_EXISTS", "Tenant code '" + request.getCode() + "' already exists");
        }

        TenantEntity entity = new TenantEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCode(request.getCode().trim().toUpperCase());
        entity.setName(request.getName());
        entity.setTenantType(request.getTenantType());
        entity.setDescription(request.getDescription());
        entity.setLogoUrl(request.getLogoUrl());
        entity.setPrimaryColor(request.getPrimaryColor());
        entity.setSecondaryColor(request.getSecondaryColor());
        entity.setCompanyName(request.getCompanyName());
        entity.setCompanyAddress(request.getCompanyAddress());
        entity.setCompanyEmail(request.getCompanyEmail());
        entity.setCompanyPhone(request.getCompanyPhone());
        entity.setWebsite(request.getWebsite());
        entity.setPlanStartDate(request.getPlanStartDate());
        entity.setPlanExpiredDate(request.getPlanExpiredDate());
        entity.setStatus(Constants.STATUS_ACTIVE);
        entity.setCreatedAt(new Date());

        return TenantResponse.from(tenantRepository.save(entity));
    }

    @Override
    @Transactional
    public TenantResponse update(String id, TenantUpdateRequest request) {
        TenantEntity entity = findOrThrow(id);

        if (request.getName() != null) entity.setName(request.getName());
        if (request.getTenantType() != null) entity.setTenantType(request.getTenantType());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getLogoUrl() != null) entity.setLogoUrl(request.getLogoUrl());
        if (request.getPrimaryColor() != null) entity.setPrimaryColor(request.getPrimaryColor());
        if (request.getSecondaryColor() != null) entity.setSecondaryColor(request.getSecondaryColor());
        if (request.getCompanyName() != null) entity.setCompanyName(request.getCompanyName());
        if (request.getCompanyAddress() != null) entity.setCompanyAddress(request.getCompanyAddress());
        if (request.getCompanyEmail() != null) entity.setCompanyEmail(request.getCompanyEmail());
        if (request.getCompanyPhone() != null) entity.setCompanyPhone(request.getCompanyPhone());
        if (request.getWebsite() != null) entity.setWebsite(request.getWebsite());
        if (request.getPlanStartDate() != null) entity.setPlanStartDate(request.getPlanStartDate());
        if (request.getPlanExpiredDate() != null) entity.setPlanExpiredDate(request.getPlanExpiredDate());
        entity.setUpdatedAt(new Date());

        return TenantResponse.from(tenantRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(String id) {
        TenantEntity entity = findOrThrow(id);
        entity.setStatus(Constants.STATUS_DELETE);
        entity.setUpdatedAt(new Date());
        tenantRepository.save(entity);
    }

    @Override
    @Transactional
    public TenantResponse updateStatus(String id, String status) {
        TenantEntity entity = findOrThrow(id);
        entity.setStatus(status);
        entity.setUpdatedAt(new Date());
        return TenantResponse.from(tenantRepository.save(entity));
    }

    private TenantEntity findOrThrow(String id) {
        return tenantRepository.findById(id)
            .orElseThrow(() -> new AppException("TENANT_NOT_FOUND", "Tenant not found: " + id));
    }
}
