package com.cambofreelance.webbackend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "cms_business_type_groups")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false, exclude = "tags")
@ToString(exclude = "tags")
public class BusinessTypeGroupEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "title_kh", length = 255)
    private String titleKh;

    @Column(name = "icon", length = 100)
    private String icon;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC")
    private List<BusinessTypeTagEntity> tags = new ArrayList<>();
}
