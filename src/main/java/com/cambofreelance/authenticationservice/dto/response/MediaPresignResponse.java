package com.cambofreelance.authenticationservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaPresignResponse {

    private String mediaId;
    private String presignedUrl;
    private String publicUrl;
    private String objectKey;
}
