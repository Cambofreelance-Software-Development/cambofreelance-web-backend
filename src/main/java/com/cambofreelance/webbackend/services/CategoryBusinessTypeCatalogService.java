package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.CategoryBusinessTypeCatalogRequest;
import com.cambofreelance.webbackend.dto.response.CategoryBusinessTypeCatalogResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CategoryBusinessTypeCatalogService {

    List<CategoryBusinessTypeCatalogResponse> listAll();

    Page<CategoryBusinessTypeCatalogResponse> search(String search, int page, int size);

    CategoryBusinessTypeCatalogResponse getById(String id);

    CategoryBusinessTypeCatalogResponse create(CategoryBusinessTypeCatalogRequest request, String createdBy);

    CategoryBusinessTypeCatalogResponse update(String id, CategoryBusinessTypeCatalogRequest request, String updatedBy);

    void delete(String id);
}
