package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FaqRequest {

    @NotBlank
    private String question;

    private String questionKh;

    @NotBlank
    private String answer;

    private String answerKh;

    private Integer sortOrder = 0;
}
