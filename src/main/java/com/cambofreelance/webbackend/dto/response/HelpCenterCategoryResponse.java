package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.HelpCenterCategoryEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HelpCenterCategoryResponse {

    private String id;
    private String articleTypeId;
    private String parentId;
    private String name;
    private String nameKh;
    private String slug;
    private String description;
    private String descriptionKh;
    private String icon;
    private Integer displayOrder;
    private String status;
    private Date createdAt;

    public static HelpCenterCategoryResponse from(HelpCenterCategoryEntity e) {
        return HelpCenterCategoryResponse.builder()
            .id(e.getId())
            .articleTypeId(e.getArticleTypeId())
            .parentId(e.getParentId())
            .name(e.getName())
            .nameKh(e.getNameKh())
            .slug(e.getSlug())
            .description(e.getDescription())
            .descriptionKh(e.getDescriptionKh())
            .icon(e.getIcon())
            .displayOrder(e.getDisplayOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .build();
    }
}
