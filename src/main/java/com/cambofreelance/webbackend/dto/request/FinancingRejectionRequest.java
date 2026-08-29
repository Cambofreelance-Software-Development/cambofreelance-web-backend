package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FinancingRejectionRequest {

    @NotBlank(message = "Rejection reason is required")
    private String rejectionReason;
}
