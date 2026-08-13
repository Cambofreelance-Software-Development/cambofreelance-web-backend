package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.CategoryProductRequest;
import com.cambofreelance.webbackend.dto.response.CategoryProductResponse;
import com.cambofreelance.webbackend.entities.CategoryProductEntity;
import com.cambofreelance.webbackend.entities.MediaFileEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.CategoryProductRepository;
import com.cambofreelance.webbackend.repository.MediaRepository;
import com.cambofreelance.webbackend.services.CategoryProductService;
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
public class CategoryProductServiceImpl implements CategoryProductService {

    private final CategoryProductRepository categoryProductRepository;
    private final MediaRepository           mediaRepository;

    @Override
    public List<CategoryProductResponse> listAll() {
        return categoryProductRepository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(CategoryProductResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryProductResponse> search(String search, int page, int size) {
        String searchFilter = StringUtils.hasText(search)
            ? "%" + search.trim().toLowerCase() + "%"
            : null;
        return categoryProductRepository.searchActive(searchFilter, PageRequest.of(page, size))
            .map(CategoryProductResponse::from);
    }

    @Override
    public CategoryProductResponse getById(String id) {
        return CategoryProductResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "CATEGORY_PRODUCT")
    public CategoryProductResponse create(CategoryProductRequest request, String createdBy) {
        CategoryProductEntity entity = new CategoryProductEntity();
        entity.setId(UUID.randomUUID().toString());
        applyRequest(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        return CategoryProductResponse.from(categoryProductRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "CATEGORY_PRODUCT", entityClass = CategoryProductEntity.class)
    public CategoryProductResponse update(String id, CategoryProductRequest request, String updatedBy) {
        CategoryProductEntity entity = requireById(id);
        applyRequest(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : entity.getSortOrder());
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        return CategoryProductResponse.from(categoryProductRepository.save(entity));
    }

    private void applyRequest(CategoryProductEntity entity, CategoryProductRequest request) {
        entity.setName(request.getName().trim());
        entity.setNameKh(request.getNameKh());
        entity.setDescription(request.getDescription());
        entity.setDescriptionKh(request.getDescriptionKh());
        entity.setIcon(request.getIcon());
        entity.setMoreLink(request.getMoreLink());
        resolveImage(entity, request.getImageId());
    }

    private void resolveImage(CategoryProductEntity entity, String imageId) {
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
    @Auditable(action = "DELETE", module = "CATEGORY_PRODUCT", entityClass = CategoryProductEntity.class)
    public void delete(String id) {
        CategoryProductEntity entity = requireById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        categoryProductRepository.save(entity);
    }

    private CategoryProductEntity requireById(String id) {
        return categoryProductRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("CATEGORY_PRODUCT_NOT_FOUND", "Category not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
