package com.cambofreelance.authenticationservice.dto.response;

import com.cambofreelance.authenticationservice.entities.AuditLogEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditLogResponse {

    private String id;
    private String userId;
    private String username;
    private String action;
    private String module;
    private String entityId;
    private String description;
    private String oldValue;
    private String newValue;
    private String ipAddress;
    private Date createdAt;

    public static AuditLogResponse from(AuditLogEntity e) {
        return AuditLogResponse.builder()
            .id(e.getId())
            .userId(e.getUserId())
            .username(e.getUsername())
            .action(e.getAction())
            .module(e.getModule())
            .entityId(e.getEntityId())
            .description(e.getDescription())
            .oldValue(e.getOldValue())
            .newValue(e.getNewValue())
            .ipAddress(e.getIpAddress())
            .createdAt(e.getCreatedAt())
            .build();
    }
}
