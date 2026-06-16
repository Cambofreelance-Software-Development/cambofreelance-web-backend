package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.SubscriptionEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("tenantName")
    private String tenantName;

    @JsonProperty("packageId")
    private String packageId;

    @JsonProperty("packageName")
    private String packageName;

    @JsonProperty("billingCycle")
    private String billingCycle;

    @JsonProperty("startDate")
    private Date startDate;

    @JsonProperty("endDate")
    private Date endDate;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("paymentStatus")
    private String paymentStatus;

    @JsonProperty("autoRenewal")
    private boolean autoRenewal;

    @JsonProperty("status")
    private String status;

    @JsonProperty("createdAt")
    private Date createdAt;

    @JsonProperty("updatedAt")
    private Date updatedAt;

    public static SubscriptionResponse from(SubscriptionEntity e, String tenantName, String packageName) {
        return SubscriptionResponse.builder()
            .id(e.getId())
            .tenantId(e.getTenantId())
            .tenantName(tenantName)
            .packageId(e.getPackageId())
            .packageName(packageName)
            .billingCycle(e.getBillingCycle())
            .startDate(e.getStartDate())
            .endDate(e.getEndDate())
            .amount(e.getAmount())
            .currency(e.getCurrency())
            .paymentStatus(e.getPaymentStatus())
            .autoRenewal(e.isAutoRenewal())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
