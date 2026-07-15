package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "cms_business_type_tags")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false, exclude = "group")
@ToString(exclude = "group")
public class BusinessTypeTagEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private BusinessTypeGroupEntity group;

    @Column(name = "label", nullable = false, length = 255)
    private String label;

    @Column(name = "label_kh", length = 255)
    private String labelKh;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
