package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HelpCenterCategoryRequest {

    @NotBlank
    private String articleTypeId;

    private String parentId;

    @NotBlank
    private String name;

    private String nameKh;

    /** Auto-generated from name when omitted. */
    private String slug;

    private String description;

    private String descriptionKh;

    private String icon;

    private Integer displayOrder = 0;
}
