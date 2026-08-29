package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.ProductAttributeEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductAttributeResponse {

    private String id;
    private String productId;
    private String attributeId;
    private String attributeName;
    private String attributeValue;

    public static ProductAttributeResponse from(ProductAttributeEntity e) {
        return ProductAttributeResponse.builder()
            .id(e.getId())
            .productId(e.getProductId())
            .attributeId(e.getAttributeId())
            .attributeName(e.getAttributeName())
            .attributeValue(e.getAttributeValue())
            .build();
    }
}
