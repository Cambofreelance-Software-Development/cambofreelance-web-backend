package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TenantBrandingUpdateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String logoUrl;
}
