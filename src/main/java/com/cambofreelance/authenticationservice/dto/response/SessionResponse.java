package com.cambofreelance.authenticationservice.dto.response;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionResponse {
    private String sessionId;
    private String deviceId;
    private String deviceName;
    private String deviceType;
    private String browser;
    private String os;
    private String ipAddress;
    private Date lastActiveAt;
    private Date createdAt;
    private boolean trusted;
    private boolean current;
}
