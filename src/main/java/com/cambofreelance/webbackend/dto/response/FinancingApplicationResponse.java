package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.FinancingApplicationEntity;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FinancingApplicationResponse {

    private String id;
    private String applicationNo;
    private String saleId;
    private String saleNo;
    private String customerId;
    private String customerName;
    private String bankId;
    private String bankName;
    private String bankLogoUrl;

    private BigDecimal requestedAmount;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private Integer termMonths;
    private BigDecimal monthlyInstallment;

    private String status;
    private String externalReference;
    private String guarantorCustomerId;
    private String guarantorCustomerName;

    private Date submittedAt;
    private Date approvedAt;
    private Date rejectedAt;
    private String rejectionReason;

    private Date createdAt;
    private String createdBy;

    public static FinancingApplicationResponse from(
        FinancingApplicationEntity e,
        String saleNo,
        String customerName,
        String bankName,
        String bankLogoUrl,
        String guarantorName
    ) {
        return FinancingApplicationResponse.builder()
            .id(e.getId())
            .applicationNo(e.getApplicationNo())
            .saleId(e.getSaleId())
            .saleNo(saleNo)
            .customerId(e.getCustomerId())
            .customerName(customerName)
            .bankId(e.getBankId())
            .bankName(bankName)
            .bankLogoUrl(bankLogoUrl)
            .requestedAmount(e.getRequestedAmount())
            .approvedAmount(e.getApprovedAmount())
            .interestRate(e.getInterestRate())
            .termMonths(e.getTermMonths())
            .monthlyInstallment(e.getMonthlyInstallment())
            .status(e.getStatus())
            .externalReference(e.getExternalReference())
            .guarantorCustomerId(e.getGuarantorCustomerId())
            .guarantorCustomerName(guarantorName)
            .submittedAt(e.getSubmittedAt())
            .approvedAt(e.getApprovedAt())
            .rejectedAt(e.getRejectedAt())
            .rejectionReason(e.getRejectionReason())
            .createdAt(e.getCreatedAt())
            .createdBy(e.getCreatedBy())
            .build();
    }
}
