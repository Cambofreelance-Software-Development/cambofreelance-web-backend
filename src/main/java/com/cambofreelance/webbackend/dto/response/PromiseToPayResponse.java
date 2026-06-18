package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.PromiseToPayEntity;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PromiseToPayResponse {

    private String id;
    private String collectionCaseId;
    private String loanApplicationId;
    private Date promiseDate;
    private BigDecimal promiseAmount;
    private String ptpStatus;
    private String notes;
    private String createdBy;
    private Date createdAt;

    public static PromiseToPayResponse from(PromiseToPayEntity e) {
        return PromiseToPayResponse.builder()
            .id(e.getId())
            .collectionCaseId(e.getCollectionCaseId())
            .loanApplicationId(e.getLoanApplicationId())
            .promiseDate(e.getPromiseDate())
            .promiseAmount(e.getPromiseAmount())
            .ptpStatus(e.getPtpStatus())
            .notes(e.getNotes())
            .createdBy(e.getCreatedBy())
            .createdAt(e.getCreatedAt())
            .build();
    }
}
