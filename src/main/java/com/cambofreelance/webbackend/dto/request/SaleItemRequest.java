package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class SaleItemRequest {

    @NotBlank(message = "Variant ID is required")
    private String variantId;

    private String inventoryItemId; // Required for serialized vehicle sale
    private String batchId;

    @NotNull(message = "Quantity is required")
    private BigDecimal quantity = BigDecimal.ONE;

    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice = BigDecimal.ZERO;

    private BigDecimal discountAmount = BigDecimal.ZERO;
}
