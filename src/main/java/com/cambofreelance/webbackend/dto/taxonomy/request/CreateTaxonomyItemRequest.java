package com.cambofreelance.webbackend.dto.taxonomy.request;

import jakarta.validation.constraints.NotBlank;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaxonomyItemRequest {
    @NotBlank(message = "Code is required")
    private String code;
    @NotBlank(message = "Taxonomy Code is required")
    private String taxonomyCode;
    private String parentCode;
    private String displayKm;
    private String displayEn;
    private String metadata;
    private Date createdAt;
}
