package com.cambofreelance.webbackend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactSettingResponse {

    private String siteEmail;
    private String sitePhone;
    private String siteAddress;
    private String footerCopyright;
    private String socialTelegram;
    private String socialFacebook;
    private String socialLinkedin;
    private String socialTwitter;
    private String socialInstagram;
}
