package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.HomeProductEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HomeProductResponse {

    private String id;
    private String name;
    private String nameKh;
    private String description;
    private String descriptionKh;
    private String icon;
    private String iconBg;
    private String href;
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public static HomeProductResponse from(HomeProductEntity e) {
        return HomeProductResponse.builder()
            .id(e.getId())
            .name(e.getName())
            .nameKh(e.getNameKh())
            .description(e.getDescription())
            .descriptionKh(e.getDescriptionKh())
            .icon(e.getIcon())
            .iconBg(e.getIconBg())
            .href(e.getHref())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
