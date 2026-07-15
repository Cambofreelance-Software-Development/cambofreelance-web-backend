package com.cambofreelance.webbackend.dto.request;

import java.util.Map;
import lombok.Data;

@Data
public class PageCtasRequest {

    /**
     * Key: page slug. Value: bottom-CTA heading / subheading / button in EN + KH plus link.
     * Only slugs included in the map are updated.
     */
    private Map<String, PageCta> pages;

    @Data
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
