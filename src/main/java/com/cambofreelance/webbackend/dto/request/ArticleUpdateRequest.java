package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class ArticleUpdateRequest {

    @NotBlank
    private String title;

    private String content;

    private String excerpt;

    private String titleKh;

    private String contentKh;

    private String excerptKh;

    /** NEWS | PROMOTIONS | BLOGS | ANNOUNCEMENTS | SERVICE | TEAM | COURSE | PRODUCTS */
    @NotBlank
    private String type;

    /** DRAFT | REVIEW | APPROVAL | PUBLISHED | ARCHIVED — unchanged if omitted */
    private String workflowStatus;

    private String featuredImageId;

    private List<String> attachmentIds = new ArrayList<>();

    private String videoLink;

    private String authorId;

    private String authorName;

    private List<String> tags = new ArrayList<>();

    private Date publishedAt;

    private Integer sortOrder = 0;

    /** Parent article this is a sub-article of (e.g. Help Center "Details" pages). */
    private String parentArticleId;

    /** Help Center category ids this article should be filed under (many-to-many). */
    private List<String> categoryIds = new ArrayList<>();

    private String metaTitle;

    private String metaDescription;

    private String metaKeywords;

    private String canonicalUrl;
}
