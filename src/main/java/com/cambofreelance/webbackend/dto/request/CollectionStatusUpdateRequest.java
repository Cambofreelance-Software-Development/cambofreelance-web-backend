package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CollectionStatusUpdateRequest {

    @NotBlank
    private String collectionStatus;

    private String remarks;
}
