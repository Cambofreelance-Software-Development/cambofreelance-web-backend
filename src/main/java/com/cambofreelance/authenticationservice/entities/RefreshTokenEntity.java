package com.cambofreelance.authenticationservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table(name = "USER_REFRESH_TOKENS")
@Entity
@Getter
@Setter
@ToString
public class RefreshTokenEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;
    @Column(name = "ACCESS_TOKEN")
    private String accessToken;
    @Column(name = "EXPIRE_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredOn;
    @Column(name = "DEVICE_ID")
    private String deviceId;
    @Column(name = "USER_ID")
    private String userId;
    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;
    @Column(name = "device_name")
    private String deviceName;
    @Column(name = "device_type")
    private String deviceType;
    @Column(name = "browser")
    private String browser;
    @Column(name = "os_name")
    private String osName;
    @Column(name = "last_active_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActiveAt;
    @Column(name = "is_trusted")
    private boolean trusted;
    @Column(name = "is_new_device")
    private boolean newDevice;

}
