package com.cambofreelance.webbackend.dto.taxonomy.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaxonomyRequest {

    @NotBlank(message = "Taxonomy code is required!")
    private String code;

    @NotBlank(message = "Name is required!")
    private String name;

    private String remark;

    @NotNull(message = "IsHierarchical is required!")
    private Boolean isHierarchical;

    private String description;

    private UUID userId;

    private Date createdAt;

}
