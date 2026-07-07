package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.HardwareEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HardwareResponse {

    private String id;
    private String name;
    private String nameKh;
    private String brand;
    private String description;
    private String descriptionKh;
    private String connectivity;
    private String platform;
    private CategoryHardwareResponse category;
    private MediaFileResponse image;
    private String icon;
    private String link;
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public static HardwareResponse from(HardwareEntity e) {
        return HardwareResponse.builder()
            .id(e.getId())
            .name(e.getName())
            .nameKh(e.getNameKh())
            .brand(e.getBrand())
            .description(e.getDescription())
            .descriptionKh(e.getDescriptionKh())
            .connectivity(e.getConnectivity())
            .platform(e.getPlatform())
            .category(e.getCategory() != null ? CategoryHardwareResponse.from(e.getCategory()) : null)
            .image(e.getImage() != null ? MediaFileResponse.from(e.getImage()) : null)
            .icon(e.getIcon())
            .link(e.getLink())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
