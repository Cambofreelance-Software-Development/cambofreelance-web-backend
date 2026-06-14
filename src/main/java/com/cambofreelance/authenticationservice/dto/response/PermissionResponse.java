package com.cambofreelance.authenticationservice.dto.response;

import com.cambofreelance.authenticationservice.entities.PermissionEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionResponse {

    private String id;
    private String code;
    private String name;
    private String groupName;
    private String description;
    private Integer sortOrder;

    public static PermissionResponse from(PermissionEntity e) {
        return PermissionResponse.builder()
            .id(e.getId())
            .code(e.getCode())
            .name(e.getName())
            .groupName(e.getGroupName())
            .description(e.getDescription())
            .sortOrder(e.getSortOrder())
            .build();
    }
}
