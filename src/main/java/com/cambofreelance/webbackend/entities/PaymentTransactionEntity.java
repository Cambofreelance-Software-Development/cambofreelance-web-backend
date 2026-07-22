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
@Table(name = "payment_transaction")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class PaymentTransactionEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    /** The transaction key sent to ABA PayWay (max 20 chars) */
    @Column(name = "tran_id")
    private String tranId;

    @Column(name = "subscription_id")
    private String subscriptionId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "payment_option")
    private String paymentOption;

    @Column(name = "payment_status")
    private String paymentStatus;

    /** Approval code returned by PayWay */
    @Column(name = "apv")
    private String apv;

    @Column(name = "bank_ref")
    private String bankRef;

    @Column(name = "raw_callback")
    private String rawCallback;

    /** When we last confirmed the status server-to-server with PayWay */
    @Column(name = "verified_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date verifiedAt;

    @Column(name = "verified_by")
    private String verifiedBy;

    /** AUTO (PayWay confirmed) or MANUAL (admin marked) */
    @Column(name = "verify_method")
    private String verifyMethod;

    @Column(name = "verify_note")
    private String verifyNote;
}
