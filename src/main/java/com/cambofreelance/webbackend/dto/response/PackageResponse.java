package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.SubscriptionPackageEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PackageResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("monthlyPrice")
    private BigDecimal monthlyPrice;

    @JsonProperty("customPricing")
    private boolean customPricing;

    @JsonProperty("maxCustomers")
    private Integer maxCustomers;

    @JsonProperty("maxLoans")
    private Integer maxLoans;

    @JsonProperty("maxUsers")
    private Integer maxUsers;

    @JsonProperty("description")
    private String description;

    @JsonProperty("sortOrder")
    private Integer sortOrder;

    @JsonProperty("status")
    private String status;

    @JsonProperty("features")
    private Map<String, Boolean> features;

    @JsonProperty("createdAt")
    private Date createdAt;

    @JsonProperty("updatedAt")
    private Date updatedAt;

    public static PackageResponse from(SubscriptionPackageEntity e, Map<String, Boolean> features) {
        return PackageResponse.builder()
            .id(e.getId())
            .code(e.getCode())
            .name(e.getName())
            .monthlyPrice(e.getMonthlyPrice())
            .customPricing(e.isCustomPricing())
            .maxCustomers(e.getMaxCustomers())
            .maxLoans(e.getMaxLoans())
            .maxUsers(e.getMaxUsers())
            .description(e.getDescription())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .features(features)
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
