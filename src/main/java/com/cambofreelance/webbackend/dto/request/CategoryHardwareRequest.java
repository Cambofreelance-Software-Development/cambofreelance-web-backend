package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryHardwareRequest {

    @NotBlank
    private String name;

    private String nameKh;

    private String description;

    private String descriptionKh;

    private String imageId;

    private String icon;

    private String moreLink;

    private Integer sortOrder = 0;
}
