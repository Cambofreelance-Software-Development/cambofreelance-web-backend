package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FeatureRequest {

    @NotBlank
    private String title;

    private String titleKh;

    private String description;

    private String descriptionKh;

    private String categoryId;

    private String icon;

    private String imageId;

    private String link;

    private Integer sortOrder = 0;
}
