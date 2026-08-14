package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.BusinessTypeCatalogRequest;
import com.cambofreelance.webbackend.dto.response.BusinessTypeCatalogResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface BusinessTypeCatalogService {

    List<BusinessTypeCatalogResponse> listAll();

    List<BusinessTypeCatalogResponse> listPublic(String categoryId);

    Page<BusinessTypeCatalogResponse> search(String search, int page, int size);

    BusinessTypeCatalogResponse getById(String id);

    BusinessTypeCatalogResponse create(BusinessTypeCatalogRequest request, String createdBy);

    BusinessTypeCatalogResponse update(String id, BusinessTypeCatalogRequest request, String updatedBy);

    void delete(String id);
}
