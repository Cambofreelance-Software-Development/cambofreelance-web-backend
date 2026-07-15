package com.cambofreelance.webbackend.dto.request;

import java.util.Map;
import lombok.Data;

@Data
public class PageHeroesRequest {

    /**
     * Key: page slug (e.g. "articles", "features", "business-types").
     * Value: heading / subheading in English + Khmer.
     * Only the pages included in the map are updated — missing pages
     * keep their existing DB values.
     */
    private Map<String, PageHero> pages;

    @Data
    public static class PageHero {
        private String heading;
        private String headingKh;
        private String subheading;
        private String subheadingKh;
    }
}
