package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.CategoryFeatureRequest;
import com.cambofreelance.webbackend.dto.response.CategoryFeatureResponse;
import com.cambofreelance.webbackend.entities.CategoryFeatureEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.CategoryFeatureRepository;
import com.cambofreelance.webbackend.services.CategoryFeatureService;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CategoryFeatureServiceImpl implements CategoryFeatureService {

    private final CategoryFeatureRepository categoryFeatureRepository;

    @Override
    public List<CategoryFeatureResponse> listAll() {
        return categoryFeatureRepository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(CategoryFeatureResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryFeatureResponse> search(String search, int page, int size) {
        String searchFilter = StringUtils.hasText(search)
            ? "%" + search.trim().toLowerCase() + "%"
            : null;
        return categoryFeatureRepository.searchActive(searchFilter, PageRequest.of(page, size))
            .map(CategoryFeatureResponse::from);
    }

    @Override
    public CategoryFeatureResponse getById(String id) {
        return CategoryFeatureResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "CATEGORY_FEATURE")
    public CategoryFeatureResponse create(CategoryFeatureRequest request, String createdBy) {
        CategoryFeatureEntity entity = new CategoryFeatureEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setName(request.getName().trim());
        entity.setNameKh(request.getNameKh());
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        return CategoryFeatureResponse.from(categoryFeatureRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "CATEGORY_FEATURE", entityClass = CategoryFeatureEntity.class)
    public CategoryFeatureResponse update(String id, CategoryFeatureRequest request, String updatedBy) {
        CategoryFeatureEntity entity = requireById(id);
        entity.setName(request.getName().trim());
        entity.setNameKh(request.getNameKh());
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : entity.getSortOrder());
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        return CategoryFeatureResponse.from(categoryFeatureRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "CATEGORY_FEATURE", entityClass = CategoryFeatureEntity.class)
    public void delete(String id) {
        CategoryFeatureEntity entity = requireById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        categoryFeatureRepository.save(entity);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private CategoryFeatureEntity requireById(String id) {
        return categoryFeatureRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("CATEGORY_FEATURE_NOT_FOUND", "Category not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
