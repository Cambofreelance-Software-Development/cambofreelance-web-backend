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

    /** Admin who triggered the refund (null unless refunded) */
    @Column(name = "refunded_by")
    private String refundedBy;

    @Column(name = "refund_reason")
    private String refundReason;

    @Column(name = "refunded_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date refundedAt;

    /** Plan this transaction activates onto — same as the subscription's current plan for a
     *  plain renewal, different for a prorated upgrade. */
    @Column(name = "target_plan_id")
    private String targetPlanId;

    /** True when {@code amount} was computed as a mid-cycle prorated upgrade charge. */
    @Column(name = "prorated")
    private Boolean prorated = false;

    /** USER (checkout/renewal by the customer) or AUTO_RENEW (unattended Card-on-File charge). */
    @Column(name = "initiated_by")
    private String initiatedBy = com.cambofreelance.webbackend.constants.Constants.PAY_INITIATED_USER;

    /** Copied from the subscription's referrerId at the time this transaction was created — kept
     *  on every transaction (not just the first) so a future commission job doesn't need to join
     *  back through the subscription/user chain. */
    @Column(name = "referrer_id")
    private String referrerId;
}
