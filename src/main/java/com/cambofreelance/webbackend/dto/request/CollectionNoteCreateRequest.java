package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CollectionNoteCreateRequest {

    @NotBlank
    private String noteType;

    @NotBlank
    private String content;

    private String contactPerson;

    private String contactResult;
}
