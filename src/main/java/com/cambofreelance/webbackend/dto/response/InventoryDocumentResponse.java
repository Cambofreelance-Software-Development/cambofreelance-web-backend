package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.InventoryDocumentEntity;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryDocumentResponse {

    private String id;
    private String ownerType;
    private String ownerId;
    private String mediaId;
    private String documentType;
    private String documentName;
    private String fileUrl;
    private String mimeType;
    private Long fileSize;
    private String documentStatus;
    private Integer version;
    private Date expiresAt;
    private String verifiedBy;
    private Date verifiedAt;
    private Date createdAt;
    private String createdBy;

    public static InventoryDocumentResponse from(InventoryDocumentEntity e) {
        return InventoryDocumentResponse.builder()
            .id(e.getId())
            .ownerType(e.getOwnerType())
            .ownerId(e.getOwnerId())
            .mediaId(e.getMediaId())
            .documentType(e.getDocumentType())
            .documentName(e.getDocumentName())
            .fileUrl(e.getFileUrl())
            .mimeType(e.getMimeType())
            .fileSize(e.getFileSize())
            .documentStatus(e.getDocumentStatus())
            .version(e.getVersion())
            .expiresAt(e.getExpiresAt())
            .verifiedBy(e.getVerifiedBy())
            .verifiedAt(e.getVerifiedAt())
            .createdAt(e.getCreatedAt())
            .createdBy(e.getCreatedBy())
            .build();
    }
}
