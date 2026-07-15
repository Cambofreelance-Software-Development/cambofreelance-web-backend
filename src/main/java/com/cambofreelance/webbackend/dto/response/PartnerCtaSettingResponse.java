package com.cambofreelance.webbackend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartnerCtaSettingResponse {

    private String title;
    private String titleKh;
    private String body;
    private String bodyKh;
    private String label;
    private String labelKh;
    private String link;
}
