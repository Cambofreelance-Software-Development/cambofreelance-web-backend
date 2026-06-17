package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MediaLibraryAddRequest {

    @NotBlank(message = "Media ID is required")
    private String mediaId;

    private String folder;
    private String title;
    private String altText;
    private String description;
    private String tags;
}
