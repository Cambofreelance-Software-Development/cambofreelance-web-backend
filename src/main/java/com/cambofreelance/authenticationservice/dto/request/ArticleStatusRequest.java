package com.cambofreelance.authenticationservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArticleStatusRequest {

    @NotBlank
    private String status;
}
