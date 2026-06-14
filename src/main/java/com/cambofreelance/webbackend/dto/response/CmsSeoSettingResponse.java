package com.cambofreelance.webbackend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CmsSeoSettingResponse {

    private String title;
    private String description;
    private String keywords;
    private String canonicalUrl;
    private String robots;
    private String ogTitle;
    private String ogDescription;
    private String ogImage;
}
