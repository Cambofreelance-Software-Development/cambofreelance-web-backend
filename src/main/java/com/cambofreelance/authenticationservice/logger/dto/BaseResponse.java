package com.cambofreelance.authenticationservice.logger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse<T> {

    private boolean success;
    private long timestamp;
    private String code;
    private String message;
    private T data;
    private String traceId;
}
