package com.cambofreelance.webbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionDashboardResponse {

    @JsonProperty("activeSubscriptions")
    private long activeSubscriptions;

    /** Active subscriptions expiring within 30 days */
    @JsonProperty("expiringSubscriptions")
    private long expiringSubscriptions;

    @JsonProperty("monthlyRecurringRevenue")
    private BigDecimal monthlyRecurringRevenue;

    @JsonProperty("totalTenants")
    private long totalTenants;

    /** Package name -> count of active subscriptions */
    @JsonProperty("packageDistribution")
    private Map<String, Long> packageDistribution;

    /** Package name -> monthly-equivalent revenue from active subscriptions */
    @JsonProperty("revenueByPackage")
    private Map<String, BigDecimal> revenueByPackage;

    /** Percentage: active / (active + expired) among subscriptions that have completed at least one cycle */
    @JsonProperty("renewalRate")
    private double renewalRate;

    /** Active subscriptions expiring within the next 7 days */
    @JsonProperty("expiryAlerts")
    private List<SubscriptionResponse> expiryAlerts;
}
