package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefundPaymentRequest {

    /** Why this payment is being refunded — required for the audit/event trail. */
    @NotBlank
    private String reason;
}
