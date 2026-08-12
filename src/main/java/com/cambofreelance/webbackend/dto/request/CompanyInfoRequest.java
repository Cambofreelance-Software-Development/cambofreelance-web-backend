package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CompanyInfoRequest {

    @NotBlank
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String address;
    private String city;
    private String country;
    private String businessType;
    private String logoUrl;
}
