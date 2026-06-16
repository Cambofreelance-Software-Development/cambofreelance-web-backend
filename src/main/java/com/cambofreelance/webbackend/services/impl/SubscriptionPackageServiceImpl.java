package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.PackageFeature;
import com.cambofreelance.webbackend.dto.request.PackageCreateRequest;
import com.cambofreelance.webbackend.dto.request.PackageFeatureToggleRequest;
import com.cambofreelance.webbackend.dto.request.PackageUpdateRequest;
import com.cambofreelance.webbackend.dto.response.PackageResponse;
import com.cambofreelance.webbackend.entities.SubscriptionPackageEntity;
import com.cambofreelance.webbackend.entities.SubscriptionPackageFeatureEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.SubscriptionPackageFeatureRepository;
import com.cambofreelance.webbackend.repository.SubscriptionPackageRepository;
import com.cambofreelance.webbackend.services.SubscriptionPackageService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SubscriptionPackageServiceImpl implements SubscriptionPackageService {

    private final SubscriptionPackageRepository packageRepository;
    private final SubscriptionPackageFeatureRepository featureRepository;

    @Override
    public Page<PackageResponse> list(String search, String status, int page, int size) {
        Specification<SubscriptionPackageEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.notEqual(root.get("status"), Constants.STATUS_DELETE));
            if (StringUtils.hasText(search)) {
                String like = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("code")), like)
                ));
            }
            if (StringUtils.hasText(status)) {
                predicates.add(cb.equal(root.get("status"), status.trim()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Sort sort = Sort.by(Sort.Order.asc("sortOrder"));
        return packageRepository.findAll(spec, PageRequest.of(page, size, sort))
            .map(e -> PackageResponse.from(e, toFeatureMap(e.getId())));
    }

    @Override
    public List<PackageResponse> listActive() {
        Specification<SubscriptionPackageEntity> spec = (root, query, cb) ->
            cb.equal(root.get("status"), Constants.STATUS_ACTIVE);
        return packageRepository.findAll(spec, Sort.by(Sort.Order.asc("sortOrder"))).stream()
            .map(e -> PackageResponse.from(e, toFeatureMap(e.getId())))
            .toList();
    }

    @Override
    public PackageResponse getById(String id) {
        SubscriptionPackageEntity entity = findOrThrow(id);
        return PackageResponse.from(entity, toFeatureMap(id));
    }

    @Override
    @Transactional
    public PackageResponse create(PackageCreateRequest request) {
        if (packageRepository.existsByCode(request.getCode())) {
            throw new AppException("PACKAGE_CODE_EXISTS", "Package code '" + request.getCode() + "' already exists");
        }

        SubscriptionPackageEntity entity = new SubscriptionPackageEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCode(request.getCode().trim().toUpperCase());
        entity.setName(request.getName());
        entity.setMonthlyPrice(request.getMonthlyPrice());
        entity.setCustomPricing(request.isCustomPricing());
        entity.setMaxCustomers(request.getMaxCustomers());
        entity.setMaxLoans(request.getMaxLoans());
        entity.setMaxUsers(request.getMaxUsers());
        entity.setDescription(request.getDescription());
        entity.setSortOrder(request.getSortOrder());
        entity.setStatus(Constants.STATUS_ACTIVE);
        entity.setCreatedAt(new Date());
        packageRepository.save(entity);

        saveFeatures(entity.getId(), request.getFeatures());
        return PackageResponse.from(entity, toFeatureMap(entity.getId()));
    }

    @Override
    @Transactional
    public PackageResponse update(String id, PackageUpdateRequest request) {
        SubscriptionPackageEntity entity = findOrThrow(id);

        if (request.getName() != null) entity.setName(request.getName());
        if (request.getMonthlyPrice() != null) entity.setMonthlyPrice(request.getMonthlyPrice());
        if (request.getCustomPricing() != null) entity.setCustomPricing(request.getCustomPricing());
        if (request.getMaxCustomers() != null) entity.setMaxCustomers(request.getMaxCustomers());
        if (request.getMaxLoans() != null) entity.setMaxLoans(request.getMaxLoans());
        if (request.getMaxUsers() != null) entity.setMaxUsers(request.getMaxUsers());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        if (request.getSortOrder() != null) entity.setSortOrder(request.getSortOrder());
        entity.setUpdatedAt(new Date());

        packageRepository.save(entity);
        return PackageResponse.from(entity, toFeatureMap(id));
    }

    @Override
    @Transactional
    public void delete(String id) {
        SubscriptionPackageEntity entity = findOrThrow(id);
        entity.setStatus(Constants.STATUS_DELETE);
        entity.setUpdatedAt(new Date());
        packageRepository.save(entity);
    }

    @Override
    @Transactional
    public PackageResponse updateFeatures(String id, PackageFeatureToggleRequest request) {
        SubscriptionPackageEntity entity = findOrThrow(id);
        saveFeatures(id, request.getFeatures());
        entity.setUpdatedAt(new Date());
        packageRepository.save(entity);
        return PackageResponse.from(entity, toFeatureMap(id));
    }

    private void saveFeatures(String packageId, Map<String, Boolean> features) {
        if (features == null) return;
        features.forEach((code, enabled) -> {
            if (!PackageFeature.isValid(code)) {
                throw new AppException("INVALID_FEATURE_CODE", "Unknown feature code: " + code);
            }
            featureRepository.save(
                new SubscriptionPackageFeatureEntity(packageId, code.toUpperCase(), Boolean.TRUE.equals(enabled)));
        });
    }

    private Map<String, Boolean> toFeatureMap(String packageId) {
        Map<String, Boolean> map = new LinkedHashMap<>();
        PackageFeature.ALL.forEach(code -> map.put(code, false));
        featureRepository.findByIdPackageId(packageId)
            .forEach(f -> map.put(f.getId().getFeatureCode(), f.isEnabled()));
        return map;
    }

    private SubscriptionPackageEntity findOrThrow(String id) {
        return packageRepository.findById(id)
            .orElseThrow(() -> new AppException("PACKAGE_NOT_FOUND", "Subscription package not found: " + id));
    }
}
