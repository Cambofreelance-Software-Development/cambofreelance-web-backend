package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.ProductVariantEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductVariantResponse {

    private String id;
    private String productId;
    private String name;
    private String sku;
    private String barcode;
    private BigDecimal costPriceOverride;
    private BigDecimal retailPriceOverride;
    private BigDecimal wholesalePriceOverride;
    private BigDecimal vipPriceOverride;
    private String imageUrl;
    private Boolean isDefault;
    private String variantStatus;
    private Map<String, String> attributes;
    private long inStockCount;

    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;

    public static ProductVariantResponse from(ProductVariantEntity e, Map<String, String> attrs, long inStock) {
        return ProductVariantResponse.builder()
            .id(e.getId())
            .productId(e.getProductId())
            .name(e.getName())
            .sku(e.getSku())
            .barcode(e.getBarcode())
            .costPriceOverride(e.getCostPriceOverride())
            .retailPriceOverride(e.getRetailPriceOverride())
            .wholesalePriceOverride(e.getWholesalePriceOverride())
            .vipPriceOverride(e.getVipPriceOverride())
            .imageUrl(e.getImageUrl())
            .isDefault(e.getIsDefault())
            .variantStatus(e.getVariantStatus())
            .attributes(attrs)
            .inStockCount(inStock)
            .createdAt(e.getCreatedAt())
            .createdBy(e.getCreatedBy())
            .updatedAt(e.getUpdatedAt())
            .updatedBy(e.getUpdatedBy())
            .build();
    }
}
