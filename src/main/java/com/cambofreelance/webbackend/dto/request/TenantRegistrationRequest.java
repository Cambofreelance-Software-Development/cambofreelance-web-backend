package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class TenantRegistrationRequest {

    // Tenant
    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Name is required")
    private String name;

    private String tenantType;
    private String description;
    private String logoUrl;
    private String primaryColor;
    private String secondaryColor;

    @NotBlank(message = "Company name is required")
    private String companyName;

    private String companyAddress;

    @NotBlank(message = "Company email is required")
    @Email
    private String companyEmail;

    private String companyPhone;
    private String website;

    // Subscription
    @NotBlank(message = "Package is required")
    private String packageId;

    @NotBlank(message = "Billing cycle is required")
    private String billingCycle;

    private BigDecimal amount;
    private String currency;
    private Boolean autoRenewal;

    // Admin user
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;
}
