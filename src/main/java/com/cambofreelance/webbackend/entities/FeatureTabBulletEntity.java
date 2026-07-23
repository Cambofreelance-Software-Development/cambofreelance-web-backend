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
@Table(name = "cms_feature_tab_bullets")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false, exclude = "item")
@ToString(exclude = "item")
public class FeatureTabBulletEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private FeatureTabItemEntity item;

    @Column(name = "icon", nullable = false, length = 20)
    private String icon = "check";

    @Column(name = "label", nullable = false, length = 500)
    private String label;

    @Column(name = "label_kh", length = 500)
    private String labelKh;

    @Column(name = "sub_label", columnDefinition = "TEXT")
    private String subLabel;

    @Column(name = "sub_label_kh", columnDefinition = "TEXT")
    private String subLabelKh;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
