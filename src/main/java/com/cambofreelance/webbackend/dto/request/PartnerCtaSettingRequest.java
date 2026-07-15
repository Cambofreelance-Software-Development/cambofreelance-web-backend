package com.cambofreelance.webbackend.dto.request;

import lombok.Data;

@Data
public class PartnerCtaSettingRequest {

    private String title;
    private String titleKh;
    private String body;
    private String bodyKh;
    private String label;
    private String labelKh;
    private String link;
}
