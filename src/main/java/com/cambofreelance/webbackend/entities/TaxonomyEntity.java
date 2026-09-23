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
@Table(name = "taxonomies")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class TaxonomyEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "code", nullable = false, length = 80, unique = true)
    private String code;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "is_hierarchical", nullable = false)
    private Boolean isHierarchical = false;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "remark", length = 500)
    private String remark;
}
