package com.cambofreelance.authenticationservice.services.impl;

import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.dto.request.ArticleTypeRequest;
import com.cambofreelance.authenticationservice.dto.response.ArticleTypeResponse;
import com.cambofreelance.authenticationservice.entities.ArticleTypeEntity;
import com.cambofreelance.authenticationservice.logger.exceptions.AppException;
import com.cambofreelance.authenticationservice.repository.ArticleTypeRepository;
import com.cambofreelance.authenticationservice.audit.Auditable;
import com.cambofreelance.authenticationservice.services.ArticleTypeService;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleTypeServiceImpl implements ArticleTypeService {

    private final ArticleTypeRepository repository;

    @Override
    public List<ArticleTypeResponse> listActive() {
        return repository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(ArticleTypeResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "ARTICLE_TYPE")
    public ArticleTypeResponse create(ArticleTypeRequest request) {
        String code = request.getCode().trim().toUpperCase();

        if (repository.existsByCodeAndStatusNot(code, Constants.STATUS_DELETE)) {
            AppException ex = new AppException("DUPLICATE_CODE",
                "Article type code '" + code + "' already exists");
            ex.setHttpStatus(HttpStatus.CONFLICT);
            throw ex;
        }

        ArticleTypeEntity entity = new ArticleTypeEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCode(code);
        entity.setLabel(request.getLabel().trim());
        entity.setLabelKh(request.getLabelKh());
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setCreatedBy(Constants.SYSTEM);

        return ArticleTypeResponse.from(repository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "ARTICLE_TYPE", entityClass = ArticleTypeEntity.class)
    public ArticleTypeResponse update(String id, ArticleTypeRequest request) {
        ArticleTypeEntity entity = repository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("NOT_FOUND", "Article type not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });

        String code = request.getCode().trim().toUpperCase();
        if (!code.equals(entity.getCode()) && repository.existsByCodeAndStatusNot(code, Constants.STATUS_DELETE)) {
            AppException ex = new AppException("DUPLICATE_CODE",
                "Article type code '" + code + "' already exists");
            ex.setHttpStatus(HttpStatus.CONFLICT);
            throw ex;
        }

        entity.setCode(code);
        entity.setLabel(request.getLabel().trim());
        entity.setLabelKh(request.getLabelKh());
        if (request.getSortOrder() != null) entity.setSortOrder(request.getSortOrder());
        entity.setUpdatedAt(new Date());

        return ArticleTypeResponse.from(repository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "ARTICLE_TYPE", entityClass = ArticleTypeEntity.class)
    public void delete(String id) {
        ArticleTypeEntity entity = repository.findById(id)
            .filter(e -> Constants.STATUS_ACTIVE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("NOT_FOUND", "Article type not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
        entity.setStatus(Constants.STATUS_DELETE);
        repository.save(entity);
    }
}
