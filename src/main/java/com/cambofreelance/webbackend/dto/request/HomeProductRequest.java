package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HomeProductRequest {

    @NotBlank
    private String name;

    private String nameKh;

    private String description;

    private String descriptionKh;

    private String icon;

    private String iconBg;

    private String href;

    private Integer sortOrder = 0;
}
