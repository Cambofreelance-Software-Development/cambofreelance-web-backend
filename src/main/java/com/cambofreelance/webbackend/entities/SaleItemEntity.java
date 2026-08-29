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
 * Line item of a sale. Links to exact serialized inventory item (VIN) if serialized.
 */
@Entity
@Table(name = "sale_items")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class SaleItemEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "sale_id", nullable = false)
    private String saleId;

    @Column(name = "variant_id", nullable = false)
    private String variantId;

    @Column(name = "inventory_item_id")
    private String inventoryItemId;

    @Column(name = "batch_id")
    private String batchId;

    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity = BigDecimal.ONE;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(name = "discount_amount", nullable = false)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "cost_price", nullable = false)
    private BigDecimal costPrice = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;
}
