package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.constants.InstallmentStatus;
import com.cambofreelance.webbackend.entities.RepaymentInstallmentEntity;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstallmentResponse {

    /** Flat daily late fee, in the loan's currency, per day past dueDate while OVERDUE. */
    private static final BigDecimal DAILY_PENALTY_FEE = BigDecimal.ONE;

    private String id;
    private int installmentNo;
    private Date dueDate;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal totalPayment;
    private BigDecimal paidAmount;
    private BigDecimal remainingBalance;
    private BigDecimal penaltyAmount;
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
            .penaltyAmount(computePenalty(e))
            .installmentStatus(e.getInstallmentStatus())
            .paidDate(e.getPaidDate())
            .notes(e.getNotes())
            .build();
    }

    private static BigDecimal computePenalty(RepaymentInstallmentEntity e) {
        if (!InstallmentStatus.OVERDUE.equalsIgnoreCase(e.getInstallmentStatus()) || e.getDueDate() == null) {
            return BigDecimal.ZERO;
        }
        LocalDate due = Instant.ofEpochMilli(e.getDueDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        long days = ChronoUnit.DAYS.between(due, LocalDate.now());
        if (days <= 0) {
            return BigDecimal.ZERO;
        }
        return DAILY_PENALTY_FEE.multiply(BigDecimal.valueOf(days));
    }
}
