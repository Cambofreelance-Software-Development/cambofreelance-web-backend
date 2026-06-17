package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class LoanDisbursementCreateRequest {

    @NotBlank(message = "Loan application ID is required")
    private String loanApplicationId;

    /** Optional — defaults to loan's netDisbursement if not supplied. */
    private BigDecimal disbursementAmount;

    private String notes;
}
