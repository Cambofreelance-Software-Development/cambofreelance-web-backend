package com.cambofreelance.webbackend.dto.taxonomy.request;


import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxonomyItemFilterRequest {
    private List<FilterRequest> filter;
    private String sortBy;
    private String sortDirection;
    private String ifModifiedSince;
}
