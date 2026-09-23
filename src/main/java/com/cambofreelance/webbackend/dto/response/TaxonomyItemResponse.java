package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.TaxonomyItemEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaxonomyItemResponse {

    private String id;
    private String code;
    private String taxonomyCode;
    private String parentCode;
    private String displayEn;
    private String displayKm;
    private String metadata;
    private Integer sortOrder;
    private String status;
    private String userId;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;

    public static TaxonomyItemResponse from(TaxonomyItemEntity e) {
        return TaxonomyItemResponse.builder()
            .id(e.getId())
            .code(e.getCode())
            .taxonomyCode(e.getTaxonomyCode())
            .parentCode(e.getParentCode())
            .displayEn(e.getDisplayEn())
            .displayKm(e.getDisplayKm())
            .metadata(e.getMetadata())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .userId(e.getCreatedBy())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .deletedAt(null)
            .build();
    }
}
