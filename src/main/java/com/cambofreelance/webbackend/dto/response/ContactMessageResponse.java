package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.ContactMessageEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactMessageResponse {

    private String id;
    private String fullName;
    private String email;
    private String subject;
    private String message;
    private String ipAddress;
    private Boolean isRead;
    private Date createdAt;

    public static ContactMessageResponse from(ContactMessageEntity e) {
        return ContactMessageResponse.builder()
            .id(e.getId())
            .fullName(e.getFullName())
            .email(e.getEmail())
            .subject(e.getSubject())
            .message(e.getMessage())
            .ipAddress(e.getIpAddress())
            .isRead(e.getIsRead())
            .createdAt(e.getCreatedAt())
            .build();
    }
}
