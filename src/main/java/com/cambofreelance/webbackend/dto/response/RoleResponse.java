package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.RoleEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("roleId")
    private String roleId;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("roleName")
    private String roleName;

    @JsonProperty("convention")
    private String convention;

    @JsonProperty("level")
    private Integer level;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private String status;

    @JsonProperty("createdAt")
    private Date createdAt;

    @JsonProperty("updatedAt")
    private Date updatedAt;

    @JsonProperty("permissions")
    private List<PermissionResponse> permissions;

    @JsonProperty("permissionCount")
    private int permissionCount;

    public static RoleResponse from(RoleEntity e) {
        List<PermissionResponse> perms = e.getPermissions() == null ? List.of() :
            e.getPermissions().stream()
                .sorted((a, b) -> Integer.compare(
                    a.getSortOrder() == null ? 0 : a.getSortOrder(),
                    b.getSortOrder() == null ? 0 : b.getSortOrder()))
                .map(PermissionResponse::from)
                .collect(Collectors.toList());

        return RoleResponse.builder()
            .id(e.getId())
            .roleId(e.getId())
            .code(e.getCode())
            .name(e.getName())
            .roleName(e.getName())
            .convention(e.getConvention())
            .level(e.getLevel())
            .description(e.getDescription())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .permissions(perms)
            .permissionCount(perms.size())
            .build();
    }
}
