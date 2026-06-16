package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.InvoiceEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("tenantId")
    private String tenantId;

    @JsonProperty("tenantName")
    private String tenantName;

    @JsonProperty("subscriptionId")
    private String subscriptionId;

    @JsonProperty("packageId")
    private String packageId;

    @JsonProperty("packageName")
    private String packageName;

    @JsonProperty("billingPeriod")
    private Date billingPeriod;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("tax")
    private BigDecimal tax;

    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("status")
    private String status;

    @JsonProperty("paidAt")
    private Date paidAt;

    @JsonProperty("createdAt")
    private Date createdAt;

    public static InvoiceResponse from(InvoiceEntity e, String tenantName, String packageName) {
        return InvoiceResponse.builder()
            .id(e.getId())
            .tenantId(e.getTenantId())
            .tenantName(tenantName)
            .subscriptionId(e.getSubscriptionId())
            .packageId(e.getPackageId())
            .packageName(packageName)
            .billingPeriod(e.getBillingPeriod())
            .amount(e.getAmount())
            .tax(e.getTax())
            .totalAmount(e.getTotalAmount())
            .currency(e.getCurrency())
            .status(e.getInvoiceStatus())
            .paidAt(e.getPaidAt())
            .createdAt(e.getCreatedAt())
            .build();
    }
}
