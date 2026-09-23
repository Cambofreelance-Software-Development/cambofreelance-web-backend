package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.TaxonomyEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaxonomyResponse {

    private String id;
    private String code;
    private String name;
    private Boolean isHierarchical;
    private String description;
    private String remark;
    private String status;
    private String userId;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    public static TaxonomyResponse from(TaxonomyEntity e) {
        return TaxonomyResponse.builder()
            .id(e.getId())
            .code(e.getCode())
            .name(e.getName())
            .isHierarchical(e.getIsHierarchical())
            .description(e.getDescription())
            .remark(e.getRemark())
            .status(e.getStatus())
            .userId(e.getCreatedBy())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .deletedAt(null)
            .build();
    }
}
