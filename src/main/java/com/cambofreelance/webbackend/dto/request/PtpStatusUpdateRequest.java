package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PtpStatusUpdateRequest {

    @NotBlank
    private String ptpStatus;
}
