package com.cambofreelance.webbackend.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/** Admin drawer payload: the application plus its live commission stats and recorded payouts. */
@Data
@Builder
public class PartnerAdminDetailResponse {

    private PartnerApplicationResponse application;

    /** BRONZE / SILVER / GOLD — live, even before approval (0 active → BRONZE). */
    private String tier;

    private java.math.BigDecimal commissionRate;

    private PartnerPortalResponse.Stats stats;

    private List<PartnerPayoutResponse> payouts;
}
