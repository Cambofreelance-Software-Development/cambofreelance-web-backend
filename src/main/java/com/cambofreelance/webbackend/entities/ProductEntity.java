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
 * Represents the parent catalog / product model.
 * Lives in the tenant's schema.
 */
@Entity
@Table(name = "products")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class ProductEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "product_code", unique = true)
    private String productCode;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "name", nullable = false)
    private String name;

    /** STOCK | VEHICLE | PART | ELECTRONICS | SERVICE */
    @Column(name = "product_type", nullable = false)
    private String productType;

    @Column(name = "category_id", nullable = false)
    private String categoryId;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "model_year")
    private Integer modelYear;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "preferred_supplier")
    private String preferredSupplier;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    /** QUANTITY | BATCH | SERIALIZED */
    @Column(name = "tracking_type", nullable = false)
    private String trackingType;

    /** DRAFT | ACTIVE | INACTIVE */
    @Column(name = "catalog_status", nullable = false)
    private String catalogStatus;

    @Column(name = "has_variants", nullable = false)
    private Boolean hasVariants = false;

    // Pricing defaults
    @Column(name = "currency", nullable = false)
    private String currency = "USD";

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(name = "retail_price")
    private BigDecimal retailPrice;

    @Column(name = "wholesale_price")
    private BigDecimal wholesalePrice;

    @Column(name = "vip_price")
    private BigDecimal vipPrice;

    @Column(name = "discount_value")
    private BigDecimal discountValue;

    /** percent | fixed */
    @Column(name = "discount_type")
    private String discountType;

    @Column(name = "tax_rate")
    private String taxRate;

    // Inventory thresholds
    @Column(name = "reorder_level")
    private Integer reorderLevel;

    @Column(name = "min_stock")
    private Integer minStock;

    @Column(name = "max_stock")
    private Integer maxStock;

    @Column(name = "default_warehouse_id")
    private String defaultWarehouseId;

    @Column(name = "default_location")
    private String defaultLocation;

    // Packaging conversions
    @Column(name = "case_name")
    private String caseName;

    @Column(name = "case_qty")
    private Integer caseQty;

    @Column(name = "case_barcode")
    private String caseBarcode;

    @Column(name = "box_name")
    private String boxName;

    @Column(name = "box_qty")
    private Integer boxQty;

    @Column(name = "box_barcode")
    private String boxBarcode;

    // Vehicle specifications (shared at model level)
    @Column(name = "engine_cc")
    private Integer engineCc;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "transmission")
    private String transmission;

    @Column(name = "vehicle_condition")
    private String vehicleCondition;

    @Column(name = "warranty_period")
    private String warrantyPeriod;
}
