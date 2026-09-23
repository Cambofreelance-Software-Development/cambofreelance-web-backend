package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.TaxonomyItemEntity;
import lombok.Builder;
import lombok.Data;

/**
 * Minimal shape for the public GET /taxonomy-items?taxonomyCode=... endpoint — only what a
 * public form's <select> needs, no audit/status fields.
 */
@Data
@Builder
public class TaxonomyItemPublicResponse {

    private String code;
    private String displayEn;
    private String displayKm;

    public static TaxonomyItemPublicResponse from(TaxonomyItemEntity e) {
        return TaxonomyItemPublicResponse.builder()
            .code(e.getCode())
            .displayEn(e.getDisplayEn())
            .displayKm(e.getDisplayKm())
            .build();
    }
}
