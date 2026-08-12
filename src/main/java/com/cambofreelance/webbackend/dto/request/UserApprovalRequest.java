package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserApprovalRequest {

    /** APR = approved, REJ = rejected, PEN = back to pending */
    @NotBlank
    private String approvalStatus;
}
