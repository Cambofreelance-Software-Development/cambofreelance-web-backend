package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.ResponseCodeEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseCodeResponse {

    private Long id;
    private String code;
    private String description;
    private String httpStatus;
    private String key;
    private String type;
    private String messageEn;
    private String messageKm;
    private String messageCn;
    private String serviceType;
    private String status;
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;

    public static ResponseCodeResponse from(ResponseCodeEntity e) {
        return ResponseCodeResponse.builder()
            .id(e.getId())
            .code(e.getCode())
            .description(e.getDescription())
            .httpStatus(e.getHttpStatus())
            .key(e.getKey())
            .type(e.getType())
            .messageEn(e.getMessageEn())
            .messageKm(e.getMessageKm())
            .messageCn(e.getMessageCn())
            .serviceType(e.getServiceType())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .createdBy(e.getCreatedBy())
            .updatedAt(e.getUpdatedAt())
            .updatedBy(e.getUpdatedBy())
            .build();
    }
}
