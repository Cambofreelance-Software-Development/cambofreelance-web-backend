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
 * Batch/Lot tracking entity for quantities with expiry and manufacturing dates.
 */
@Entity
@Table(name = "inventory_batches")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class InventoryBatchEntity extends BaseEntity {

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

    @Column(name = "batch_no", nullable = false)
    private String batchNo;

    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity = BigDecimal.ZERO;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(name = "manufactured_at")
    @Temporal(TemporalType.DATE)
    private Date manufacturedAt;

    @Column(name = "expires_at")
    @Temporal(TemporalType.DATE)
    private Date expiresAt;
}
