package com.cambofreelance.webbackend.services;

import java.time.Duration;
import java.util.List;

public interface SpacesService {

    String presignPut(
        String endpoint,
        String region,
        String bucket,
        String accessKey,
        String secretKey,
        String objectKey,
        String mimeType,
        Duration expiry
    );

    byte[] getObject(
        String endpoint,
        String region,
        String bucket,
        String accessKey,
        String secretKey,
        String objectKey
    );

    /**
     * Applies a CORS configuration to the bucket so browsers can PUT directly
     * from any listed origin. Call once after configuring storage settings.
     */
    void setupBucketCors(
        String endpoint,
        String region,
        String bucket,
        String accessKey,
        String secretKey,
        List<String> allowedOrigins
    );
}
