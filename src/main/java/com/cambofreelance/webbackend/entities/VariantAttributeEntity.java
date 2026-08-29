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
 * Attribute dimension defining a specific Product Variant.
 * Lives in the tenant's schema.
 */
@Entity
@Table(name = "variant_attributes")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class VariantAttributeEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "variant_id", nullable = false)
    private String variantId;

    @Column(name = "attribute_id")
    private String attributeId;

    @Column(name = "attribute_name", nullable = false)
    private String attributeName;

    @Column(name = "attribute_value", nullable = false)
    private String attributeValue;
}
