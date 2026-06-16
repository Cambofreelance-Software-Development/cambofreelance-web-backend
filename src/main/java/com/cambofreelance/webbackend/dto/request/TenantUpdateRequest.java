package com.cambofreelance.webbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date planExpiredDate;
}
