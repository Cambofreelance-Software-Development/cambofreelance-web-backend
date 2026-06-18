package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.CustomerDocumentEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDocumentResponse {

    private String id;
    private String customerId;
    private String documentType;
    private String mediaId;
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private String mimeType;
    private String createdBy;
    private Date createdAt;

    public static CustomerDocumentResponse from(CustomerDocumentEntity doc, MediaFileResponse media) {
        return CustomerDocumentResponse.builder()
            .id(doc.getId())
            .customerId(doc.getCustomerId())
            .documentType(doc.getDocumentType())
            .mediaId(doc.getMediaId())
            .fileName(media != null ? media.getFileName() : null)
            .fileUrl(media != null ? media.getFileUrl() : null)
            .fileSize(media != null ? media.getFileSize() : null)
            .mimeType(media != null ? media.getMimeType() : null)
            .createdBy(doc.getCreatedBy())
            .createdAt(doc.getCreatedAt())
            .build();
    }
}
