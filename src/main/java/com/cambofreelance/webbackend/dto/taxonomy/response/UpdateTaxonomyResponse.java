package com.cambofreelance.webbackend.dto.taxonomy.response;

import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaxonomyResponse {
    private String code;
    private String name;
    private Boolean isHierarchical;
    private String description;
    private String remark;
    private UUID userId;
    private Date updatedAt;
}
