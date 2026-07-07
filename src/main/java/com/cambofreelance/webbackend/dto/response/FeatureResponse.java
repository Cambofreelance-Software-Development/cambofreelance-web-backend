package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.FeatureEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeatureResponse {

    private String id;
    private String title;
    private String titleKh;
    private String description;
    private String descriptionKh;
    private CategoryFeatureResponse category;
    private String icon;
    private MediaFileResponse image;
    private String link;
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public static FeatureResponse from(FeatureEntity e) {
        return FeatureResponse.builder()
            .id(e.getId())
            .title(e.getTitle())
            .titleKh(e.getTitleKh())
            .description(e.getDescription())
            .descriptionKh(e.getDescriptionKh())
            .category(e.getCategory() != null ? CategoryFeatureResponse.from(e.getCategory()) : null)
            .icon(e.getIcon())
            .image(e.getImage() != null ? MediaFileResponse.from(e.getImage()) : null)
            .link(e.getLink())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
