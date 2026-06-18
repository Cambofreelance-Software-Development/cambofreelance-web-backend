package com.cambofreelance.webbackend.dto.taxonomy.request;

import jakarta.validation.Valid;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListAllTaxonomyRequest {
    private List<FilterRequest> filter;

    private String sortBy;
    private String sortDirection;

    @Valid
    private PaginationRequest paginate = new PaginationRequest(); // Ensure defaults

    private String search;
}
