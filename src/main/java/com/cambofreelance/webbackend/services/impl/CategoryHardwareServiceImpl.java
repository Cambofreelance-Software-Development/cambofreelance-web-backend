package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.CategoryHardwareRequest;
import com.cambofreelance.webbackend.dto.response.CategoryHardwareResponse;
import com.cambofreelance.webbackend.entities.CategoryHardwareEntity;
import com.cambofreelance.webbackend.entities.MediaFileEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.CategoryHardwareRepository;
import com.cambofreelance.webbackend.repository.MediaRepository;
import com.cambofreelance.webbackend.services.CategoryHardwareService;
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
public class CategoryHardwareServiceImpl implements CategoryHardwareService {

    private final CategoryHardwareRepository categoryHardwareRepository;
    private final MediaRepository            mediaRepository;

    @Override
    public List<CategoryHardwareResponse> listAll() {
        return categoryHardwareRepository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(CategoryHardwareResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryHardwareResponse> search(String search, int page, int size) {
        String searchFilter = StringUtils.hasText(search)
            ? "%" + search.trim().toLowerCase() + "%"
            : null;
        return categoryHardwareRepository.searchActive(searchFilter, PageRequest.of(page, size))
            .map(CategoryHardwareResponse::from);
    }

    @Override
    public CategoryHardwareResponse getById(String id) {
        return CategoryHardwareResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "CATEGORY_HARDWARE")
    public CategoryHardwareResponse create(CategoryHardwareRequest request, String createdBy) {
        CategoryHardwareEntity entity = new CategoryHardwareEntity();
        entity.setId(UUID.randomUUID().toString());
        applyRequest(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        return CategoryHardwareResponse.from(categoryHardwareRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "CATEGORY_HARDWARE", entityClass = CategoryHardwareEntity.class)
    public CategoryHardwareResponse update(String id, CategoryHardwareRequest request, String updatedBy) {
        CategoryHardwareEntity entity = requireById(id);
        applyRequest(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : entity.getSortOrder());
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        return CategoryHardwareResponse.from(categoryHardwareRepository.save(entity));
    }

    private void applyRequest(CategoryHardwareEntity entity, CategoryHardwareRequest request) {
        entity.setName(request.getName().trim());
        entity.setNameKh(request.getNameKh());
        entity.setDescription(request.getDescription());
        entity.setDescriptionKh(request.getDescriptionKh());
        entity.setIcon(request.getIcon());
        entity.setMoreLink(request.getMoreLink());
        resolveImage(entity, request.getImageId());
    }

    private void resolveImage(CategoryHardwareEntity entity, String imageId) {
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
    @Auditable(action = "DELETE", module = "CATEGORY_HARDWARE", entityClass = CategoryHardwareEntity.class)
    public void delete(String id) {
        CategoryHardwareEntity entity = requireById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        categoryHardwareRepository.save(entity);
    }

    private CategoryHardwareEntity requireById(String id) {
        return categoryHardwareRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("CATEGORY_HARDWARE_NOT_FOUND", "Category not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
