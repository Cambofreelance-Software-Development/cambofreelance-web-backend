package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HardwareRequest {

    @NotBlank
    private String name;

    private String nameKh;

    private String brand;

    private String description;

    private String descriptionKh;

    private String connectivity;

    private String platform;

    private String categoryId;

    private String imageId;

    private String icon;

    private String link;

    private Integer sortOrder = 0;
}
