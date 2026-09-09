package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Used for both the first submit and later edits of a partner application. */
@Data
public class PartnerApplicationRequest {

    @NotBlank(message = "Partner name is required")
    private String partnerName;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Partner type is required")
    private String partnerType;

    private String businessType;

    private String employeeCount;

    private String businessRegistrationNo;

    private String website;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "City / province is required")
    private String city;

    @NotBlank(message = "Business address is required")
    private String businessAddress;

    private String logoUrl;

    @Size(max = 1000, message = "Notes must be at most 1000 characters")
    private String notes;

    /** Must be true — the applicant confirms the partner agreement, code of conduct and revenue-share policy. */
    private Boolean agreementAccepted;
}
