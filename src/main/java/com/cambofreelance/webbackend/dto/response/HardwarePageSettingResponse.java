package com.cambofreelance.webbackend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HardwarePageSettingResponse {

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
