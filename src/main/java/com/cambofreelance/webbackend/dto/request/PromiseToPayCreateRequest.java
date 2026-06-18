package com.cambofreelance.webbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class PromiseToPayCreateRequest {

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date promiseDate;

    @NotNull
    @Positive
    private BigDecimal promiseAmount;

    private String notes;
}
