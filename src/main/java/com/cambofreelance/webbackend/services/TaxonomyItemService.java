package com.cambofreelance.webbackend.services;


import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.CreateTaxonomyItemRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.DetailTaxonomyItemRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.UpdateTaxonomyItemRequest;
import com.cambofreelance.webbackend.dto.taxonomy.response.CreateTaxonomyItemResponse;
import com.cambofreelance.webbackend.dto.taxonomy.response.PaginateResponse;
import com.cambofreelance.webbackend.dto.taxonomy.response.TaxonomyItemResponseDTO;
import com.cambofreelance.webbackend.dto.taxonomy.response.UpdateTaxonomyItemResponse;
import com.cambofreelance.webbackend.entities.TaxonomyItemEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;

public interface TaxonomyItemService {

    CreateTaxonomyItemResponse createTaxonomyItem(CreateTaxonomyItemRequest request, String userId) throws AppException;

    UpdateTaxonomyItemResponse updateTaxonomyItem(UpdateTaxonomyItemRequest request, String userId) throws AppException;

    PaginateResponse<TaxonomyItemEntity> listAllTaxonomyItems(BaseRequest req) throws AppException;

    TaxonomyItemResponseDTO findByCode(DetailTaxonomyItemRequest request, String userId) throws AppException;

    String deleteByCode(DetailTaxonomyItemRequest code, String userId) throws AppException;
}
