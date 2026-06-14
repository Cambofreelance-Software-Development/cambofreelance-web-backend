package com.cambofreelance.authenticationservice.dto.request;

import lombok.Data;

@Data
public class CdnSettingRequest {

    private boolean enabled;
    private String provider;
    private String baseUrl;
    private String customDomain;
}
