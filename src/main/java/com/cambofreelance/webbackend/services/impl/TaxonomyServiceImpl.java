package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.CreateTaxonomyRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.DetailTaxonomyRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.FilterRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.UpdateTaxonomyRequest;
import com.cambofreelance.webbackend.dto.taxonomy.response.CreateTaxonomyResponse;
import com.cambofreelance.webbackend.dto.taxonomy.response.DetailTaxonomyResponse;
import com.cambofreelance.webbackend.dto.taxonomy.response.PaginateResponse;
import com.cambofreelance.webbackend.dto.taxonomy.response.TaxonomyItemResponseDTO;
import com.cambofreelance.webbackend.dto.taxonomy.response.TaxonomyResponseDTO;
import com.cambofreelance.webbackend.dto.taxonomy.response.UpdateTaxonomyResponse;
import com.cambofreelance.webbackend.entities.TaxonomyEntity;
import com.cambofreelance.webbackend.entities.TaxonomyItemEntity;
import com.cambofreelance.webbackend.logger.contants.Constants;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.TaxonomyItemRepository;
import com.cambofreelance.webbackend.repository.TaxonomyRepository;
import com.cambofreelance.webbackend.services.TaxonomyService;
import com.cambofreelance.webbackend.utils.PaginationUtils;
import com.cambofreelance.webbackend.utils.SpecificationBuilder;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaxonomyServiceImpl implements TaxonomyService {

    private final TaxonomyRepository taxonomyRepository;
    private final TaxonomyItemRepository itemRepository;

    // Cache name constants
    private static final String CACHE_TAXONOMY       = "taxonomy";
    private static final String CACHE_TAXONOMY_LIST  = "taxonomy_list";

    @Override
//    @Caching(evict = {
//        @CacheEvict(value = CACHE_TAXONOMY_LIST, allEntries = true)
//    })
    public CreateTaxonomyResponse createTaxonomy(CreateTaxonomyRequest request, String userId) throws AppException {
        Optional<TaxonomyEntity> existing = taxonomyRepository.findByCode(request.getCode());
        if (existing.isPresent()) {
            throw new AppException(ErrorCode.INVALID_REQ_ERROR, "Taxonomy code already exists: " + request.getCode());
        }

        TaxonomyEntity taxonomyEntity = new TaxonomyEntity();
        taxonomyEntity.setCode(request.getCode());
        taxonomyEntity.setName(request.getName());
        taxonomyEntity.setIsHierarchical(request.getIsHierarchical());
        taxonomyEntity.setDescription(request.getDescription());
        taxonomyEntity.setRemark(request.getRemark());
        taxonomyEntity.setStatus(Constants.STATUS_ACT);
        taxonomyEntity.setUserId(UUID.randomUUID());
        taxonomyEntity.setCreatedAt(new Date());
        taxonomyRepository.save(taxonomyEntity);

        CreateTaxonomyResponse response = new CreateTaxonomyResponse();
        BeanUtils.copyProperties(taxonomyEntity, response);
        return response;
    }

    @Override
//    @Caching(
//        put    = { @CachePut(value = CACHE_TAXONOMY, key = "#request.code") },
//        evict  = { @CacheEvict(value = CACHE_TAXONOMY_LIST, allEntries = true) }
//    )
    public UpdateTaxonomyResponse updateTaxonomy(UpdateTaxonomyRequest request, String userId) throws AppException {
        TaxonomyEntity taxonomyEntity = taxonomyRepository
            .findByCodeAndStatus(request.getCode(), Constants.STATUS_ACT)
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQ_ERROR));

        taxonomyEntity.setName(request.getName());
        taxonomyEntity.setDescription(request.getDescription());
        taxonomyEntity.setRemark(request.getRemark());
        taxonomyEntity.setIsHierarchical(request.getIsHierarchical());
        taxonomyEntity.setUpdatedAt(new Date());
        taxonomyRepository.save(taxonomyEntity);

        UpdateTaxonomyResponse response = new UpdateTaxonomyResponse();
        response.setCode(taxonomyEntity.getCode());
        response.setName(taxonomyEntity.getName());
        response.setIsHierarchical(taxonomyEntity.getIsHierarchical());
        response.setDescription(taxonomyEntity.getDescription());
        response.setRemark(taxonomyEntity.getRemark());
        response.setUpdatedAt(taxonomyEntity.getUpdatedAt());
        return response;
    }

    @Override
