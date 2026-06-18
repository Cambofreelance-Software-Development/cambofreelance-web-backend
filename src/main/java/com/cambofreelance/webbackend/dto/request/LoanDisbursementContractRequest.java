package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoanDisbursementContractRequest {

    @NotBlank(message = "Media ID is required")
    private String mediaId;
}
