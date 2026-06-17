package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.LoanDisbursementEntity;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanDisbursementResponse {

    private String id;
    private String loanApplicationId;
    private String loanNumber;
    private String customerId;
    private String customerCode;
    private String customerFirstName;
    private String customerLastName;
    private BigDecimal disbursementAmount;
    private String currency;
    private String contractMediaId;
    private String contractFileName;
    private String contractFileUrl;
    private Date disbursementDate;
    private String disbursedById;
    private String disbursedByName;
    private String disbursementStatus;
    private String notes;
    private String status;
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;

    public static LoanDisbursementResponse from(LoanDisbursementEntity e,
                                                String disbursedByName,
                                                MediaFileResponse contract) {
        return LoanDisbursementResponse.builder()
            .id(e.getId())
            .loanApplicationId(e.getLoanApplicationId())
            .loanNumber(e.getLoanNumber())
            .customerId(e.getCustomerId())
            .customerCode(e.getCustomerCode())
            .customerFirstName(e.getCustomerFirstName())
            .customerLastName(e.getCustomerLastName())
            .disbursementAmount(e.getDisbursementAmount())
            .currency(e.getCurrency())
            .contractMediaId(e.getContractMediaId())
            .contractFileName(contract != null ? contract.getFileName() : null)
            .contractFileUrl(contract != null ? contract.getFileUrl() : null)
            .disbursementDate(e.getDisbursementDate())
            .disbursedById(e.getDisbursedById())
            .disbursedByName(disbursedByName)
            .disbursementStatus(e.getDisbursementStatus())
            .notes(e.getNotes())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .createdBy(e.getCreatedBy())
            .updatedAt(e.getUpdatedAt())
            .updatedBy(e.getUpdatedBy())
            .build();
    }
}
