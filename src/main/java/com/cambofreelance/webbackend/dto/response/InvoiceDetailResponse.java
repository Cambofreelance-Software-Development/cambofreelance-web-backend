package com.cambofreelance.webbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

/** Enriched, print-ready invoice: the invoice plus billed-party, seller and line details. */
@Data
@Builder
public class InvoiceDetailResponse {

    // ── Invoice ──
    private String id;
    private String invoiceNo;
    private String currency;
    private String invoiceStatus;
    private BigDecimal subtotal;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal total;
    private String taxLabel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date issuedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date paidAt;

    // ── Line item ──
    private String planName;
    private String billingCycle;

    // ── Payment reference ──
    private String tranId;
    private String apv;
    private String paymentOption;

    // ── Bill to ──
    private String customerName;
    private String customerEmail;
    private String companyName;
    private String companyPhone;
    private String address;
    private String city;
    private String country;

    // ── Seller ──
    private String sellerName;
    private String sellerEmail;
    private String sellerAddress;
}
