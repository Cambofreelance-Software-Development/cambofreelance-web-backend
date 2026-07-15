package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TestimonialRequest {

    @NotBlank
    private String quote;

    private String quoteKh;

    @NotBlank
    private String authorName;

    private String authorNameKh;

    private String location;

    private String locationKh;

    private String avatarUrl;

    private Integer sortOrder = 0;
}
