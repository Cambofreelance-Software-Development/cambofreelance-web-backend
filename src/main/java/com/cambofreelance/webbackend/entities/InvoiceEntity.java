package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "invoices")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class InvoiceEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Sequential human-readable ID, e.g. "INV-2026000001" */
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "subscription_id", nullable = false)
    private String subscriptionId;

    @Column(name = "package_id", nullable = false)
    private String packageId;

    /** First day of the billed calendar month */
    @Column(name = "billing_period", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date billingPeriod;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "tax", nullable = false)
    private BigDecimal tax;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "currency", nullable = false)
    private String currency;

    /** DRAFT | UNPAID | PAID */
    @Column(name = "invoice_status", nullable = false)
    private String invoiceStatus;

    @Column(name = "paid_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paidAt;
}
