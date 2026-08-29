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
 * Sale transaction record in the tenant schema.
 */
@Entity
@Table(name = "sales")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class SaleEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "sale_no", nullable = false, unique = true)
    private String saleNo;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "salesperson_id")
    private String salespersonId;

    @Column(name = "warehouse_id")
    private String warehouseId;

    /** DRAFT | RESERVED | LOAN_PENDING | CONFIRMED | DELIVERED | CANCELLED | COMPLETED */
    @Column(name = "sale_status", nullable = false)
    private String saleStatus = "DRAFT";

    /** CASH | INSTALLMENT | BANK_LOAN */
    @Column(name = "payment_type", nullable = false)
    private String paymentType = "CASH";

    @Column(name = "currency", nullable = false)
    private String currency = "USD";

    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "discount_amount", nullable = false)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount", nullable = false)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "down_payment", nullable = false)
    private BigDecimal downPayment = BigDecimal.ZERO;

    @Column(name = "financed_amount", nullable = false)
    private BigDecimal financedAmount = BigDecimal.ZERO;

    @Column(name = "contract_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date contractDate;

    @Column(name = "delivery_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
}
