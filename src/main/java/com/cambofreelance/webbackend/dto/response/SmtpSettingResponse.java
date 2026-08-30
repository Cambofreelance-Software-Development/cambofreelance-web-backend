package com.cambofreelance.webbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmtpSettingResponse {
    private String host;
    private Integer port;
    private String username;
    private Boolean hasPassword;
    private String fromEmail;
    private String fromName;
    private String encryption;
    private Boolean auth;
}
