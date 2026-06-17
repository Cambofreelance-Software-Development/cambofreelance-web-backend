package com.cambofreelance.webbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class PaymentCreateRequest {

    @NotBlank
    private String loanApplicationId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paymentDate;

    private BigDecimal principalPaid;
    private BigDecimal interestPaid;
    private BigDecimal penaltyFee;

    @NotNull
    @Positive
    private BigDecimal totalPaid;

    @NotBlank
    private String paymentMethod;

    @NotBlank
    private String paymentType;

    private String notes;
}
