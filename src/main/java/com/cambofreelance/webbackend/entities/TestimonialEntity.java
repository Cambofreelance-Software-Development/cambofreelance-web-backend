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
@Table(name = "cms_testimonials")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class TestimonialEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "quote", nullable = false, columnDefinition = "TEXT")
    private String quote;

    @Column(name = "quote_kh", columnDefinition = "TEXT")
    private String quoteKh;

    @Column(name = "author_name", nullable = false, length = 255)
    private String authorName;

    @Column(name = "author_name_kh", length = 255)
    private String authorNameKh;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "location_kh", length = 255)
    private String locationKh;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
