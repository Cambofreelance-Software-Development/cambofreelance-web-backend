package com.cambofreelance.webbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionResponse {

    private String id;
    private String userId;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date createdAt;

    private boolean autoRenew;
    /** Whether a Card-on-File token is stored — the raw token itself is never serialized. */
    private boolean hasPaymentToken;
    private Integer autoRenewFailureCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date paymentTokenCapturedAt;
}
