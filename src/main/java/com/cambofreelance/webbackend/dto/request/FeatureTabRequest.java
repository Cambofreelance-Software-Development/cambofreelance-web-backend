package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class FeatureTabRequest {

    @NotBlank
    private String tabLabel;

    private String tabLabelKh;

    @NotBlank
    private String title;

    private String titleKh;

    private String subtitle;

    private String subtitleKh;

    private String ctaLabel;

    private String ctaLabelKh;

    private String ctaHref;

    private Boolean ctaButton = false;

    private String imageUrl;

    private String imageSide = "right";

    private Integer sortOrder = 0;

    @Valid
    private List<FeatureTabBulletRequest> bullets = new ArrayList<>();

    @Data
    public static class FeatureTabBulletRequest {
        private String id; // present on update, null on create
        private String icon = "check";
        @NotBlank
        private String label;
        private String labelKh;
        private String subLabel;
        private String subLabelKh;
        private Integer sortOrder = 0;
    }
}
