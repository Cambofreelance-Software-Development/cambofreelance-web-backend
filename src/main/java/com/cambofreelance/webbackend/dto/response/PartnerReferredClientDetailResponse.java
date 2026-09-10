package com.cambofreelance.webbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

/** Full detail behind "View details" on a partner's Referred Clients row. */
@Data
@Builder
public class PartnerReferredClientDetailResponse {

    private String userId;
    private String username;
    private String email;
    private String phoneNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date joinedDate;

    // Company profile — null if the client hasn't filled in Company Info yet.
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String address;
    private String city;
    private String country;
    private String businessType;
    private String logoUrl;
    private String clientStatus;

    // Current subscription — null if the client has never subscribed.
    private String planId;
    private String planName;
    private String billingCycle;
    private BigDecimal price;
    private String currency;
    private String subStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date startAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date expiresAt;

    private boolean autoRenew;

    // Commission attribution — basis for the amount shown in the referred-clients table.
    private BigDecimal totalPaid;
    private BigDecimal commissionRate;
    private BigDecimal commission;
}
