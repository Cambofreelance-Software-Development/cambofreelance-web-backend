package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class SaleCreateRequest {

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    private String salespersonId;
    private String warehouseId;

    /** CASH | INSTALLMENT | BANK_LOAN */
    private String paymentType = "CASH";

    private String currency = "USD";
    private BigDecimal discountAmount = BigDecimal.ZERO;
    private BigDecimal taxAmount = BigDecimal.ZERO;
    private BigDecimal downPayment = BigDecimal.ZERO;
    private BigDecimal financedAmount = BigDecimal.ZERO;
    private String notes;

    @NotEmpty(message = "At least one sale item is required")
    @Valid
    private List<SaleItemRequest> items;

    // Optional immediate financing submission
    private FinancingSubmitRequest financing;
}
