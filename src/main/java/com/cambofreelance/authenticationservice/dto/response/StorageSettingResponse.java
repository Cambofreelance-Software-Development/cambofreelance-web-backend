package com.cambofreelance.authenticationservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StorageSettingResponse {

    private String provider;
    private String bucket;
    private String accessKey;
    private String endpoint;
    private String region;
    private String publicUrl;
}
