package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CmsGeneralSettingRequest {

    @NotBlank
    private String siteName;

    @NotBlank
    @Pattern(regexp = "DEV|UAT|PROD", message = "environment must be DEV, UAT, or PROD")
    private String environment;

    @NotBlank
    private String defaultLanguage;

    @NotBlank
    private String timeZone;

    private String siteLogo;

    private String siteDescription;
}
