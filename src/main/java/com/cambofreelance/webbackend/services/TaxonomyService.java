package com.cambofreelance.webbackend.services;


import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.CreateTaxonomyRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.DetailTaxonomyRequest;
import com.cambofreelance.webbackend.dto.taxonomy.request.UpdateTaxonomyRequest;
import com.cambofreelance.webbackend.dto.taxonomy.response.CreateTaxonomyResponse;
import com.cambofreelance.webbackend.dto.taxonomy.response.DetailTaxonomyResponse;
import com.cambofreelance.webbackend.dto.taxonomy.response.PaginateResponse;
import com.cambofreelance.webbackend.dto.taxonomy.response.UpdateTaxonomyResponse;
import com.cambofreelance.webbackend.entities.TaxonomyEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;

public interface TaxonomyService {

    CreateTaxonomyResponse createTaxonomy(CreateTaxonomyRequest request, String userId) throws AppException;

    UpdateTaxonomyResponse updateTaxonomy(UpdateTaxonomyRequest request, String userId) throws AppException;

    DetailTaxonomyResponse findByCode(DetailTaxonomyRequest code, String userId) throws AppException;

    String deleteByCode(DetailTaxonomyRequest code, String userId) throws AppException;

    PaginateResponse<TaxonomyEntity> listAllTaxonomies(BaseRequest request) throws AppException;
}
