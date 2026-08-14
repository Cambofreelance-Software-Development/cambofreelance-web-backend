package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.CategoryBusinessTypeCatalogRequest;
import com.cambofreelance.webbackend.dto.response.CategoryBusinessTypeCatalogResponse;
import com.cambofreelance.webbackend.entities.CategoryBusinessTypeCatalogEntity;
import com.cambofreelance.webbackend.entities.MediaFileEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.CategoryBusinessTypeCatalogRepository;
import com.cambofreelance.webbackend.repository.MediaRepository;
import com.cambofreelance.webbackend.services.CategoryBusinessTypeCatalogService;
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
public class CategoryBusinessTypeCatalogServiceImpl implements CategoryBusinessTypeCatalogService {

    private final CategoryBusinessTypeCatalogRepository categoryBusinessTypeCatalogRepository;
    private final MediaRepository                       mediaRepository;

    @Override
    public List<CategoryBusinessTypeCatalogResponse> listAll() {
        return categoryBusinessTypeCatalogRepository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(CategoryBusinessTypeCatalogResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryBusinessTypeCatalogResponse> search(String search, int page, int size) {
        String searchFilter = StringUtils.hasText(search)
            ? "%" + search.trim().toLowerCase() + "%"
            : null;
        return categoryBusinessTypeCatalogRepository.searchActive(searchFilter, PageRequest.of(page, size))
            .map(CategoryBusinessTypeCatalogResponse::from);
    }

    @Override
    public CategoryBusinessTypeCatalogResponse getById(String id) {
        return CategoryBusinessTypeCatalogResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "CATEGORY_BUSINESS_TYPE_CATALOG")
    public CategoryBusinessTypeCatalogResponse create(CategoryBusinessTypeCatalogRequest request, String createdBy) {
        CategoryBusinessTypeCatalogEntity entity = new CategoryBusinessTypeCatalogEntity();
        entity.setId(UUID.randomUUID().toString());
        applyRequest(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        return CategoryBusinessTypeCatalogResponse.from(categoryBusinessTypeCatalogRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "CATEGORY_BUSINESS_TYPE_CATALOG", entityClass = CategoryBusinessTypeCatalogEntity.class)
    public CategoryBusinessTypeCatalogResponse update(String id, CategoryBusinessTypeCatalogRequest request, String updatedBy) {
        CategoryBusinessTypeCatalogEntity entity = requireById(id);
        applyRequest(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : entity.getSortOrder());
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        return CategoryBusinessTypeCatalogResponse.from(categoryBusinessTypeCatalogRepository.save(entity));
    }

    private void applyRequest(CategoryBusinessTypeCatalogEntity entity, CategoryBusinessTypeCatalogRequest request) {
        entity.setName(request.getName().trim());
        entity.setNameKh(request.getNameKh());
        entity.setDescription(request.getDescription());
        entity.setDescriptionKh(request.getDescriptionKh());
        entity.setIcon(request.getIcon());
        entity.setMoreLink(request.getMoreLink());
        resolveImage(entity, request.getImageId());
    }

    private void resolveImage(CategoryBusinessTypeCatalogEntity entity, String imageId) {
        if (StringUtils.hasText(imageId)) {
            MediaFileEntity image = mediaRepository.findById(imageId)
                .filter(m -> Constants.STATUS_ACTIVE.equals(m.getStatus()))
                .orElse(null);
            entity.setImage(image);
        } else {
            entity.setImage(null);
        }
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "CATEGORY_BUSINESS_TYPE_CATALOG", entityClass = CategoryBusinessTypeCatalogEntity.class)
    public void delete(String id) {
        CategoryBusinessTypeCatalogEntity entity = requireById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        categoryBusinessTypeCatalogRepository.save(entity);
    }

    private CategoryBusinessTypeCatalogEntity requireById(String id) {
        return categoryBusinessTypeCatalogRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("CATEGORY_BUSINESS_TYPE_CATALOG_NOT_FOUND", "Category not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
