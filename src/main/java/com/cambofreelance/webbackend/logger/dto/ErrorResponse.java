package com.cambofreelance.webbackend.logger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    @JsonProperty("messageKm")
    private String messageKm;

    @JsonProperty("messageEn")
    private String messageEn;

    @JsonProperty("messageCh")
    private String messageCh;

    @JsonProperty("httpStatus")
    private String httpStatus;
}
