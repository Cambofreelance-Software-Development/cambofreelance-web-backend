package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Used for both create and update — the row is identified by {@code code}, not an id.
 */
@Data
public class TaxonomyRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private Boolean isHierarchical = false;

    private String description;

    private String remark;
}
