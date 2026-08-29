package com.cambofreelance.webbackend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CmsGeneralSettingResponse {

    private String siteName;
    private String environment;
    private String defaultLanguage;
    private String timeZone;
    private String siteLogo;
    private String siteLogoFooter;
    private String siteDescription;
    private String siteEmail;
}
