package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.TenantEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TenantResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("tenantType")
    private String tenantType;

    @JsonProperty("description")
    private String description;

    @JsonProperty("logoUrl")
    private String logoUrl;

    @JsonProperty("primaryColor")
    private String primaryColor;

    @JsonProperty("secondaryColor")
    private String secondaryColor;

    @JsonProperty("companyName")
    private String companyName;

    @JsonProperty("companyAddress")
    private String companyAddress;

    @JsonProperty("companyEmail")
    private String companyEmail;

    @JsonProperty("companyPhone")
    private String companyPhone;

    @JsonProperty("website")
    private String website;

    @JsonProperty("planStartDate")
    private Date planStartDate;

    @JsonProperty("planExpiredDate")
    private Date planExpiredDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("rejectionReason")
    private String rejectionReason;

    @JsonProperty("createdAt")
    private Date createdAt;

    @JsonProperty("updatedAt")
    private Date updatedAt;

    public static TenantResponse from(TenantEntity e) {
        return TenantResponse.builder()
            .id(e.getId())
            .code(e.getCode())
            .name(e.getName())
            .tenantType(e.getTenantType())
            .description(e.getDescription())
            .logoUrl(e.getLogoUrl())
            .primaryColor(e.getPrimaryColor())
            .secondaryColor(e.getSecondaryColor())
            .companyName(e.getCompanyName())
            .companyAddress(e.getCompanyAddress())
            .companyEmail(e.getCompanyEmail())
            .companyPhone(e.getCompanyPhone())
            .website(e.getWebsite())
            .planStartDate(e.getPlanStartDate())
            .planExpiredDate(e.getPlanExpiredDate())
            .status(e.getStatus())
            .rejectionReason(e.getRejectionReason())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
