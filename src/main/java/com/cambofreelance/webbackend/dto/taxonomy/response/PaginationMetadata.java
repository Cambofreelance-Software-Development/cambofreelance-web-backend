package com.cambofreelance.webbackend.dto.taxonomy.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationMetadata {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean hasPrevious;
    private boolean hasNext;
}

