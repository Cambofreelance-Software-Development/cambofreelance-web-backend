package com.cambofreelance.webbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class RepaymentInstallmentUpdateRequest {

    @NotBlank(message = "Status is required")
    private String installmentStatus;

    private BigDecimal paidAmount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paidDate;

    private String notes;
}
