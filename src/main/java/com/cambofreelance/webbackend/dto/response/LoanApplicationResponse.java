package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.LoanApplicationEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanApplicationResponse {

    private String id;
    private String loanNumber;
    private String customerId;
    private String customerCode;
    private String customerFirstName;
    private String customerLastName;
    private BigDecimal loanAmount;
    private String currency;
    private Integer tenorValue;
    private String tenorUnit;
    private String paymentType;
    private BigDecimal interestRate;
    private BigDecimal serviceFeeRate;
    private BigDecimal serviceFeeAmount;
    private BigDecimal interestAmount;
    private BigDecimal netDisbursement;
    private Date applicationDate;
    private Date firstPaymentDate;
    private String purpose;
    private String loanOfficerId;
    private String loanOfficerName;
    private String applicationStatus;
    private String submittedBy;
    private String submittedByName;
    private Date submittedAt;
    private String approvedBy;
    private String approvedByName;
    private Date approvedAt;
    private String approvalNote;
    private String rejectedBy;
    private String rejectedByName;
    private Date rejectedAt;
    private String rejectionReason;
    private String status;
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;

    public static LoanApplicationResponse from(LoanApplicationEntity e, Map<String, String> userNames) {
        return LoanApplicationResponse.builder()
            .id(e.getId())
            .loanNumber(e.getLoanNumber())
            .customerId(e.getCustomerId())
            .customerCode(e.getCustomerCode())
            .customerFirstName(e.getCustomerFirstName())
            .customerLastName(e.getCustomerLastName())
            .loanAmount(e.getLoanAmount())
            .currency(e.getCurrency())
            .tenorValue(e.getTenorValue())
            .tenorUnit(e.getTenorUnit())
            .paymentType(e.getPaymentType())
            .interestRate(e.getInterestRate())
            .serviceFeeRate(e.getServiceFeeRate())
            .serviceFeeAmount(e.getServiceFeeAmount())
            .interestAmount(e.getInterestAmount())
            .netDisbursement(e.getNetDisbursement())
            .applicationDate(e.getApplicationDate())
            .firstPaymentDate(e.getFirstPaymentDate())
            .purpose(e.getPurpose())
            .loanOfficerId(e.getLoanOfficerId())
            .loanOfficerName(userNames.get(e.getLoanOfficerId()))
            .applicationStatus(e.getApplicationStatus())
            .submittedBy(e.getSubmittedBy())
            .submittedByName(userNames.get(e.getSubmittedBy()))
            .submittedAt(e.getSubmittedAt())
            .approvedBy(e.getApprovedBy())
            .approvedByName(userNames.get(e.getApprovedBy()))
            .approvedAt(e.getApprovedAt())
            .approvalNote(e.getApprovalNote())
            .rejectedBy(e.getRejectedBy())
            .rejectedByName(userNames.get(e.getRejectedBy()))
            .rejectedAt(e.getRejectedAt())
            .rejectionReason(e.getRejectionReason())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .createdBy(e.getCreatedBy())
            .updatedAt(e.getUpdatedAt())
            .updatedBy(e.getUpdatedBy())
            .build();
    }
}
