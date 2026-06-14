package com.cambofreelance.authenticationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionMetadataDto {
    private String ipAddress;
    private String userAgent;
    private String browser;
    private String osName;
    private String deviceType;
    private String deviceName;
    private boolean newDevice;
}
