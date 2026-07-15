package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.entities.FeatureTabBulletEntity;
import com.cambofreelance.webbackend.entities.FeatureTabEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeatureTabResponse {

    private String id;
    private String tabLabel;
    private String tabLabelKh;
    private String title;
    private String titleKh;
    private String subtitle;
    private String subtitleKh;
    private String ctaLabel;
    private String ctaLabelKh;
    private String ctaHref;
    private Boolean ctaButton;
    private String imageUrl;
    private String imageSide;
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private List<FeatureTabBulletResponse> bullets;

    public static FeatureTabResponse from(FeatureTabEntity e) {
        List<FeatureTabBulletResponse> bulletResponses = e.getBullets() == null ? List.of() :
            e.getBullets().stream()
                .filter(b -> !Constants.STATUS_DELETE.equals(b.getStatus()))
                .sorted(Comparator.comparing(b -> b.getSortOrder() == null ? 0 : b.getSortOrder()))
                .map(FeatureTabBulletResponse::from)
                .collect(Collectors.toList());
        return FeatureTabResponse.builder()
            .id(e.getId())
            .tabLabel(e.getTabLabel())
            .tabLabelKh(e.getTabLabelKh())
            .title(e.getTitle())
            .titleKh(e.getTitleKh())
            .subtitle(e.getSubtitle())
            .subtitleKh(e.getSubtitleKh())
            .ctaLabel(e.getCtaLabel())
            .ctaLabelKh(e.getCtaLabelKh())
            .ctaHref(e.getCtaHref())
            .ctaButton(e.getCtaButton())
            .imageUrl(e.getImageUrl())
            .imageSide(e.getImageSide())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .bullets(bulletResponses)
            .build();
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FeatureTabBulletResponse {
        private String id;
        private String icon;
        private String label;
        private String labelKh;
        private String subLabel;
        private String subLabelKh;
        private Integer sortOrder;

        public static FeatureTabBulletResponse from(FeatureTabBulletEntity b) {
            return FeatureTabBulletResponse.builder()
                .id(b.getId())
                .icon(b.getIcon())
                .label(b.getLabel())
                .labelKh(b.getLabelKh())
                .subLabel(b.getSubLabel())
                .subLabelKh(b.getSubLabelKh())
                .sortOrder(b.getSortOrder())
                .build();
        }
    }
}
