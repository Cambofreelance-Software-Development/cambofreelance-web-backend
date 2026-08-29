package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.SaleEntity;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaleResponse {

    private String id;
    private String saleNo;
    private String customerId;
    private String customerName;
    private String customerPhone;
    private String salespersonId;
    private String warehouseId;
    private String saleStatus;
    private String paymentType;
    private String currency;

    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal downPayment;
    private BigDecimal financedAmount;

    private Date contractDate;
    private Date deliveryDate;
    private String notes;

    private String status;
    private Date createdAt;
    private String createdBy;

    public static SaleResponse from(SaleEntity e, String customerName, String customerPhone) {
        return SaleResponse.builder()
            .id(e.getId())
            .saleNo(e.getSaleNo())
            .customerId(e.getCustomerId())
            .customerName(customerName)
            .customerPhone(customerPhone)
            .salespersonId(e.getSalespersonId())
            .warehouseId(e.getWarehouseId())
            .saleStatus(e.getSaleStatus())
            .paymentType(e.getPaymentType())
            .currency(e.getCurrency())
            .subtotal(e.getSubtotal())
            .discountAmount(e.getDiscountAmount())
            .taxAmount(e.getTaxAmount())
            .totalAmount(e.getTotalAmount())
            .downPayment(e.getDownPayment())
            .financedAmount(e.getFinancedAmount())
            .contractDate(e.getContractDate())
            .deliveryDate(e.getDeliveryDate())
            .notes(e.getNotes())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .createdBy(e.getCreatedBy())
            .build();
    }
}
