package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.CategoryProductRequest;
import com.cambofreelance.webbackend.dto.response.CategoryProductResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CategoryProductService {

    List<CategoryProductResponse> listAll();

    Page<CategoryProductResponse> search(String search, int page, int size);

    CategoryProductResponse getById(String id);

    CategoryProductResponse create(CategoryProductRequest request, String createdBy);

    CategoryProductResponse update(String id, CategoryProductRequest request, String updatedBy);

    void delete(String id);
}
