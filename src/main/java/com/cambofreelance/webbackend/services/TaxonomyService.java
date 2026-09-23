package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.request.TaxonomyDeleteRequest;
import com.cambofreelance.webbackend.dto.request.TaxonomyRequest;
import com.cambofreelance.webbackend.dto.response.TaxonomyResponse;
import com.cambofreelance.webbackend.dto.taxonomy.response.PaginateResponse;

public interface TaxonomyService {

    PaginateResponse<TaxonomyResponse> list(BaseRequest request);

    TaxonomyResponse create(TaxonomyRequest request);

    TaxonomyResponse update(TaxonomyRequest request);

    void delete(TaxonomyDeleteRequest request);
}
