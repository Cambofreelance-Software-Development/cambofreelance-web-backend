package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.BaseRequest;
import com.cambofreelance.webbackend.dto.request.TaxonomyItemDeleteRequest;
import com.cambofreelance.webbackend.dto.request.TaxonomyItemRequest;
import com.cambofreelance.webbackend.dto.response.TaxonomyItemPublicResponse;
import com.cambofreelance.webbackend.dto.response.TaxonomyItemResponse;
import com.cambofreelance.webbackend.dto.taxonomy.response.PaginateResponse;
import java.util.List;

public interface TaxonomyItemService {

    PaginateResponse<TaxonomyItemResponse> list(BaseRequest request);

    TaxonomyItemResponse create(TaxonomyItemRequest request);

    TaxonomyItemResponse update(TaxonomyItemRequest request);

    void delete(TaxonomyItemDeleteRequest request);

    List<TaxonomyItemPublicResponse> listPublicByCode(String taxonomyCode, String parentCode);
}
