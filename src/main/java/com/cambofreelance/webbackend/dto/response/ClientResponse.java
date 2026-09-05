package com.cambofreelance.webbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientResponse {

    private String id;
    private String userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String approvalStatus;
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String address;
    private String city;
    private String country;
    private String businessType;
    private String logoUrl;
    private Boolean emailVerified;
    private String clientStatus;
    private String onboardingStep;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date createdAt;

    /** Status/expiry of this client's most recent subscription (null if they've never subscribed). */
    private String subStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date expiresAt;
}