//    @Cacheable(value = CACHE_TAXONOMY, key = "#code.code")
    public DetailTaxonomyResponse findByCode(DetailTaxonomyRequest code, String userId) throws AppException {
        log.info("Cache miss — fetching taxonomy from DB: {}", code.getCode());

        TaxonomyEntity taxonomyEntity = taxonomyRepository
            .findByCodeAndStatus(code.getCode(), Constants.STATUS_ACT)
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQ_ERROR));

        DetailTaxonomyResponse response = new DetailTaxonomyResponse();
        response.setCode(taxonomyEntity.getCode());
        response.setName(taxonomyEntity.getName());
        response.setIsHierarchical(taxonomyEntity.getIsHierarchical());
        response.setDescription(taxonomyEntity.getDescription());
        response.setRemark(taxonomyEntity.getRemark());
        response.setCreatedAt(taxonomyEntity.getCreatedAt());
        return response;
    }

    @Override
//    @Caching(evict = {
//        @CacheEvict(value = CACHE_TAXONOMY,      key = "#code.code"),
//        @CacheEvict(value = CACHE_TAXONOMY_LIST, allEntries = true)
//    })
    public String deleteByCode(DetailTaxonomyRequest code, String userId) throws AppException {
        TaxonomyEntity taxonomyEntity = taxonomyRepository
            .findByCodeAndStatus(code.getCode(), Constants.STATUS_ACT)
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_REQ_ERROR));

        taxonomyEntity.setStatus(Constants.STATUS_DEL);
        taxonomyEntity.setDeletedAt(new Date());
        taxonomyEntity.setUserId(UUID.fromString(userId));
        taxonomyRepository.save(taxonomyEntity);
        return "Taxonomy with code: " + code.getCode() + " deleted successfully";
    }

    @Override
//    @Cacheable(value = CACHE_TAXONOMY_LIST, key = "#req.paginate.page + '-' + #req.paginate.size + '-' + #req.search")
    public PaginateResponse<TaxonomyEntity> listAllTaxonomies(BaseRequest req) throws AppException {
        log.info("Cache miss — fetching taxonomy list from DB");

        Pageable pageable = PaginationUtils.toPageable(req, "createdAt");
        List<String> searchFields = List.of("code", "name", "description", "remark");
        List<FilterRequest> filters = Optional.ofNullable(req.getFilter()).orElse(List.of());
        Specification<TaxonomyEntity> spec = SpecificationBuilder.build(filters, req.getSearch(), searchFields);
        Page<TaxonomyEntity> listContent = taxonomyRepository.findAll(spec, pageable);

        PaginateResponse<TaxonomyEntity> response = new PaginateResponse<>();
        response.setContent(listContent.getContent());
        response.setMetadata(PaginationUtils.from(listContent));
        return response;
    }

    // ── private helpers ──────────────────────────────────────────────────────

    private TaxonomyItemResponseDTO toDto(TaxonomyItemEntity item) {
        TaxonomyItemResponseDTO dto = new TaxonomyItemResponseDTO();
        dto.setCode(item.getCode());
        dto.setTaxonomyCode(item.getTaxonomyCode());
        dto.setParentCode(item.getParentCode());
        dto.setDisplayKm(item.getDisplayKm());
        dto.setDisplayEn(item.getDisplayEn());
        dto.setMetadata(item.getMetadata());
        return dto;
    }

    private TaxonomyResponseDTO mapToResponseDTO(TaxonomyEntity entity) {
        TaxonomyResponseDTO dto = new TaxonomyResponseDTO();
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setIsHierarchical(entity.getIsHierarchical());
        dto.setDescription(entity.getDescription());
        dto.setRemark(entity.getRemark());
        return dto;
    }
}