package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.request.TaxonomyItemDeleteRequest;
import com.cambofreelance.webbackend.dto.request.TaxonomyItemRequest;
import com.cambofreelance.webbackend.dto.response.TaxonomyItemPublicResponse;
import com.cambofreelance.webbackend.dto.response.TaxonomyItemResponse;
import com.cambofreelance.webbackend.dto.taxonomy.response.PaginateResponse;
import com.cambofreelance.webbackend.entities.TaxonomyItemEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.TaxonomyItemRepository;
import com.cambofreelance.webbackend.repository.TaxonomyRepository;
import com.cambofreelance.webbackend.services.TaxonomyItemService;
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
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class TaxonomyItemServiceImpl implements TaxonomyItemService {

    private static final List<String> SEARCH_FIELDS = List.of("code", "displayEn", "displayKm");

    private final TaxonomyItemRepository repository;
    private final TaxonomyRepository taxonomyRepository;

    @Override
    public PaginateResponse<TaxonomyItemResponse> list(BaseRequest request) {
        Specification<TaxonomyItemEntity> spec =
            SpecificationBuilder.build(request.getFilter(), request.getSearch(), SEARCH_FIELDS);
        Page<TaxonomyItemEntity> page = repository.findAll(spec, PaginationUtils.toPageable(request, "createdAt"));

        PaginateResponse<TaxonomyItemResponse> result = new PaginateResponse<>();
        result.setContent(page.getContent().stream().map(TaxonomyItemResponse::from).collect(Collectors.toList()));
        result.setMetadata(PaginationUtils.from(page));
        return result;
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "TAXONOMY_ITEM")
    public TaxonomyItemResponse create(TaxonomyItemRequest request) {
        String code = request.getCode().trim().toUpperCase();
        String taxonomyCode = request.getTaxonomyCode().trim().toUpperCase();

        requireTaxonomyExists(taxonomyCode);

        if (repository.existsByTaxonomyCodeAndCodeAndStatusNot(taxonomyCode, code, Constants.STATUS_DELETE)) {
            AppException ex = new AppException("TAXONOMY_ITEM_CODE_EXISTS",
                "Taxonomy item code '" + code + "' already exists under taxonomy '" + taxonomyCode + "'");
            ex.setHttpStatus(HttpStatus.CONFLICT);
            throw ex;
        }

        TaxonomyItemEntity entity = new TaxonomyItemEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setCode(code);
        entity.setTaxonomyCode(taxonomyCode);
        entity.setCreatedBy(Constants.SYSTEM);
        applyRequest(entity, request);

        return TaxonomyItemResponse.from(repository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "TAXONOMY_ITEM")
    public TaxonomyItemResponse update(TaxonomyItemRequest request) {
        TaxonomyItemEntity entity = requireByTaxonomyCodeAndCode(request.getTaxonomyCode(), request.getCode());
        applyRequest(entity, request);
        entity.setUpdatedAt(new Date());

        return TaxonomyItemResponse.from(repository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "TAXONOMY_ITEM")
    public void delete(TaxonomyItemDeleteRequest request) {
        TaxonomyItemEntity entity;
        if (StringUtils.hasText(request.getTaxonomyCode())) {
            entity = requireByTaxonomyCodeAndCode(request.getTaxonomyCode(), request.getCode());
        } else {
            // Current frontend contract only posts { code } — resolve by code alone,
            // and refuse to guess if that code exists under more than one taxonomy.
            String code = request.getCode().trim().toUpperCase();
            List<TaxonomyItemEntity> matches = repository.findByCodeAndStatusNot(code, Constants.STATUS_DELETE);
            if (matches.isEmpty()) {
                AppException ex = new AppException("TAXONOMY_ITEM_NOT_FOUND",
                    "Taxonomy item not found: " + code);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                throw ex;
            }
            if (matches.size() > 1) {
                AppException ex = new AppException("TAXONOMY_ITEM_CODE_AMBIGUOUS",
                    "Taxonomy item code '" + code + "' exists under multiple taxonomies; specify taxonomyCode");
                ex.setHttpStatus(HttpStatus.CONFLICT);
                throw ex;
            }
            entity = matches.get(0);
        }
        entity.setStatus(Constants.STATUS_DELETE);
        entity.setUpdatedAt(new Date());
        repository.save(entity);
    }

    @Override
    public List<TaxonomyItemPublicResponse> listPublicByCode(String taxonomyCode, String parentCode) {
        String normalized = taxonomyCode == null ? null : taxonomyCode.trim().toUpperCase();
        List<TaxonomyItemEntity> items;
        if (StringUtils.hasText(parentCode)) {
            items = repository.findByTaxonomyCodeAndParentCodeAndStatusOrderBySortOrderAscDisplayEnAsc(
                normalized, parentCode.trim().toUpperCase(), Constants.STATUS_ACTIVE);
        } else {
            items = repository.findByTaxonomyCodeAndStatusOrderBySortOrderAscDisplayEnAsc(
                normalized, Constants.STATUS_ACTIVE);
        }
        return items.stream()
            .map(TaxonomyItemPublicResponse::from)
            .collect(Collectors.toList());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void applyRequest(TaxonomyItemEntity entity, TaxonomyItemRequest request) {
        entity.setParentCode(StringUtils.hasText(request.getParentCode())
            ? request.getParentCode().trim().toUpperCase() : null);
        entity.setDisplayEn(request.getDisplayEn().trim());
        entity.setDisplayKm(request.getDisplayKm());
        entity.setMetadata(request.getMetadata());
    }

    private void requireTaxonomyExists(String taxonomyCode) {
        if (!taxonomyRepository.existsByCodeAndStatusNot(taxonomyCode, Constants.STATUS_DELETE)) {
            AppException ex = new AppException("TAXONOMY_NOT_FOUND",
                "Taxonomy not found: " + taxonomyCode);
            ex.setHttpStatus(HttpStatus.NOT_FOUND);
            throw ex;
        }
    }

    private TaxonomyItemEntity requireByTaxonomyCodeAndCode(String taxonomyCode, String code) {
        String normalizedTaxonomyCode = taxonomyCode == null ? null : taxonomyCode.trim().toUpperCase();
        String normalizedCode = code == null ? null : code.trim().toUpperCase();
        return repository
            .findByTaxonomyCodeAndCodeAndStatusNot(normalizedTaxonomyCode, normalizedCode, Constants.STATUS_DELETE)
            .orElseThrow(() -> {
                AppException ex = new AppException("TAXONOMY_ITEM_NOT_FOUND",
                    "Taxonomy item not found: " + normalizedTaxonomyCode + "/" + normalizedCode);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
