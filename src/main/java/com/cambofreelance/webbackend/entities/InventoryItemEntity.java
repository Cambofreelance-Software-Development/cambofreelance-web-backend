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

/**
 * One physical serialized item (Motorcycle, Car, Truck, Smartphone, Laptop).
 * Identified uniquely by VIN, engine number, or serial number.
 */
@Entity
@Table(name = "inventory_items")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class InventoryItemEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "variant_id", nullable = false)
    private String variantId;

    @Column(name = "warehouse_id", nullable = false)
    private String warehouseId;

    @Column(name = "location_id")
    private String locationId;

    // Unit Identifiers
    @Column(name = "serial_no")
    private String serialNo;

    @Column(name = "vin", length = 17)
    private String vin;

    @Column(name = "engine_no")
    private String engineNo;

    @Column(name = "color")
    private String color;

    // Unit Financials & Status
    @Column(name = "purchase_cost", nullable = false)
    private BigDecimal purchaseCost = BigDecimal.ZERO;

    /** AVAILABLE | RESERVED | LOAN_PENDING | SOLD | DELIVERED | RETURNED | DAMAGED */
    @Column(name = "item_status", nullable = false)
    private String itemStatus = "AVAILABLE";

    // Origin & Sourcing
    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "po_reference")
    private String poReference;

    @Column(name = "received_at", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date receivedAt = new Date();

    // Salesperson hold
    @Column(name = "reserved_by")
    private String reservedBy;

    @Column(name = "reserved_until")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservedUntil;
}
