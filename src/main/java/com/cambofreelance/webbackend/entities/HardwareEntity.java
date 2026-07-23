package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "hardware")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class HardwareEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "name_kh", length = 255)
    private String nameKh;

    @Column(name = "brand", length = 150)
    private String brand;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "description_kh", columnDefinition = "TEXT")
    private String descriptionKh;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "content_kh", columnDefinition = "TEXT")
    private String contentKh;

    @Column(name = "connectivity", length = 255)
    private String connectivity;

    @Column(name = "price", length = 50)
    private String price;

    @Column(name = "platform", length = 100)
    private String platform;

    @Column(name = "countries", length = 500)
    private String countries;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryHardwareEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private MediaFileEntity image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "hardware_images",
        joinColumns = @JoinColumn(name = "hardware_id"),
        inverseJoinColumns = @JoinColumn(name = "media_file_id")
    )
    @OrderColumn(name = "position")
    private List<MediaFileEntity> images = new ArrayList<>();

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "icon", length = 100)
    private String icon;

    @Column(name = "link", length = 500)
    private String link;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
