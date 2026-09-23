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
@Table(name = "taxonomy_items")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class TaxonomyItemEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "code", nullable = false, length = 80)
    private String code;

    @Column(name = "taxonomy_code", nullable = false, length = 80)
    private String taxonomyCode;

    @Column(name = "parent_code", length = 80)
    private String parentCode;

    @Column(name = "display_en", nullable = false, length = 150)
    private String displayEn;

    @Column(name = "display_km", length = 150)
    private String displayKm;

    @Column(name = "metadata", length = 1000)
    private String metadata;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
