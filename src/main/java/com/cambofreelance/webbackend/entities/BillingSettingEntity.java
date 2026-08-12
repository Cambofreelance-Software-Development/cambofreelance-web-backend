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
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "billing_settings")
@Data
@DynamicUpdate
public class BillingSettingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "tax_rate")
    private BigDecimal taxRate;

    @Column(name = "tax_label")
    private String taxLabel;

    @Column(name = "invoice_prefix")
    private String invoicePrefix;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
