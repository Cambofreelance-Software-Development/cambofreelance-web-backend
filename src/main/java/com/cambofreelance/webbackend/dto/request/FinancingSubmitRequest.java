package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class FinancingSubmitRequest {

    private String saleId;

    @NotBlank(message = "Bank ID is required")
    private String bankId;

    @NotNull(message = "Requested amount is required")
    private BigDecimal requestedAmount;

    private Integer termMonths;
    private BigDecimal interestRate;
    private BigDecimal monthlyInstallment;
    private String guarantorCustomerId;
    private String externalReference; // Mini Loan application ID if integrated
}
