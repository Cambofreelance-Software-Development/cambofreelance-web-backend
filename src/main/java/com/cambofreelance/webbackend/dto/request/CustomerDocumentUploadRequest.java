package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerDocumentUploadRequest {

    @NotBlank(message = "Media id is required")
    private String mediaId;

    @NotBlank(message = "Document type is required")
    private String documentType;
}
