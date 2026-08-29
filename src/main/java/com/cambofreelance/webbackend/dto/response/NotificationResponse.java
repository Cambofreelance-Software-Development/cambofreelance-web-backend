package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.AdminNotificationEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponse {

    private String id;
    private String type;
    private String title;
    private String message;
    private String referenceId;
    private String referenceType;
    private Boolean isRead;
    private Date createdAt;

    public static NotificationResponse from(AdminNotificationEntity e) {
        return NotificationResponse.builder()
            .id(e.getId())
            .type(e.getType())
            .title(e.getTitle())
            .message(e.getMessage())
            .referenceId(e.getReferenceId())
            .referenceType(e.getReferenceType())
            .isRead(e.getIsRead())
            .createdAt(e.getCreatedAt())
            .build();
    }
}
