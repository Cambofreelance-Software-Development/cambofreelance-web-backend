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

    /** Optional external video URL (YouTube/Vimeo/mp4) shown by the "Watch video" button. */
    @Column(name = "video_link", length = 500)
    private String videoLink;

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

    /** Self-reference for unlimited sub-articles (e.g. Help Center "Details" pages). */
    @Column(name = "parent_article_id")
    private String parentArticleId;

    @Column(name = "meta_title", length = 255)
    private String metaTitle;

    @Column(name = "meta_description", length = 500)
    private String metaDescription;

    @Column(name = "meta_keywords", length = 500)
    private String metaKeywords;

    @Column(name = "canonical_url", length = 500)
    private String canonicalUrl;

    /** Help Center topics this article is filed under — one article can belong to several. */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "article_categories",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<HelpCenterCategoryEntity> categories = new ArrayList<>();
}
