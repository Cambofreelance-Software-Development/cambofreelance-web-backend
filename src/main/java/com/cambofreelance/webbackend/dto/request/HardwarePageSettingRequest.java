package com.cambofreelance.webbackend.dto.request;

import lombok.Data;

@Data
public class HardwarePageSettingRequest {

    private String heroTitle;
    private String heroTitleKh;
    private String heroSubtitle;
    private String heroSubtitleKh;
    private String heroCtaLabel;
    private String heroCtaLabelKh;
    private String heroCtaLink;

    private String downloadAndroidUrl;
    private String downloadIosUrl;
}
