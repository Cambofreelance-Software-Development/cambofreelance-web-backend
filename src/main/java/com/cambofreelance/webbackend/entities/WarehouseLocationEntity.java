package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Storage location within a warehouse (aisle, rack, bin, showroom slot).
 */
@Entity
@Table(name = "warehouse_locations")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class WarehouseLocationEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "warehouse_id", nullable = false)
    private String warehouseId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "aisle")
    private String aisle;

    @Column(name = "rack")
    private String rack;

    @Column(name = "bin")
    private String bin;
}
