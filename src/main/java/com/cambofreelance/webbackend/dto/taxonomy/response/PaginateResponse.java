package com.cambofreelance.webbackend.dto.taxonomy.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginateResponse<T> {
    private List<T> content;
    private PaginationMetadata metadata;
}
