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
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public static CategoryHardwareResponse from(CategoryHardwareEntity e) {
        return CategoryHardwareResponse.builder()
            .id(e.getId())
            .name(e.getName())
            .nameKh(e.getNameKh())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
