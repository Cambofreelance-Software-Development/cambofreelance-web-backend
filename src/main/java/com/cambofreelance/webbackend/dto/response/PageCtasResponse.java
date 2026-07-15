package com.cambofreelance.webbackend.dto.response;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageCtasResponse {

    private Map<String, PageCta> pages;

    @Data
    @Builder
    public static class PageCta {
        private String heading;
        private String headingKh;
        private String subheading;
        private String subheadingKh;
        private String buttonLabel;
        private String buttonLabelKh;
        private String buttonLink;
    }
}
