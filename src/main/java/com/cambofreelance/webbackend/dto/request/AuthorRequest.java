package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthorRequest {

    @NotBlank
    private String name;

    private String nameKh;

    private String bio;

    private String email;

    private String avatarId;

    private Integer sortOrder = 0;
}
