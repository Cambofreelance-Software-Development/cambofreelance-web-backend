package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Reservation record to guarantee unit hold and prevent concurrent double-booking.
 */
@Entity
@Table(name = "inventory_reservations")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class InventoryReservationEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "inventory_item_id", nullable = false)
    private String inventoryItemId;

    @Column(name = "sale_id")
    private String saleId;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "reserved_by", nullable = false)
    private String reservedBy;

    /** ACTIVE | RELEASED | EXPIRED | CONVERTED */
    @Column(name = "status", nullable = false)
    private String status = "ACTIVE";

    @Column(name = "reserved_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservedAt = new Date();

    @Column(name = "expires_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;

    @Column(name = "released_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releasedAt;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "created_by", nullable = false)
    private String createdBy = "SYS";

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
