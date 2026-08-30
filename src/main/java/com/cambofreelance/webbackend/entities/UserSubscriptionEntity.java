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
@Table(name = "user_subscription")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class UserSubscriptionEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "plan_id")
    private String planId;

    @Column(name = "billing_cycle")
    private String billingCycle;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "currency")
    private String currency;

    @Column(name = "sub_status")
    private String subStatus;

    @Column(name = "start_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startAt;

    @Column(name = "expires_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;

    /** Opt-in to Card-on-File auto-charge. Only ever meaningful once payway.cof-enabled=true. */
    @Column(name = "auto_renew")
    private Boolean autoRenew = false;

    /** Opaque token PayWay returns once a card is tokenized — never raw card data. */
    @Column(name = "payment_token")
    private String paymentToken;

    @Column(name = "payment_token_captured_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentTokenCapturedAt;

    @Column(name = "auto_renew_failure_count")
    private Integer autoRenewFailureCount = 0;

    /** Guards the renewal job from re-attempting the same subscription twice in one run window. */
    @Column(name = "auto_renew_last_attempt_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date autoRenewLastAttemptAt;

    /** One-shot flags so the expiry-reminder job never re-notifies the same threshold twice. Reset on renewal. */
    @Column(name = "notice_7d_sent")
    private Boolean notice7dSent = false;

    @Column(name = "notice_3d_sent")
    private Boolean notice3dSent = false;

    @Column(name = "notice_1d_sent")
    private Boolean notice1dSent = false;

    /** userId of the user who referred this subscription's owner, snapshotted at first checkout
     *  (from UserEntity.referredBy) — stays fixed for the subscription's lifetime. */
    @Column(name = "referrer_id")
    private String referrerId;
}
