package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.ProductEntity;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    private String id;
    private String productCode;
    private String sku;
    private String barcode;
    private String name;
    private String productType;
    private String categoryId;
    private String brand;
    private String model;
    private Integer modelYear;
    private String unit;
    private String preferredSupplier;
    private String description;
    private String imageUrl;
    private String trackingType;
    private String catalogStatus;
    private Boolean hasVariants;

    // Pricing
    private String currency;
    private BigDecimal costPrice;
    private BigDecimal retailPrice;
    private BigDecimal wholesalePrice;
    private BigDecimal vipPrice;
    private BigDecimal discountValue;
    private String discountType;
    private String taxRate;

    // Margin calculations
    private BigDecimal grossMargin;
    private BigDecimal grossMarginPercentage;

    // Inventory status overview
    private long variantCount;
    private long totalStock;
    private long availableStock;
    private long reservedStock;
    private long loanPendingStock;
    private long soldStock;

    private Integer reorderLevel;
    private Integer minStock;
    private Integer maxStock;
    private String defaultWarehouseId;
    private String defaultLocation;

    private String status;
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;

    public static ProductResponse from(ProductEntity e, long variantCount, long totalStock, long availableStock) {
        BigDecimal margin = null;
        BigDecimal marginPct = null;
        if (e.getRetailPrice() != null && e.getCostPrice() != null) {
            margin = e.getRetailPrice().subtract(e.getCostPrice());
            if (e.getRetailPrice().compareTo(BigDecimal.ZERO) > 0) {
                marginPct = margin.divide(e.getRetailPrice(), 4, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            }
        }

        return ProductResponse.builder()
            .id(e.getId())
            .productCode(e.getProductCode())
            .sku(e.getSku())
            .barcode(e.getBarcode())
            .name(e.getName())
            .productType(e.getProductType())
            .categoryId(e.getCategoryId())
            .brand(e.getBrand())
            .model(e.getModel())
            .modelYear(e.getModelYear())
            .unit(e.getUnit())
            .preferredSupplier(e.getPreferredSupplier())
            .description(e.getDescription())
            .imageUrl(e.getImageUrl())
            .trackingType(e.getTrackingType())
            .catalogStatus(e.getCatalogStatus())
            .hasVariants(e.getHasVariants())
            .currency(e.getCurrency())
            .costPrice(e.getCostPrice())
            .retailPrice(e.getRetailPrice())
            .wholesalePrice(e.getWholesalePrice())
            .vipPrice(e.getVipPrice())
            .discountValue(e.getDiscountValue())
            .discountType(e.getDiscountType())
            .taxRate(e.getTaxRate())
            .grossMargin(margin)
            .grossMarginPercentage(marginPct)
            .variantCount(variantCount)
            .totalStock(totalStock)
            .availableStock(availableStock)
            .reorderLevel(e.getReorderLevel())
            .minStock(e.getMinStock())
            .maxStock(e.getMaxStock())
            .defaultWarehouseId(e.getDefaultWarehouseId())
            .defaultLocation(e.getDefaultLocation())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .createdBy(e.getCreatedBy())
            .updatedAt(e.getUpdatedAt())
            .updatedBy(e.getUpdatedBy())
            .build();
    }
}
