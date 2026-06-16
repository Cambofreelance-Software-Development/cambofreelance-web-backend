package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.PermissionEntity;
import com.cambofreelance.webbackend.entities.RoleEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TenantRoleResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

    @JsonProperty("permissions")
    private Set<String> permissions;

    public static TenantRoleResponse from(RoleEntity entity) {
        return TenantRoleResponse.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .status(entity.getStatus())
            .permissions(entity.getPermissions().stream().map(PermissionEntity::getCode).collect(Collectors.toSet()))
            .build();
    }
}
