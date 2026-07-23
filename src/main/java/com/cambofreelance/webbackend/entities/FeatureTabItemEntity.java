package com.cambofreelance.webbackend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "cms_feature_tab_items")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false, exclude = {"tab", "bullets"})
@ToString(exclude = {"tab", "bullets"})
public class FeatureTabItemEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tab_id", nullable = false)
    private FeatureTabEntity tab;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "title_kh", length = 255)
    private String titleKh;

    @Column(name = "subtitle", columnDefinition = "TEXT")
    private String subtitle;

    @Column(name = "subtitle_kh", columnDefinition = "TEXT")
    private String subtitleKh;

    @Column(name = "cta_label", length = 255)
    private String ctaLabel;

    @Column(name = "cta_label_kh", length = 255)
    private String ctaLabelKh;

    @Column(name = "cta_href", length = 500)
    private String ctaHref;

    @Column(name = "cta_button", nullable = false)
    private Boolean ctaButton = false;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "image_side", length = 10, nullable = false)
    private String imageSide = "right";

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sortOrder ASC")
    private List<FeatureTabBulletEntity> bullets = new ArrayList<>();
}
