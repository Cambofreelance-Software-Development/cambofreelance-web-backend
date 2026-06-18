package com.cambofreelance.webbackend.dto.taxonomy.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaxonomyRequest {

    @NotBlank(message = "Taxonomy code is required!")
    private String code;

    @NotBlank(message = "Name is required!")
    private String name;

    private String remark;

    @NotNull(message = "IsHierarchical is required!")
    private Boolean isHierarchical;

    private String description;
}
