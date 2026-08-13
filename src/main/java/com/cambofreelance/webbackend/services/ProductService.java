package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.ProductRequest;
import com.cambofreelance.webbackend.dto.response.ProductResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProductService {

    List<ProductResponse> listAll();

    List<ProductResponse> listPublic(String categoryId);

    Page<ProductResponse> search(String search, int page, int size);

    ProductResponse getById(String id);

    ProductResponse create(ProductRequest request, String createdBy);

    ProductResponse update(String id, ProductRequest request, String updatedBy);

    void delete(String id);
}
