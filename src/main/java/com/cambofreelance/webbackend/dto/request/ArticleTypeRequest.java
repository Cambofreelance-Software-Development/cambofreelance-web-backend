package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArticleTypeRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String label;

    private String labelKh;

    private Integer sortOrder = 0;
}
