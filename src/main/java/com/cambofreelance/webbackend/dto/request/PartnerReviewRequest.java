package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PartnerReviewRequest {

    /** UNDER_REVIEW / DEMO_SCHEDULED / APPROVED / REJECTED — must be a valid transition from the current state. */
    @NotBlank(message = "Decision is required")
    private String decision;

    private String note;
}
