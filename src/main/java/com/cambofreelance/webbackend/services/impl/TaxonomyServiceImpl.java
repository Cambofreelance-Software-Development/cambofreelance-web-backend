package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.request.TaxonomyDeleteRequest;
import com.cambofreelance.webbackend.dto.request.TaxonomyRequest;
import com.cambofreelance.webbackend.dto.response.TaxonomyResponse;
import com.cambofreelance.webbackend.dto.taxonomy.response.PaginateResponse;
import com.cambofreelance.webbackend.entities.TaxonomyEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.TaxonomyRepository;
import com.cambofreelance.webbackend.services.TaxonomyService;
import com.cambofreelance.webbackend.utils.PaginationUtils;
import com.cambofreelance.webbackend.utils.SpecificationBuilder;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxonomyServiceImpl implements TaxonomyService {

    private static final List<String> SEARCH_FIELDS = List.of("code", "name");

    private final TaxonomyRepository repository;

    @Override
    public PaginateResponse<TaxonomyResponse> list(BaseRequest request) {
        Specification<TaxonomyEntity> spec =
            SpecificationBuilder.build(request.getFilter(), request.getSearch(), SEARCH_FIELDS);
        Page<TaxonomyEntity> page = repository.findAll(spec, PaginationUtils.toPageable(request, "createdAt"));

        PaginateResponse<TaxonomyResponse> result = new PaginateResponse<>();
        result.setContent(page.getContent().stream().map(TaxonomyResponse::from).collect(Collectors.toList()));
        result.setMetadata(PaginationUtils.from(page));
        return result;
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "TAXONOMY")
    public TaxonomyResponse create(TaxonomyRequest request) {
        String code = request.getCode().trim().toUpperCase();

        if (repository.existsByCodeAndStatusNot(code, Constants.STATUS_DELETE)) {
            AppException ex = new AppException("TAXONOMY_CODE_EXISTS",
                "Taxonomy code '" + code + "' already exists");
            ex.setHttpStatus(HttpStatus.CONFLICT);
            throw ex;
        }

        TaxonomyEntity entity = new TaxonomyEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCode(code);
        entity.setCreatedBy(Constants.SYSTEM);
        applyRequest(entity, request);

        return TaxonomyResponse.from(repository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "TAXONOMY")
    public TaxonomyResponse update(TaxonomyRequest request) {
        TaxonomyEntity entity = requireByCode(request.getCode());
        applyRequest(entity, request);
        entity.setUpdatedAt(new Date());

        return TaxonomyResponse.from(repository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "TAXONOMY")
    public void delete(TaxonomyDeleteRequest request) {
        TaxonomyEntity entity = requireByCode(request.getCode());
        entity.setStatus(Constants.STATUS_DELETE);
        entity.setUpdatedAt(new Date());
        repository.save(entity);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void applyRequest(TaxonomyEntity entity, TaxonomyRequest request) {
        entity.setName(request.getName().trim());
        entity.setIsHierarchical(request.getIsHierarchical() != null ? request.getIsHierarchical() : false);
        entity.setDescription(request.getDescription());
        entity.setRemark(request.getRemark());
    }

    private TaxonomyEntity requireByCode(String code) {
        String normalized = code == null ? null : code.trim().toUpperCase();
        return repository.findByCodeAndStatusNot(normalized, Constants.STATUS_DELETE)
            .orElseThrow(() -> {
                AppException ex = new AppException("TAXONOMY_NOT_FOUND",
                    "Taxonomy not found: " + normalized);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
