package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryFeatureRequest {

    @NotBlank
    private String name;

    private String nameKh;

    private Integer sortOrder = 0;
}
