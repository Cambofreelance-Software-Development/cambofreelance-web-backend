package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.BusinessTypeCatalogRequest;
import com.cambofreelance.webbackend.dto.response.BusinessTypeCatalogResponse;
import com.cambofreelance.webbackend.entities.BusinessTypeCatalogEntity;
import com.cambofreelance.webbackend.entities.CategoryBusinessTypeCatalogEntity;
import com.cambofreelance.webbackend.entities.MediaFileEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.BusinessTypeCatalogRepository;
import com.cambofreelance.webbackend.repository.CategoryBusinessTypeCatalogRepository;
import com.cambofreelance.webbackend.repository.MediaRepository;
import com.cambofreelance.webbackend.services.BusinessTypeCatalogService;
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
public class BusinessTypeCatalogServiceImpl implements BusinessTypeCatalogService {

    private final BusinessTypeCatalogRepository         businessTypeCatalogRepository;
    private final MediaRepository                       mediaRepository;
    private final CategoryBusinessTypeCatalogRepository categoryBusinessTypeCatalogRepository;

    @Override
    public List<BusinessTypeCatalogResponse> listAll() {
        return businessTypeCatalogRepository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(BusinessTypeCatalogResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public List<BusinessTypeCatalogResponse> listPublic(String categoryId) {
        List<BusinessTypeCatalogEntity> entities = StringUtils.hasText(categoryId)
            ? businessTypeCatalogRepository.findByStatusAndCategory_IdOrderBySortOrderAsc(Constants.STATUS_ACTIVE, categoryId)
            : businessTypeCatalogRepository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE);
        return entities.stream()
            .map(BusinessTypeCatalogResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public Page<BusinessTypeCatalogResponse> search(String search, int page, int size) {
        String searchFilter = StringUtils.hasText(search)
            ? "%" + search.trim().toLowerCase() + "%"
            : null;
        return businessTypeCatalogRepository.searchActive(searchFilter, PageRequest.of(page, size))
            .map(BusinessTypeCatalogResponse::from);
    }

    @Override
    public BusinessTypeCatalogResponse getById(String id) {
        return BusinessTypeCatalogResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "BUSINESS_TYPE_CATALOG")
    public BusinessTypeCatalogResponse create(BusinessTypeCatalogRequest request, String createdBy) {
        BusinessTypeCatalogEntity entity = new BusinessTypeCatalogEntity();
        entity.setId(UUID.randomUUID().toString());
        applyRequest(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        return BusinessTypeCatalogResponse.from(businessTypeCatalogRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "BUSINESS_TYPE_CATALOG", entityClass = BusinessTypeCatalogEntity.class)
    public BusinessTypeCatalogResponse update(String id, BusinessTypeCatalogRequest request, String updatedBy) {
        BusinessTypeCatalogEntity entity = requireById(id);
        applyRequest(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : entity.getSortOrder());
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        return BusinessTypeCatalogResponse.from(businessTypeCatalogRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "BUSINESS_TYPE_CATALOG", entityClass = BusinessTypeCatalogEntity.class)
    public void delete(String id) {
        BusinessTypeCatalogEntity entity = requireById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        businessTypeCatalogRepository.save(entity);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void applyRequest(BusinessTypeCatalogEntity entity, BusinessTypeCatalogRequest request) {
        entity.setName(request.getName().trim());
        entity.setNameKh(request.getNameKh());
        entity.setDescription(request.getDescription());
        entity.setDescriptionKh(request.getDescriptionKh());
        entity.setPrice(request.getPrice());
        entity.setIcon(request.getIcon());
        entity.setLink(request.getLink());
        resolveImage(entity, request.getImageId());
        resolveCategory(entity, request.getCategoryId());
    }

    private void resolveCategory(BusinessTypeCatalogEntity entity, String categoryId) {
        if (StringUtils.hasText(categoryId)) {
            CategoryBusinessTypeCatalogEntity category = categoryBusinessTypeCatalogRepository.findById(categoryId)
                .filter(c -> !Constants.STATUS_DELETE.equals(c.getStatus()))
                .orElse(null);
            entity.setCategory(category);
        } else {
            entity.setCategory(null);
        }
    }

    private void resolveImage(BusinessTypeCatalogEntity entity, String imageId) {
        if (StringUtils.hasText(imageId)) {
            MediaFileEntity image = mediaRepository.findById(imageId)
                .filter(m -> Constants.STATUS_ACTIVE.equals(m.getStatus()))
                .orElse(null);
            entity.setImage(image);
        } else {
            entity.setImage(null);
        }
    }

    private BusinessTypeCatalogEntity requireById(String id) {
        return businessTypeCatalogRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("BUSINESS_TYPE_CATALOG_NOT_FOUND", "Business-Type not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
