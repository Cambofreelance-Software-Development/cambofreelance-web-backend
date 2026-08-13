package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank
    private String name;

    private String nameKh;

    private String description;

    private String descriptionKh;

    private String price;

    private String categoryId;

    private String imageId;

    private String icon;

    private String link;

    private Integer sortOrder = 0;
}
