package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.CreateTaxonomyItemRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.DetailTaxonomyItemRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.FilterRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.UpdateTaxonomyItemRequest;
import com.cambofreelance.webbackend.dto.taxonomy.response.CreateTaxonomyItemResponse;
import com.cambofreelance.webbackend.dto.taxonomy.response.PaginateResponse;
import com.cambofreelance.webbackend.dto.taxonomy.response.TaxonomyItemResponseDTO;
import com.cambofreelance.webbackend.dto.taxonomy.response.UpdateTaxonomyItemResponse;
import com.cambofreelance.webbackend.entities.TaxonomyItemEntity;
import com.cambofreelance.webbackend.logger.contants.Constants;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.TaxonomyItemRepository;
import com.cambofreelance.webbackend.services.TaxonomyItemService;
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
public class TaxonomyItemServiceImpl implements TaxonomyItemService {

    private final TaxonomyItemRepository taxonomyItemRepository;

    private static final String CACHE_ITEM      = "taxonomy_item";
    private static final String CACHE_ITEM_LIST = "taxonomy_item_list";

    @Override
//    @CacheEvict(value = CACHE_ITEM_LIST, allEntries = true)
    public CreateTaxonomyItemResponse createTaxonomyItem(CreateTaxonomyItemRequest request, String userId) throws AppException {
        // Duplicate check
        if (taxonomyItemRepository.existsById(request.getCode())) {
            throw new AppException("Taxonomy item code '" + request.getCode() + "' already exists.");
        }

        TaxonomyItemEntity taxonomyItemEntity = new TaxonomyItemEntity();
        taxonomyItemEntity.setCode(request.getCode());
        taxonomyItemEntity.setTaxonomyCode(request.getTaxonomyCode());
        taxonomyItemEntity.setDisplayKm(request.getDisplayKm());
        taxonomyItemEntity.setDisplayEn(request.getDisplayEn());

        if (request.getParentCode() != null && !request.getParentCode().isBlank()) {
            if (!taxonomyItemRepository.existsById(request.getParentCode())) {
                throw new AppException("Parent code '" + request.getParentCode() + "' does not exist.");
            }
            taxonomyItemEntity.setParentCode(request.getParentCode());
        } else {
            taxonomyItemEntity.setParentCode(null);
        }

        taxonomyItemEntity.setMetadata(request.getMetadata());
        taxonomyItemEntity.setStatus(Constants.STATUS_ACTIVE);
        taxonomyItemEntity.setUserId(UUID.randomUUID());
        taxonomyItemEntity.setCreatedAt(new Date());
        taxonomyItemRepository.save(taxonomyItemEntity);

        CreateTaxonomyItemResponse response = new CreateTaxonomyItemResponse();
        response.setCode(taxonomyItemEntity.getCode());
        response.setTaxonomyCode(taxonomyItemEntity.getTaxonomyCode());
        response.setDisplayKm(taxonomyItemEntity.getDisplayKm());
        response.setDisplayEn(taxonomyItemEntity.getDisplayEn());
        response.setParentCode(taxonomyItemEntity.getParentCode());
        response.setUserId(taxonomyItemEntity.getUserId());
        response.setMetadata(taxonomyItemEntity.getMetadata());
        response.setCreatedAt(taxonomyItemEntity.getCreatedAt());
        return response;
    }

