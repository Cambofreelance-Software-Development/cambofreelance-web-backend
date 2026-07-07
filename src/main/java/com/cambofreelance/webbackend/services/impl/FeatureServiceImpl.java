package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.FeatureRequest;
import com.cambofreelance.webbackend.dto.response.FeatureResponse;
import com.cambofreelance.webbackend.entities.CategoryFeatureEntity;
import com.cambofreelance.webbackend.entities.FeatureEntity;
import com.cambofreelance.webbackend.entities.MediaFileEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.CategoryFeatureRepository;
import com.cambofreelance.webbackend.repository.FeatureRepository;
import com.cambofreelance.webbackend.repository.MediaRepository;
import com.cambofreelance.webbackend.services.FeatureService;
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
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository         featureRepository;
    private final MediaRepository           mediaRepository;
    private final CategoryFeatureRepository categoryFeatureRepository;

    @Override
    public List<FeatureResponse> listAll() {
        return featureRepository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(FeatureResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public Page<FeatureResponse> search(String search, int page, int size) {
        String searchFilter = StringUtils.hasText(search)
            ? "%" + search.trim().toLowerCase() + "%"
            : null;
        return featureRepository.searchActive(searchFilter, PageRequest.of(page, size))
            .map(FeatureResponse::from);
    }

    @Override
    public FeatureResponse getById(String id) {
        return FeatureResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "FEATURE")
    public FeatureResponse create(FeatureRequest request, String createdBy) {
        FeatureEntity entity = new FeatureEntity();
        entity.setId(UUID.randomUUID().toString());
        applyRequest(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        return FeatureResponse.from(featureRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "FEATURE", entityClass = FeatureEntity.class)
    public FeatureResponse update(String id, FeatureRequest request, String updatedBy) {
        FeatureEntity entity = requireById(id);
        applyRequest(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : entity.getSortOrder());
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        return FeatureResponse.from(featureRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "FEATURE", entityClass = FeatureEntity.class)
    public void delete(String id) {
        FeatureEntity entity = requireById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        featureRepository.save(entity);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void applyRequest(FeatureEntity entity, FeatureRequest request) {
        entity.setTitle(request.getTitle().trim());
        entity.setTitleKh(request.getTitleKh());
        entity.setDescription(request.getDescription());
        entity.setDescriptionKh(request.getDescriptionKh());
        entity.setIcon(request.getIcon());
        entity.setLink(request.getLink());
        resolveImage(entity, request.getImageId());
        resolveCategory(entity, request.getCategoryId());
    }

    private void resolveCategory(FeatureEntity entity, String categoryId) {
        if (StringUtils.hasText(categoryId)) {
            CategoryFeatureEntity category = categoryFeatureRepository.findById(categoryId)
                .filter(c -> !Constants.STATUS_DELETE.equals(c.getStatus()))
                .orElse(null);
            entity.setCategory(category);
        } else {
            entity.setCategory(null);
        }
    }

    private FeatureEntity requireById(String id) {
        return featureRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("FEATURE_NOT_FOUND", "Feature not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }

    private void resolveImage(FeatureEntity entity, String imageId) {
        if (StringUtils.hasText(imageId)) {
            MediaFileEntity image = mediaRepository.findById(imageId)
                .filter(m -> Constants.STATUS_ACTIVE.equals(m.getStatus()))
                .orElse(null);
            entity.setImage(image);
        } else {
            entity.setImage(null);
        }
    }
}
