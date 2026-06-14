package com.cambofreelance.authenticationservice.dto.request;

import lombok.Data;

@Data
public class CmsSeoSettingRequest {

    private String title;
    private String description;
    private String keywords;
    private String canonicalUrl;
    private String robots;
    private String ogTitle;
    private String ogDescription;
    private String ogImage;
}
