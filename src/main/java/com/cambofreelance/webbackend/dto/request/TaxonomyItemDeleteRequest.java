package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * {@code taxonomyCode} is optional: the current frontend's deleteTaxonomyItem() only posts
 * {@code { code }}. When it is omitted the service falls back to looking the item up by
 * code alone (disambiguating on taxonomyCode if more than one taxonomy happens to reuse
 * the same item code).
 */
@Data
public class TaxonomyItemDeleteRequest {

    @NotBlank
    private String code;

    private String taxonomyCode;
}
