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
@Table(name = "partner_payouts")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class PartnerPayoutEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "application_id")
    private String applicationId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "channel")
    private String channel;

    @Column(name = "reference")
    private String reference;

    @Column(name = "note")
    private String note;

    @Column(name = "paid_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paidAt;
}
