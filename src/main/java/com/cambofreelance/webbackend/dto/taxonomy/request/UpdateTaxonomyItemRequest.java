package com.cambofreelance.webbackend.dto.taxonomy.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaxonomyItemRequest {

    @NotBlank(message = "Code is required")
    private String code;

    private String displayKm;

    @NotBlank(message = "Display (English) is required")
    private String displayEn;

    @NotBlank(message = "Taxonomy Code is required")
    private String taxonomyCode;

    private String parentCode;

    private String metadata;
}
