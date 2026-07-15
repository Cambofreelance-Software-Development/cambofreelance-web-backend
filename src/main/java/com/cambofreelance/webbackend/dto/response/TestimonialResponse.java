package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.TestimonialEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestimonialResponse {

    private String id;
    private String quote;
    private String quoteKh;
    private String authorName;
    private String authorNameKh;
    private String location;
    private String locationKh;
    private String avatarUrl;
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public static TestimonialResponse from(TestimonialEntity e) {
        return TestimonialResponse.builder()
            .id(e.getId())
            .quote(e.getQuote())
            .quoteKh(e.getQuoteKh())
            .authorName(e.getAuthorName())
            .authorNameKh(e.getAuthorNameKh())
            .location(e.getLocation())
            .locationKh(e.getLocationKh())
            .avatarUrl(e.getAvatarUrl())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}
