package com.cambofreelance.webbackend.dto.request;

import lombok.Data;

@Data
public class MediaLibraryUpdateRequest {

    private String folder;
    private String title;
    private String altText;
    private String description;
    private String tags;
}
