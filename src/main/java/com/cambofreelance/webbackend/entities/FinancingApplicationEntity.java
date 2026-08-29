package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Financing application bridging Inventory Sales with Mini Loan or External Banks.
 */
@Entity
@Table(name = "financing_applications")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class FinancingApplicationEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "application_no", nullable = false, unique = true)
    private String applicationNo;

    @Column(name = "sale_id", nullable = false)
    private String saleId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "bank_id")
    private String bankId;

    @Column(name = "requested_amount", nullable = false)
    private BigDecimal requestedAmount;

    @Column(name = "approved_amount")
    private BigDecimal approvedAmount;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "term_months")
    private Integer termMonths;

    @Column(name = "monthly_installment")
    private BigDecimal monthlyInstallment;

    /** DRAFT | SUBMITTED | UNDER_REVIEW | ADDITIONAL_DOCUMENT_REQUIRED | APPROVED | REJECTED | CANCELLED | EXPIRED */
    @Column(name = "status", nullable = false)
    private String status = "DRAFT";

    @Column(name = "external_reference")
    private String externalReference;

    @Column(name = "guarantor_customer_id")
    private String guarantorCustomerId;

    @Column(name = "submitted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt;

    @Column(name = "approved_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date approvedAt;

    @Column(name = "rejected_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rejectedAt;

    @Column(name = "rejection_reason", length = 1000)
    private String rejectionReason;

    @Column(name = "created_by", nullable = false)
    private String createdBy = "SYS";

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
