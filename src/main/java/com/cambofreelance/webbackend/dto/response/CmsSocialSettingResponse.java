package com.cambofreelance.webbackend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CmsSocialSettingResponse {

    private String socialTwitter;
    private String socialLinkedin;
    private String socialInstagram;
    private String socialFacebook;
    private String socialTelegram;
    private String socialWhatsapp;
}
