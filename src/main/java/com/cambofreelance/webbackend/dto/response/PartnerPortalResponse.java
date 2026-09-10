package com.cambofreelance.webbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class PartnerPortalResponse {

    private String partnerRef;
    private String appStatus;

    /** BRONZE / SILVER / GOLD — derived live from active referred subscriptions. */
    private String tier;

    /** Commission rate for the current tier, e.g. 0.25. */
    private BigDecimal commissionRate;

    /** Public registration link carrying this partner's ref. */
    private String referralLink;

    private String payoutChannel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date activatedAt;

    private Stats stats;

    private List<ReferredClient> referredClients;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Stats {
        private long totalReferredClients;
        private long activeSubscriptions;
        /** activeSubscriptions / totalReferredClients as a percentage (0–100), 0 when no referrals. */
        private BigDecimal conversionRate;
        private BigDecimal commissionEarned;
        private BigDecimal totalPaidOut;
        private BigDecimal pendingPayout;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReferredClient {
        private String userId;
        private String username;
        private String companyName;
        private String city;
        private String businessType;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
        private Date joinedDate;

        private String planName;
        /** Latest subscription status, or null if they never subscribed. */
        private String subStatus;
        /** This client's attributed approved payments × the partner's current rate. */
        private BigDecimal commission;
    }
}
