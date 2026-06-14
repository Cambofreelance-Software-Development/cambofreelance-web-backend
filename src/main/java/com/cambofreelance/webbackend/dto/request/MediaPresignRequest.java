package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MediaPresignRequest {

    @NotBlank
    private String fileName;

    private String mimeType;

    private Long fileSize;
}
