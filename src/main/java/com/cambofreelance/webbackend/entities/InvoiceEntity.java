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

@Entity
@Table(name = "invoices")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class InvoiceEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "invoice_no")
    private String invoiceNo;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "subscription_id")
    private String subscriptionId;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Column(name = "tax_rate")
    private BigDecimal taxRate;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "currency")
    private String currency;

    /** ISSUED / PAID / REFUNDED / CANCELLED */
    @Column(name = "invoice_status")
    private String invoiceStatus;

    @Column(name = "issued_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date issuedAt;

    @Column(name = "paid_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paidAt;
}
