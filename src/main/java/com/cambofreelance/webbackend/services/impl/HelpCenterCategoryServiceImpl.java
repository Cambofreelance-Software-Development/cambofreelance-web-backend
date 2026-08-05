package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.HelpCenterCategoryRequest;
import com.cambofreelance.webbackend.dto.response.HelpCenterCategoryResponse;
import com.cambofreelance.webbackend.entities.HelpCenterCategoryEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.ArticleTypeRepository;
import com.cambofreelance.webbackend.repository.HelpCenterCategoryRepository;
import com.cambofreelance.webbackend.services.HelpCenterCategoryService;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class HelpCenterCategoryServiceImpl implements HelpCenterCategoryService {

    private final HelpCenterCategoryRepository repository;
    private final ArticleTypeRepository articleTypeRepository;

    @Override
    public List<HelpCenterCategoryResponse> listActive(String articleTypeId) {
        List<HelpCenterCategoryEntity> entities = StringUtils.hasText(articleTypeId)
            ? repository.findByArticleTypeIdAndStatusOrderByDisplayOrderAsc(articleTypeId, Constants.STATUS_ACTIVE)
            : repository.findByStatusOrderByDisplayOrderAsc(Constants.STATUS_ACTIVE);
        return entities.stream().map(HelpCenterCategoryResponse::from).collect(Collectors.toList());
    }

    @Override
    public List<HelpCenterCategoryResponse> listActiveByArticleTypeCode(String articleTypeCode) {
        if (!StringUtils.hasText(articleTypeCode)) {
            return listActive(null);
        }
        return articleTypeRepository.findByCodeAndStatusNot(articleTypeCode.toUpperCase(), Constants.STATUS_DELETE)
            .map(type -> listActive(type.getId()))
            .orElse(Collections.emptyList());
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "HELP_CENTER_CATEGORY")
    public HelpCenterCategoryResponse create(HelpCenterCategoryRequest request) {
        String slug = ensureUniqueSlug(resolveSlug(request), null);

        HelpCenterCategoryEntity entity = new HelpCenterCategoryEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setArticleTypeId(request.getArticleTypeId());
        entity.setParentId(StringUtils.hasText(request.getParentId()) ? request.getParentId() : null);
        entity.setName(request.getName().trim());
        entity.setNameKh(request.getNameKh());
        entity.setSlug(slug);
        entity.setDescription(request.getDescription());
        entity.setDescriptionKh(request.getDescriptionKh());
        entity.setIcon(request.getIcon());
        entity.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        entity.setCreatedBy(Constants.SYSTEM);

        validateNotOwnAncestor(entity.getId(), entity.getParentId());

        return HelpCenterCategoryResponse.from(repository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "HELP_CENTER_CATEGORY", entityClass = HelpCenterCategoryEntity.class)
    public HelpCenterCategoryResponse update(String id, HelpCenterCategoryRequest request) {
        HelpCenterCategoryEntity entity = repository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> notFound(id));

        String newParentId = StringUtils.hasText(request.getParentId()) ? request.getParentId() : null;
        validateNotOwnAncestor(id, newParentId);

        String requestedSlug = resolveSlug(request);
        if (!requestedSlug.equals(entity.getSlug())) {
            entity.setSlug(ensureUniqueSlug(requestedSlug, id));
        }

        entity.setArticleTypeId(request.getArticleTypeId());
        entity.setParentId(newParentId);
        entity.setName(request.getName().trim());
        entity.setNameKh(request.getNameKh());
        entity.setDescription(request.getDescription());
        entity.setDescriptionKh(request.getDescriptionKh());
        entity.setIcon(request.getIcon());
        if (request.getDisplayOrder() != null) entity.setDisplayOrder(request.getDisplayOrder());
        entity.setUpdatedAt(new Date());

        return HelpCenterCategoryResponse.from(repository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "HELP_CENTER_CATEGORY", entityClass = HelpCenterCategoryEntity.class)
    public void delete(String id) {
        HelpCenterCategoryEntity entity = repository.findById(id)
            .filter(e -> Constants.STATUS_ACTIVE.equals(e.getStatus()))
            .orElseThrow(() -> notFound(id));

        if (repository.existsByParentIdAndStatusNot(id, Constants.STATUS_DELETE)) {
            AppException ex = new AppException("HAS_SUBCATEGORIES",
                "Cannot delete a category that still has subcategories. Move or delete them first.");
            ex.setHttpStatus(HttpStatus.CONFLICT);
            throw ex;
        }

        entity.setStatus(Constants.STATUS_DELETE);
        repository.save(entity);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private AppException notFound(String id) {
        AppException ex = new AppException("NOT_FOUND", "Help center category not found: " + id);
        ex.setHttpStatus(HttpStatus.NOT_FOUND);
        return ex;
    }

    private String resolveSlug(HelpCenterCategoryRequest request) {
        String base = StringUtils.hasText(request.getSlug()) ? request.getSlug() : request.getName();
        return slugify(base);
    }

    private String slugify(String text) {
        return text.toLowerCase()
            .replaceAll("[^a-z0-9\\s/-]", "")
            .trim()
            .replaceAll("\\s+", "-")
            .replaceAll("-+", "-");
    }

    private String ensureUniqueSlug(String base, String excludeId) {
        String slug = base;
        int counter = 1;
        while (excludeId == null
            ? repository.existsBySlugAndStatusNot(slug, Constants.STATUS_DELETE)
            : repository.existsBySlugAndIdNotAndStatusNot(slug, excludeId, Constants.STATUS_DELETE)) {
            slug = base + "-" + counter++;
        }
        return slug;
    }

    /** Walks up the parent chain to reject cycles (a category becoming its own ancestor). */
    private void validateNotOwnAncestor(String id, String parentId) {
        if (parentId == null) return;
        if (parentId.equals(id)) {
            throw invalidParent();
        }
        Set<String> visited = new HashSet<>();
        String current = parentId;
        while (current != null) {
            if (current.equals(id) || !visited.add(current)) {
                throw invalidParent();
            }
            current = repository.findById(current).map(HelpCenterCategoryEntity::getParentId).orElse(null);
        }
    }

    private AppException invalidParent() {
        AppException ex = new AppException("INVALID_PARENT", "A category cannot be nested under itself or its own descendant.");
        ex.setHttpStatus(HttpStatus.BAD_REQUEST);
        return ex;
    }
}
