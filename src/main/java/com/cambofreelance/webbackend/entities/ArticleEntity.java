package com.cambofreelance.webbackend.entities;

import com.cambofreelance.webbackend.constants.ArticleWorkflowStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "articles")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class ArticleEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "slug", nullable = false, length = 500, unique = true)
    private String slug;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "excerpt", length = 1000)
    private String excerpt;

    @Column(name = "title_kh", length = 500)
    private String titleKh;

    @Column(name = "content_kh", columnDefinition = "TEXT")
    private String contentKh;

    @Column(name = "excerpt_kh", length = 1000)
    private String excerptKh;

    /** One of: NEWS | PROMOTIONS | BLOGS | ANNOUNCEMENTS | SERVICE | TEAM | COURSE | PRODUCTS */
    @Column(name = "type", nullable = false, length = 20)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "featured_image_id")
    private MediaFileEntity featuredImage;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "article_attachments",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "media_file_id")
    )
    private List<MediaFileEntity> attachments = new ArrayList<>();

    @Column(name = "author_id")
    private String authorId;

    @Column(name = "author_name")
    private String authorName;

    /** Comma-separated tag values */
    @Column(name = "tags", length = 500)
    private String tags;

    @Column(name = "published_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedAt;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    /** Editorial workflow state: DRAFT → REVIEW → APPROVAL → PUBLISHED → ARCHIVED */
    @Column(name = "workflow_status", nullable = false, length = 20)
    private String workflowStatus = ArticleWorkflowStatus.DRAFT;
}
