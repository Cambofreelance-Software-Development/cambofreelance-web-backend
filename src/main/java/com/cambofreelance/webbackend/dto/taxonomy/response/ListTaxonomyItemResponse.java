package com.cambofreelance.webbackend.dto.taxonomy.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ListTaxonomyItemResponse {
    private String code;
    private String taxonomyCode;
    private String parentCode;
    private String displayKm;
    private String displayEn;
    private String metadata;
}
