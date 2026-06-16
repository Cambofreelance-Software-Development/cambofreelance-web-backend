package com.cambofreelance.webbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsageMetricsResponse {

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("packageName")
    private String packageName;

    @JsonProperty("totalCustomers")
    private UsageMetric totalCustomers;

    @JsonProperty("totalLoans")
    private UsageMetric totalLoans;

    @JsonProperty("activeUsers")
    private UsageMetric activeUsers;

    /** Bytes */
    @JsonProperty("storageUsage")
    private UsageMetric storageUsage;

    /** Monthly authenticated request count */
    @JsonProperty("apiRequests")
    private UsageMetric apiRequests;
}
