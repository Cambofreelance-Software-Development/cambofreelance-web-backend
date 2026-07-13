package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.CategoryHardwareEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryHardwareResponse {

    private String id;
    private String name;
    private String nameKh;
    private String description;
    private String descriptionKh;
    private MediaFileResponse image;
    private String icon;
    private String moreLink;
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public static CategoryHardwareResponse from(CategoryHardwareEntity e) {
        return CategoryHardwareResponse.builder()
            .id(e.getId())
            .name(e.getName())
            .nameKh(e.getNameKh())
            .description(e.getDescription())
            .descriptionKh(e.getDescriptionKh())
            .image(e.getImage() != null ? MediaFileResponse.from(e.getImage()) : null)
            .icon(e.getIcon())
            .moreLink(e.getMoreLink())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
