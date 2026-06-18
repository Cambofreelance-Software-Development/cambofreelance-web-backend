package com.cambofreelance.webbackend.dto.taxonomy.response;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailTaxonomyResponse {
    private String code;
    private String name;
    private Boolean isHierarchical;
    private String description;
    private String remark;
    private String userId;
    private Date createdAt;
}
