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
@Table(name = "subscriptions")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class SubscriptionEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Sequential human-readable ID, e.g. "SUB-00000001" */
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "package_id", nullable = false)
    private String packageId;

    /** MONTHLY | QUARTERLY | YEARLY */
    @Column(name = "billing_cycle", nullable = false)
    private String billingCycle;

    /** Null while the subscription is PEN (pending approval); set by SubscriptionService.activate(). */
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    /** Null while the subscription is PEN (pending approval); set by SubscriptionService.activate(). */
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    /** PENDING | PAID | OVERDUE */
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Column(name = "auto_renewal", nullable = false)
    private boolean autoRenewal;

    /** Subscription status reuses ACT/SUS/EXP/DEL from BaseEntity.status */
}
