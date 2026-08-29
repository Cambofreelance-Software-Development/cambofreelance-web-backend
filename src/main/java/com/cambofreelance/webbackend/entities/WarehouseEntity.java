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
 * Physical warehouse or branch store in a tenant schema.
 */
@Entity
@Table(name = "warehouses")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class WarehouseEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "manager_name")
    private String managerName;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;
}
