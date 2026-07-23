package com.cambofreelance.webbackend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SitePublicConfigResponse {

    private String siteName;
    private String siteDescription;
    private String siteLogo;
    private String siteLogoFooter;
    private String siteAddress;
    private String siteEmail;
    private String sitePhone;
    private String socialTwitter;
    private String socialLinkedin;
    private String socialInstagram;
    private String socialFacebook;
}
