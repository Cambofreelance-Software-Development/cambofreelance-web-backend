package com.cambofreelance.webbackend.dto.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReferralStatsResponse {

    private String referralCode;

    /** Shareable registration link pre-filled with this user's referral code. */
    private String referralLink;

    /** Count of accounts registered with this user's referral code. */
    private long totalReferred;

    /** Count of those referred accounts that have gone on to create a subscription. */
    private long totalSubscribed;

    /** Sum of APPROVED payment transactions carrying this user's referrerId — basis for future commission calc. */
    private BigDecimal totalRevenue;
}
