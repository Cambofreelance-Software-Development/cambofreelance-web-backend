package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.ArticleEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleResponse {

    private String id;
    private String title;
    private String slug;
    private String content;
    private String excerpt;
    private String titleKh;
    private String contentKh;
    private String excerptKh;
    private String type;
    private MediaFileResponse featuredImage;
    private List<MediaFileResponse> attachments;
    private String videoLink;
    private String authorId;
    private String authorName;
    private List<String> tags;
    private Date publishedAt;
    private Integer sortOrder;
    private Integer viewCount;
    private String status;
    private String workflowStatus;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;

    public static ArticleResponse from(ArticleEntity e) {
        List<String> tagList = StringUtils.hasText(e.getTags())
            ? Arrays.asList(e.getTags().split(","))
            : Collections.emptyList();

        List<MediaFileResponse> attachmentList = e.getAttachments() == null
            ? Collections.emptyList()
            : e.getAttachments().stream().map(MediaFileResponse::from).collect(Collectors.toList());

        return ArticleResponse.builder()
            .id(e.getId())
            .title(e.getTitle())
            .slug(e.getSlug())
            .content(e.getContent())
            .excerpt(e.getExcerpt())
            .titleKh(e.getTitleKh())
            .contentKh(e.getContentKh())
            .excerptKh(e.getExcerptKh())
            .type(e.getType())
            .featuredImage(e.getFeaturedImage() != null ? MediaFileResponse.from(e.getFeaturedImage()) : null)
            .attachments(attachmentList)
            .videoLink(e.getVideoLink())
            .authorId(e.getAuthorId())
            .authorName(e.getAuthorName())
            .tags(tagList)
            .publishedAt(e.getPublishedAt())
            .sortOrder(e.getSortOrder())
            .viewCount(e.getViewCount())
            .status(e.getStatus())
            .workflowStatus(e.getWorkflowStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .createdBy(e.getCreatedBy())
            .build();
    }
}
