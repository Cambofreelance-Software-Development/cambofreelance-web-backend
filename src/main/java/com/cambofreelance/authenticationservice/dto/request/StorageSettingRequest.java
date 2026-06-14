package com.cambofreelance.authenticationservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StorageSettingRequest {

    @NotBlank
    @Pattern(regexp = "S3|MINIO|AZURE|SPACES", message = "provider must be S3, MINIO, AZURE, or SPACES")
    private String provider;

    private String bucket;
    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String region;
    private String publicUrl;
}
