package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "pricing_plan_feature")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class PricingPlanFeatureEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "plan_id", nullable = false)
    private String planId;

    @Column(name = "label", nullable = false, length = 255)
    private String label;

    @Column(name = "label_kh", length = 255)
    private String labelKh;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