    @Override
//    @Caching(
//        put   = { @CachePut(value = CACHE_ITEM, key = "#request.code") },
//        evict = { @CacheEvict(value = CACHE_ITEM_LIST, allEntries = true) }
//    )
    public UpdateTaxonomyItemResponse updateTaxonomyItem(UpdateTaxonomyItemRequest request, String userId) throws AppException {
        TaxonomyItemEntity item = taxonomyItemRepository
            .findByCodeAndStatus(request.getCode(), Constants.STATUS_ACTIVE)
            .orElseThrow(() -> new AppException("Taxonomy item with code '" + request.getCode() + "' not found."));

        if (request.getParentCode() != null && !request.getParentCode().isBlank()) {
            // NOTE: original code compared request.getCode() to itself — fixed to compare parentCode vs code
            if (!request.getParentCode().equals(request.getCode())) {
                boolean parentExists = taxonomyItemRepository
                    .findByCodeAndStatus(request.getParentCode(), Constants.STATUS_ACTIVE)
                    .isPresent();
                if (!parentExists) {
                    throw new AppException("Parent code '" + request.getParentCode() + "' does not exist.");
                }
            }
            item.setParentCode(request.getParentCode());
        } else {
            item.setParentCode(null);
        }

        item.setDisplayKm(request.getDisplayKm());
        item.setDisplayEn(request.getDisplayEn());
        item.setTaxonomyCode(request.getTaxonomyCode());
        item.setMetadata(request.getMetadata());
        item.setUpdatedAt(new Date());
        taxonomyItemRepository.save(item);

        UpdateTaxonomyItemResponse response = new UpdateTaxonomyItemResponse();
        response.setCode(item.getCode());
        response.setTaxonomyCode(item.getTaxonomyCode());
        response.setDisplayKm(item.getDisplayKm());
        response.setDisplayEn(item.getDisplayEn());
        response.setParentCode(item.getParentCode());
        response.setMetadata(item.getMetadata());
        response.setUserId(item.getUserId());
        response.setUpdatedAt(item.getUpdatedAt());
        return response;
    }

    @Override
//    @Cacheable(value = CACHE_ITEM_LIST, key = "#req.paginate.page + '-' + #req.paginate.size + '-' + #req.search")
    public PaginateResponse<TaxonomyItemEntity> listAllTaxonomyItems(BaseRequest req) {
        log.info("Cache miss — fetching taxonomy item list from DB");

        Pageable pageable = PaginationUtils.toPageable(req, "createdAt");
        List<String> searchFields = List.of("displayKm", "code", "taxonomyCode", "parentCode", "displayEn");
        List<FilterRequest> filters = Optional.ofNullable(req.getFilter()).orElse(List.of());
        Specification<TaxonomyItemEntity> spec = SpecificationBuilder.build(filters, req.getSearch(), searchFields);
        Page<TaxonomyItemEntity> listContent = taxonomyItemRepository.findAll(spec, pageable);

        PaginateResponse<TaxonomyItemEntity> response = new PaginateResponse<>();
        response.setContent(listContent.getContent());
        response.setMetadata(PaginationUtils.from(listContent));
        return response;
    }

    @Override
//    @Cacheable(value = CACHE_ITEM, key = "#request.code")
    public TaxonomyItemResponseDTO findByCode(DetailTaxonomyItemRequest request, String userId) throws AppException {
        log.info("Cache miss — fetching taxonomy item from DB: {}", request.getCode());

        TaxonomyItemEntity item = taxonomyItemRepository
            .findByCodeAndStatus(request.getCode(), Constants.STATUS_ACTIVE)
            .orElseThrow(() -> {
                log.error("Taxonomy item not found for code: {}", request.getCode());
                return new AppException(ErrorCode.INVALID_REQ_ERROR);
            });

        TaxonomyItemResponseDTO response = new TaxonomyItemResponseDTO();
        BeanUtils.copyProperties(item, response);
        return response;
    }

    @Override
//    @Caching(evict = {
//        @CacheEvict(value = CACHE_ITEM,      key = "#code.code"),
//        @CacheEvict(value = CACHE_ITEM_LIST, allEntries = true)
//    })
    public String deleteByCode(DetailTaxonomyItemRequest code, String userId) throws AppException {
        TaxonomyItemEntity item = taxonomyItemRepository
            .findByCodeAndStatus(code.getCode(), Constants.STATUS_ACTIVE)
            .orElseThrow(() -> new AppException("Taxonomy Item with code '" + code.getCode() + "' not found or already deleted."));

        item.setStatus(Constants.STATUS_DEL);
        item.setDeletedAt(new Date());
        item.setUserId(UUID.fromString(userId));
        taxonomyItemRepository.save(item);

        return "Taxonomy Item with code: " + code.getCode() + " deleted successfully";
    }
}