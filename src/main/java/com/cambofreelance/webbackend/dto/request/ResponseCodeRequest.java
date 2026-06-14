package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResponseCodeRequest {

    @NotBlank(message = "Code is required")
    private String code;

    private String description;

    @NotBlank(message = "httpStatus is required")
    private String httpStatus;

    private String key;

    /** SUC | ERR */
    @Pattern(regexp = "SUC|ERR", message = "type must be SUC or ERR")
    private String type;

    @NotBlank(message = "messageEn is required")
    private String messageEn;

    private String messageKm;

    private String messageCn;

    private String serviceType;
}
