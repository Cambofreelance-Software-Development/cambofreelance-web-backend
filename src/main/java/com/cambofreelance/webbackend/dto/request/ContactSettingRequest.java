package com.cambofreelance.webbackend.dto.request;

import lombok.Data;

@Data
public class ContactSettingRequest {

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
