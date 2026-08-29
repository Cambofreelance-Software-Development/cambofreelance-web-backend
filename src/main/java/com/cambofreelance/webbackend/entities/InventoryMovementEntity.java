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
 * Immutable stock movement ledger entry.
 */
@Entity
@Table(name = "inventory_movements")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class InventoryMovementEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    /** OPENING | PURCHASE | SALE | RETURN_IN | RETURN_OUT | TRANSFER_IN | TRANSFER_OUT | ADJUSTMENT_IN | ADJUSTMENT_OUT | DAMAGE */
    @Column(name = "movement_type", nullable = false)
    private String movementType;

    @Column(name = "variant_id", nullable = false)
    private String variantId;

    @Column(name = "inventory_item_id")
    private String inventoryItemId;

    @Column(name = "batch_id")
    private String batchId;

    @Column(name = "from_warehouse_id")
    private String fromWarehouseId;

    @Column(name = "to_warehouse_id")
    private String toWarehouseId;

    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity = BigDecimal.ZERO;

    @Column(name = "unit_cost", nullable = false)
    private BigDecimal unitCost = BigDecimal.ZERO;

    @Column(name = "total_cost", nullable = false)
    private BigDecimal totalCost = BigDecimal.ZERO;

    /** PO | SALE | TRANSFER | ADJUSTMENT | INITIAL */
    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "note", length = 1000)
    private String note;
}
