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
@Table(name = "cms_home_products")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class HomeProductEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "name_kh", length = 255)
    private String nameKh;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "description_kh", columnDefinition = "TEXT")
    private String descriptionKh;

    @Column(name = "icon", length = 100)
    private String icon;

    @Column(name = "icon_bg", length = 255)
    private String iconBg;

    @Column(name = "href", length = 500)
    private String href;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
