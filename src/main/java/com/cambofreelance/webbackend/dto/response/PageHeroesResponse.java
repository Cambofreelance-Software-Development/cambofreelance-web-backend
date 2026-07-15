package com.cambofreelance.webbackend.dto.response;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageHeroesResponse {

    /**
     * Key: page slug (e.g. "articles", "features", "business-types").
     * Value: heading / subheading in English + Khmer.
     */
    private Map<String, PageHero> pages;

    @Data
    @Builder
    public static class PageHero {
        private String heading;
        private String headingKh;
        private String subheading;
        private String subheadingKh;
    }
}
