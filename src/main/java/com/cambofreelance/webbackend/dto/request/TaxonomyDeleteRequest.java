package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaxonomyDeleteRequest {

    @NotBlank
    private String code;
}
