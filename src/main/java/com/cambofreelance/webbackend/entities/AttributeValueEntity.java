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
 * Predefined value for a master attribute.
 * Lives in the tenant's schema.
 */
@Entity
@Table(name = "attribute_values")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class AttributeValueEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "attribute_id", nullable = false)
    private String attributeId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;
}
