package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Used for both create and update — the row is identified by {@code taxonomyCode} + {@code code}.
 */
@Data
public class TaxonomyItemRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String taxonomyCode;

    private String parentCode;

    @NotBlank
    private String displayEn;

    private String displayKm;

    private String metadata;
}
