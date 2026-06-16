package com.cambofreelance.webbackend.dto.request;

import lombok.Data;

@Data
public class TenantUpdateRequest {

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
}
