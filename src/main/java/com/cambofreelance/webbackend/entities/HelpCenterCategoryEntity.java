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
@Table(name = "help_center_categories")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class HelpCenterCategoryEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    /** Which Article Type this category tree belongs to (e.g. HELP_CENTER). */
    @Column(name = "article_type_id", nullable = false)
    private String articleTypeId;

    /** Self-reference for unlimited nesting; null = top-level topic. */
    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "name_kh", length = 150)
    private String nameKh;

    @Column(name = "slug", nullable = false, length = 180, unique = true)
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "description_kh", columnDefinition = "TEXT")
    private String descriptionKh;

    @Column(name = "icon", length = 100)
    private String icon;

    @Column(name = "display_order")
    private Integer displayOrder = 0;
}
