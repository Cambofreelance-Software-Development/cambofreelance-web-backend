package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.Date;
import lombok.Data;

@Data
public class InventoryDocumentUploadRequest {

    @NotBlank(message = "Owner type is required")
    private String ownerType; // CUSTOMER, SALE, FINANCING_APPLICATION, INVENTORY_ITEM, PRODUCT, VARIANT

    @NotBlank(message = "Owner ID is required")
    private String ownerId;

    private String mediaId;

    @NotBlank(message = "Document type is required")
    private String documentType;

    @NotBlank(message = "Document name is required")
    private String documentName;

    private String fileUrl;
    private String mimeType;
    private Long fileSize;
    private Date expiresAt;
}
