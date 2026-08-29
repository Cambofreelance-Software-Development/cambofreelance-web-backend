package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.ProductCreateRequest;
import com.cambofreelance.webbackend.dto.response.ProductDetailResponse;
import com.cambofreelance.webbackend.dto.response.ProductResponse;
import com.cambofreelance.webbackend.dto.response.ProductVariantResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProductService {

    ProductResponse create(ProductCreateRequest request, String userId);

    ProductResponse update(String productId, ProductCreateRequest request, String userId);

    ProductResponse getById(String productId);

    ProductDetailResponse getDetail(String productId);

    Page<ProductResponse> search(
        String search,
        String categoryId,
        String productType,
        String trackingType,
        String catalogStatus,
        int page,
        int size
    );

    List<ProductVariantResponse> getVariants(String productId);

    void delete(String productId, String userId);

    String generateSku(String categoryId, String brand, String model);
}
