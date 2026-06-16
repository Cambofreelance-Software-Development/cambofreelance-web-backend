package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TenantUserCreateRequest {

    /** Local part only — the tenant's code is prefixed automatically, e.g. "VLG-001.johndoe" */
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    private String email;

    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;
}
