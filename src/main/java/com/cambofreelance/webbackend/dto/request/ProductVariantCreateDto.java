package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Map;
import lombok.Data;

@Data
public class ProductVariantCreateDto {

    @NotBlank(message = "Variant name is required")
    private String name;

    @NotBlank(message = "Variant SKU is required")
    private String sku;

    private String barcode;
    private BigDecimal costPriceOverride;
    private BigDecimal retailPriceOverride;
    private BigDecimal wholesalePriceOverride;
    private BigDecimal vipPriceOverride;
    private String imageUrl;
    private Boolean isDefault = false;

    /** Map of attribute name to value, e.g. { "Colour": "Black", "Engine CC": "125" } */
    private Map<String, String> attributes;
}
