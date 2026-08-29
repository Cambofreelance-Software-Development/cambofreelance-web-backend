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
 * Master catalog attribute definition.
 * Lives in the tenant's schema.
 */
@Entity
@Table(name = "attributes")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class AttributeEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    /** TEXT | NUMBER | BOOLEAN | DATE | SELECT */
    @Column(name = "data_type", nullable = false)
    private String dataType = "TEXT";

    @Column(name = "is_variant_attribute", nullable = false)
    private Boolean isVariantAttribute = false;

    @Column(name = "applicable_category")
    private String applicableCategory;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
}
