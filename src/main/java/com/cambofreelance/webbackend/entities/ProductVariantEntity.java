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

/**
 * Represents a sellable configuration/variant of a product.
 * Lives in the tenant's schema.
 */
@Entity
@Table(name = "product_variants")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class ProductVariantEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "cost_price_override")
    private BigDecimal costPriceOverride;

    @Column(name = "retail_price_override")
    private BigDecimal retailPriceOverride;

    @Column(name = "wholesale_price_override")
    private BigDecimal wholesalePriceOverride;

    @Column(name = "vip_price_override")
    private BigDecimal vipPriceOverride;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    /** ACTIVE | INACTIVE */
    @Column(name = "variant_status", nullable = false)
    private String variantStatus = "ACTIVE";
}
