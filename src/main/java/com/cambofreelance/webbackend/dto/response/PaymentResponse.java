package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.PaymentEntity;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

    private String id;
    private String receiptNumber;
    private String loanApplicationId;
    private String loanNumber;
    private String currency;
    private String customerId;
    private String customerCode;
    private String customerFirstName;
    private String customerLastName;
    private Date paymentDate;
    private BigDecimal principalPaid;
    private BigDecimal interestPaid;
    private BigDecimal penaltyFee;
    private BigDecimal totalPaid;
    private String paymentMethod;
    private String paymentType;
    private String receivedById;
    private String notes;
    private String paymentStatus;
    private Date reversedAt;
    private String reversedById;
    private String reversalReason;
    private Date createdAt;

    public static PaymentResponse from(PaymentEntity e) {
        return PaymentResponse.builder()
            .id(e.getId())
            .receiptNumber(e.getReceiptNumber())
            .loanApplicationId(e.getLoanApplicationId())
            .loanNumber(e.getLoanNumber())
            .currency(e.getCurrency())
            .customerId(e.getCustomerId())
            .customerCode(e.getCustomerCode())
            .customerFirstName(e.getCustomerFirstName())
            .customerLastName(e.getCustomerLastName())
            .paymentDate(e.getPaymentDate())
            .principalPaid(e.getPrincipalPaid())
            .interestPaid(e.getInterestPaid())
            .penaltyFee(e.getPenaltyFee())
            .totalPaid(e.getTotalPaid())
            .paymentMethod(e.getPaymentMethod())
            .paymentType(e.getPaymentType())
            .receivedById(e.getReceivedById())
            .notes(e.getNotes())
            .paymentStatus(e.getPaymentStatus())
            .reversedAt(e.getReversedAt())
            .reversedById(e.getReversedById())
            .reversalReason(e.getReversalReason())
            .createdAt(e.getCreatedAt())
            .build();
    }
}
