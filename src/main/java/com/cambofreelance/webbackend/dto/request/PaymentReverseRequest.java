package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentReverseRequest {

    @NotBlank
    private String reversalReason;
}
