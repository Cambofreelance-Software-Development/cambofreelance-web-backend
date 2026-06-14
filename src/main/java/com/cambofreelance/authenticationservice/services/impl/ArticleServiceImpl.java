package com.cambofreelance.authenticationservice.services.impl;

import com.cambofreelance.authenticationservice.constants.ArticleWorkflowStatus;
import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.dto.request.ArticleCreateRequest;
import com.cambofreelance.authenticationservice.dto.request.ArticleUpdateRequest;
import com.cambofreelance.authenticationservice.dto.response.ArticleResponse;
import com.cambofreelance.authenticationservice.entities.ArticleEntity;
import com.cambofreelance.authenticationservice.entities.MediaFileEntity;
import com.cambofreelance.authenticationservice.logger.exceptions.AppException;
import com.cambofreelance.authenticationservice.repository.ArticleRepository;
import com.cambofreelance.authenticationservice.repository.MediaRepository;
import com.cambofreelance.authenticationservice.audit.Auditable;
import com.cambofreelance.authenticationservice.services.ArticleService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final MediaRepository   mediaRepository;

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "ARTICLE")
    public ArticleResponse create(ArticleCreateRequest request, String createdBy) {

        String slug = ensureUniqueSlug(generateSlug(request.getTitle()), null);

        ArticleEntity entity = new ArticleEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setTitle(request.getTitle());
        entity.setSlug(slug);
        entity.setContent(request.getContent());
        entity.setExcerpt(request.getExcerpt());
        entity.setTitleKh(request.getTitleKh());
        entity.setContentKh(request.getContentKh());
        entity.setExcerptKh(request.getExcerptKh());
        entity.setType(request.getType().toUpperCase());
        entity.setAuthorId(StringUtils.hasText(request.getAuthorId()) ? request.getAuthorId() : createdBy);
        entity.setAuthorName(request.getAuthorName());
        entity.setTags(tagsToString(request.getTags()));
        entity.setPublishedAt(request.getPublishedAt());
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setViewCount(0);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        entity.setWorkflowStatus(ArticleWorkflowStatus.DRAFT);

        resolveFeaturedImage(entity, request.getFeaturedImageId());
        resolveAttachments(entity, request.getAttachmentIds());

        return ArticleResponse.from(articleRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "ARTICLE", entityClass = ArticleEntity.class)
    public ArticleResponse update(String id, ArticleUpdateRequest request, String updatedBy) {

        ArticleEntity entity = requireById(id);

        if (!entity.getTitle().equals(request.getTitle())) {
            entity.setSlug(ensureUniqueSlug(generateSlug(request.getTitle()), id));
        }

        entity.setTitle(request.getTitle());
        entity.setContent(request.getContent());
        entity.setExcerpt(request.getExcerpt());
        entity.setTitleKh(request.getTitleKh());
        entity.setContentKh(request.getContentKh());
        entity.setExcerptKh(request.getExcerptKh());
        entity.setType(request.getType().toUpperCase());
        entity.setAuthorId(request.getAuthorId());
        entity.setAuthorName(request.getAuthorName());
        entity.setTags(tagsToString(request.getTags()));
        entity.setPublishedAt(request.getPublishedAt());
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());

        resolveFeaturedImage(entity, request.getFeaturedImageId());
        resolveAttachments(entity, request.getAttachmentIds());

        return ArticleResponse.from(articleRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "ARTICLE", entityClass = ArticleEntity.class)
    public void delete(String id) {
        ArticleEntity entity = requireById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        articleRepository.save(entity);
    }

    @Override
    @Transactional
    @Auditable(action = "STATUS_CHANGE", module = "ARTICLE", entityClass = ArticleEntity.class)
    public ArticleResponse updateStatus(String id, String status, String updatedBy) {
        ArticleEntity entity = requireById(id);

        String target = status.toUpperCase();

        if (!ArticleWorkflowStatus.isValid(target)) {
            throw new AppException("INVALID_WORKFLOW_STATUS",
                "Invalid workflow status '" + status + "'. Allowed: DRAFT, REVIEW, APPROVAL, PUBLISHED, ARCHIVED");
        }

        String current = entity.getWorkflowStatus();
        if (!ArticleWorkflowStatus.canTransition(current, target)) {
            throw new AppException("INVALID_WORKFLOW_TRANSITION",
                "Cannot transition article from " + current + " to " + target);
        }

        entity.setWorkflowStatus(target);
        if (ArticleWorkflowStatus.PUBLISHED.equals(target) && entity.getPublishedAt() == null) {
            entity.setPublishedAt(new Date());
        }
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        return ArticleResponse.from(articleRepository.save(entity));
    }

    @Override
    @Transactional
    public Page<ArticleResponse> list(String type, String workflowStatus, String authorId, String search, int page, int size) {
        String typeFilter           = StringUtils.hasText(type)           ? type.toUpperCase()                       : null;
        String workflowStatusFilter = StringUtils.hasText(workflowStatus) ? workflowStatus.toUpperCase()             : null;
        String authorIdFilter       = StringUtils.hasText(authorId)       ? authorId.trim()                          : null;
        String searchFilter         = StringUtils.hasText(search)         ? "%" + search.trim().toLowerCase() + "%" : null;

        return articleRepository
            .findFiltered(typeFilter, workflowStatusFilter, authorIdFilter, searchFilter, PageRequest.of(page, size))
            .map(ArticleResponse::from);
    }

    @Override
    @Transactional
    public ArticleResponse getById(String id) {
        return ArticleResponse.from(requireById(id));
    }

    @Override
    @Transactional
    public ArticleResponse getBySlug(String slug) {
        return articleRepository.findBySlugAndWorkflowStatusAndStatusNot(
                slug, ArticleWorkflowStatus.PUBLISHED, Constants.STATUS_DELETE)
            .map(ArticleResponse::from)
            .orElseThrow(() -> notFound("slug", slug));
    }

    @Override
    @Transactional
    @Auditable(action = "COPY", module = "ARTICLE")
    public ArticleResponse copy(String id, String createdBy) {
        ArticleEntity source = requireById(id);

        String baseSlug = source.getSlug() + "-copy";
        String slug = ensureUniqueSlug(baseSlug, null);

        ArticleEntity copy = new ArticleEntity();
        copy.setId(UUID.randomUUID().toString());
        copy.setTitle(source.getTitle() + " (Copy)");
        copy.setSlug(slug);
        copy.setContent(source.getContent());
        copy.setExcerpt(source.getExcerpt());
        copy.setTitleKh(source.getTitleKh());
        copy.setContentKh(source.getContentKh());
        copy.setExcerptKh(source.getExcerptKh());
        copy.setType(source.getType());
        copy.setFeaturedImage(source.getFeaturedImage());
        copy.setAttachments(new ArrayList<>(source.getAttachments()));
        copy.setAuthorId(source.getAuthorId());
        copy.setAuthorName(source.getAuthorName());
        copy.setTags(source.getTags());
        copy.setSortOrder(source.getSortOrder());
        copy.setViewCount(0);
        copy.setPublishedAt(null);
        copy.setWorkflowStatus(ArticleWorkflowStatus.DRAFT);
        copy.setStatus(Constants.STATUS_ACTIVE);
        copy.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);

        return ArticleResponse.from(articleRepository.save(copy));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private ArticleEntity requireById(String id) {
        return articleRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> notFound("id", id));
    }

    private AppException notFound(String field, String value) {
        AppException ex = new AppException("ARTICLE_NOT_FOUND", "Article not found: " + field + "=" + value);
        ex.setHttpStatus(HttpStatus.NOT_FOUND);
        return ex;
    }

    private void resolveFeaturedImage(ArticleEntity entity, String featuredImageId) {
        if (StringUtils.hasText(featuredImageId)) {
            MediaFileEntity img = mediaRepository.findById(featuredImageId)
                .filter(m -> Constants.STATUS_ACTIVE.equals(m.getStatus()))
                .orElse(null);
            entity.setFeaturedImage(img);
        } else {
            entity.setFeaturedImage(null);
        }
    }

    private void resolveAttachments(ArticleEntity entity, List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            entity.setAttachments(Collections.emptyList());
            return;
        }
        entity.setAttachments(mediaRepository.findAllById(ids));
    }

    private String generateSlug(String title) {
        return title.toLowerCase()
            .replaceAll("[^a-z0-9\\s-]", "")
            .trim()
            .replaceAll("\\s+", "-")
            .replaceAll("-+", "-");
    }

    private String ensureUniqueSlug(String base, String excludeId) {
        String slug = base;
        int counter = 1;
        while (excludeId == null
            ? articleRepository.existsBySlug(slug)
            : articleRepository.existsBySlugAndIdNot(slug, excludeId)) {
            slug = base + "-" + counter++;
        }
        return slug;
    }

    private String tagsToString(List<String> tags) {
        if (tags == null || tags.isEmpty()) return null;
        return String.join(",", tags);
    }
}
