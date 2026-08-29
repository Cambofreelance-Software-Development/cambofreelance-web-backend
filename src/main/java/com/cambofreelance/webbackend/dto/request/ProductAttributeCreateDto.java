package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductAttributeCreateDto {

    private String attributeId;

    @NotBlank(message = "Attribute name is required")
    private String attributeName;

    @NotBlank(message = "Attribute value is required")
    private String attributeValue;
}
