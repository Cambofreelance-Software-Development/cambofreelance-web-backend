package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class ProductCreateRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "SKU is required")
    private String sku;

    private String barcode;
    private String productCode;

    @NotBlank(message = "Product type is required")
    private String productType; // STOCK, VEHICLE, PART, ELECTRONICS, SERVICE

    @NotBlank(message = "Category is required")
    private String categoryId;

    private String brand;
    private String model;
    private Integer modelYear;

    @NotBlank(message = "Unit is required")
    private String unit;

    private String preferredSupplier;
    private String description;
    private String imageUrl;

    @NotBlank(message = "Tracking type is required")
    private String trackingType; // QUANTITY, BATCH, SERIALIZED

    private String catalogStatus; // DRAFT, ACTIVE, INACTIVE
    private Boolean hasVariants = false;

    // Pricing
    private String currency = "USD";
    private BigDecimal costPrice;
    private BigDecimal retailPrice;
    private BigDecimal wholesalePrice;
    private BigDecimal vipPrice;
    private BigDecimal discountValue;
    private String discountType;
    private String taxRate;

    // Inventory thresholds
    private Integer openingStock;
    private Integer reorderLevel;
    private Integer minStock;
    private Integer maxStock;
    private String defaultWarehouseId;
    private String defaultLocation;

    // Packaging ladder
    private String caseName;
    private Integer caseQty;
    private String caseBarcode;
    private String boxName;
    private Integer boxQty;
    private String boxBarcode;

    // Vehicle specifications
    private Integer engineCc;
    private String fuelType;
    private String transmission;
    private String vehicleCondition;
    private String warrantyPeriod;

    // Additional attributes
    private List<ProductAttributeCreateDto> additionalAttributes;

    // Variants (if hasVariants = true)
    private List<ProductVariantCreateDto> variants;

    // Serialized Items (if trackingType = SERIALIZED, e.g. opening items)
    private List<InventoryItemCreateRequest> initialItems;
}
