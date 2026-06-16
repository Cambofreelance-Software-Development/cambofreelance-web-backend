package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "subscription_packages")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class SubscriptionPackageEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    /** Unique short code, e.g. "STARTER" */
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "monthly_price")
    private BigDecimal monthlyPrice;

    /** True for plans priced on request (e.g. Enterprise) instead of a flat monthly price */
    @Column(name = "is_custom_pricing")
    private boolean customPricing;

    /** null = unlimited */
    @Column(name = "max_customers")
    private Integer maxCustomers;

    /** null = unlimited */
    @Column(name = "max_loans")
    private Integer maxLoans;

    /** null = unlimited */
    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "sort_order")
    private Integer sortOrder;
}
