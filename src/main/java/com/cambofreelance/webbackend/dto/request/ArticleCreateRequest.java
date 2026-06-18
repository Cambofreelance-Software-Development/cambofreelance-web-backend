package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class ArticleCreateRequest {

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

    /** DRAFT | REVIEW | APPROVAL | PUBLISHED | ARCHIVED — defaults to DRAFT if omitted */
    private String workflowStatus;

    private String featuredImageId;

    private List<String> attachmentIds = new ArrayList<>();

    private String authorId;

    private String authorName;

    private List<String> tags = new ArrayList<>();

    private Date publishedAt;

    private Integer sortOrder = 0;
}
