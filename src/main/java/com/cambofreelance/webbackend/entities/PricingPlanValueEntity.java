package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Entity
@Table(name = "pricing_plan_value")
@Data
public class PricingPlanValueEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "plan_id", nullable = false)
    private String planId;

    @Column(name = "feature_id", nullable = false)
    private String featureId;

    @Column(name = "value", length = 255)
    private String value;
}
