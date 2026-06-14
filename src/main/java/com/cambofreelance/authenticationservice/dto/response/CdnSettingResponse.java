package com.cambofreelance.authenticationservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CdnSettingResponse {

    private boolean enabled;
    private String provider;
    private String baseUrl;
    private String customDomain;
}
