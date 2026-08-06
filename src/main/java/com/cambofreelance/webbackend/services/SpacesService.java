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

    /**
     * Fetches an object, honoring an optional HTTP {@code Range} header value
     * (e.g. "bytes=0-1023"). Pass {@code null} to fetch the whole object.
     */
    SpacesObject getObject(
        String endpoint,
        String region,
        String bucket,
        String accessKey,
        String secretKey,
        String objectKey,
        String rangeHeader
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
