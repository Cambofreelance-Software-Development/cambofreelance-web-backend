package com.cambofreelance.webbackend.dto.taxonomy.response;

import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaxonomyItemResponse {
    private String code;
    private String parentCode;
    private String taxonomyCode;
    private String displayKm;
    private String displayEn;
    private String metadata;
    private UUID userId;
    private Date updatedAt;
}
