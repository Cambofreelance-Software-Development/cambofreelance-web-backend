package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.CollectionCaseEntity;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectionCaseResponse {

    private String id;
    private String loanApplicationId;
    private String loanNumber;
    private String currency;
    private String customerId;
    private String customerCode;
    private String customerFirstName;
    private String customerLastName;
    private String collectionStatus;
    private String assignedOfficerId;
    private int dpd;
    private BigDecimal totalOverdueAmount;
    private BigDecimal penaltyRate;
    private BigDecimal penaltyAmount;
    private String remarks;
    private Date createdAt;
    private Date updatedAt;

    public static CollectionCaseResponse from(CollectionCaseEntity e) {
        return CollectionCaseResponse.builder()
            .id(e.getId())
            .loanApplicationId(e.getLoanApplicationId())
            .loanNumber(e.getLoanNumber())
            .currency(e.getCurrency())
            .customerId(e.getCustomerId())
            .customerCode(e.getCustomerCode())
            .customerFirstName(e.getCustomerFirstName())
            .customerLastName(e.getCustomerLastName())
            .collectionStatus(e.getCollectionStatus())
            .assignedOfficerId(e.getAssignedOfficerId())
            .dpd(e.getDpd())
            .totalOverdueAmount(e.getTotalOverdueAmount())
            .penaltyRate(e.getPenaltyRate())
            .penaltyAmount(e.getPenaltyAmount())
            .remarks(e.getRemarks())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
