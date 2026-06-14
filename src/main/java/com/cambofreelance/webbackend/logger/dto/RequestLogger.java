package com.cambofreelance.webbackend.logger.dto;

import com.cambofreelance.webbackend.logger.utils.LoggerUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestLogger {

    String path;
    String request;
    String response;
    String duration;
    String httpMethod;

    public void setRequest(Object request) {
        this.request = LoggerUtils.toJsonString(request);
    }

    public void setResponse(Object response) {
        this.response = LoggerUtils.toJsonString(response);
    }
}
