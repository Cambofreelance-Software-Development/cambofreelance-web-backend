package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.RepaymentInstallmentEntity;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstallmentResponse {

    private String id;
    private int installmentNo;
    private Date dueDate;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal totalPayment;
    private BigDecimal paidAmount;
    private BigDecimal remainingBalance;
    private String installmentStatus;
    private Date paidDate;
    private String notes;

    public static InstallmentResponse from(RepaymentInstallmentEntity e) {
        return InstallmentResponse.builder()
            .id(e.getId())
            .installmentNo(e.getInstallmentNo())
            .dueDate(e.getDueDate())
            .principalAmount(e.getPrincipalAmount())
            .interestAmount(e.getInterestAmount())
            .totalPayment(e.getTotalPayment())
            .paidAmount(e.getPaidAmount())
            .remainingBalance(e.getRemainingBalance())
            .installmentStatus(e.getInstallmentStatus())
            .paidDate(e.getPaidDate())
            .notes(e.getNotes())
            .build();
    }
}
