package com.cambofreelance.webbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/** All fields are optional on update — the service applies only non-null values present. */
@Data
public class LoanApplicationUpdateRequest {

    private String customerId;

    /** Minimum depends on currency (USD 10 / KHR 40000) — enforced in LoanApplicationServiceImpl. */
    private BigDecimal loanAmount;

    private String currency;

    @Min(value = 1, message = "Tenor value must be a positive integer")
    private Integer tenorValue;

    private String tenorUnit;

    private String paymentType;

    private BigDecimal interestRate;

    private BigDecimal serviceFeeRate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date firstPaymentDate;

    private String purpose;
}
