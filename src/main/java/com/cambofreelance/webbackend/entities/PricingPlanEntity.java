package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "pricing_plan")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class PricingPlanEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "name_kh", length = 150)
    private String nameKh;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "description_kh", length = 500)
    private String descriptionKh;

    @Column(name = "price_monthly", nullable = false)
    private BigDecimal priceMonthly = BigDecimal.ZERO;

    @Column(name = "price_yearly")
    private BigDecimal priceYearly;

    @Column(name = "currency", length = 8)
    private String currency = "$";

    @Column(name = "includes_note", length = 255)
    private String includesNote;

    @Column(name = "includes_note_kh", length = 255)
    private String includesNoteKh;

    @Column(name = "badge", length = 100)
    private String badge;

    @Column(name = "badge_kh", length = 100)
    private String badgeKh;

    @Column(name = "highlighted", nullable = false)
    private Boolean highlighted = false;

    @Column(name = "cta_label", length = 150)
    private String ctaLabel;

    @Column(name = "cta_label_kh", length = 150)
    private String ctaLabelKh;

    @Column(name = "cta_link", length = 500)
    private String ctaLink;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
