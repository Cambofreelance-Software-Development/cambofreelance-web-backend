package com.cambofreelance.webbackend.dto.taxonomy.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaxonomyResponseDTO {
    private String code;
    private String name;
    private Boolean isHierarchical;
    private String description;
    private String remark;

}
