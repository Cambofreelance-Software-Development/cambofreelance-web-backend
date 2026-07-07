package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "category_hardware")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class CategoryHardwareEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "name_kh", length = 100)
    private String nameKh;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
