package com.cambofreelance.webbackend.dto.request;

import lombok.Data;

@Data
public class CmsSocialSettingRequest {

    private String socialTwitter;

    private String socialLinkedin;

    private String socialInstagram;

    private String socialFacebook;
}
