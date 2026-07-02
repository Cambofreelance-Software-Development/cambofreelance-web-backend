package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TenantCreateRequest {

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Name is required")
    private String name;

    /** VILLAGE | BRANCH | COOPERATIVE | LENDER */
    private String tenantType;

    private String description;

    private String logoUrl;

    private String primaryColor;

    private String secondaryColor;

    private String companyName;

    private String companyAddress;

    private String companyEmail;

    private String companyPhone;

    private String website;

    // Tenant admin user, auto-created and assigned to the tenant

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;
}
