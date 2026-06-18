package com.cambofreelance.webbackend.dto.taxonomy.response;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TaxonomyItemResponseDTO {
    private String code;
    private String parentCode;
    private String taxonomyCode;
    private String displayKm;
    private String displayEn;
    private String metadata;
}
