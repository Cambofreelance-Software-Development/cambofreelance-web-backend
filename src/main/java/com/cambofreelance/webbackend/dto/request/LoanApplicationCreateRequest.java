package com.cambofreelance.webbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class LoanApplicationCreateRequest {

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    /** Minimum depends on currency (USD 10 / KHR 40000) — enforced in LoanApplicationServiceImpl. */
    @NotNull(message = "Loan amount is required")
    private BigDecimal loanAmount;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotNull(message = "Tenor value is required")
    @Min(value = 1, message = "Tenor value must be a positive integer")
    private Integer tenorValue;

    @NotBlank(message = "Tenor unit is required")
    private String tenorUnit;

    @NotBlank(message = "Payment type is required")
    private String paymentType;

    /** Optional — defaults to 12.00 in the service if not supplied. */
    private BigDecimal interestRate;

    /** Optional — defaults to 5.00 in the service if not supplied. */
    private BigDecimal serviceFeeRate;

    @NotNull(message = "First payment date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date firstPaymentDate;

    @NotBlank(message = "Purpose is required")
    private String purpose;
}
