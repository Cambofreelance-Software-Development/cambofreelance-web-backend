package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.entities.FeatureTabBulletEntity;
import com.cambofreelance.webbackend.entities.FeatureTabEntity;
import com.cambofreelance.webbackend.entities.FeatureTabItemEntity;
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
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private List<FeatureTabItemResponse> items;

    public static FeatureTabResponse from(FeatureTabEntity e) {
        List<FeatureTabItemResponse> itemResponses = e.getItems() == null ? List.of() :
            e.getItems().stream()
                .filter(i -> !Constants.STATUS_DELETE.equals(i.getStatus()))
                .sorted(Comparator.comparing(i -> i.getSortOrder() == null ? 0 : i.getSortOrder()))
                .map(FeatureTabItemResponse::from)
                .collect(Collectors.toList());
        return FeatureTabResponse.builder()
            .id(e.getId())
            .tabLabel(e.getTabLabel())
            .tabLabelKh(e.getTabLabelKh())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .items(itemResponses)
            .build();
    }

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FeatureTabItemResponse {
        private String id;
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
        private List<FeatureTabBulletResponse> bullets;

        public static FeatureTabItemResponse from(FeatureTabItemEntity i) {
            List<FeatureTabBulletResponse> bulletResponses = i.getBullets() == null ? List.of() :
                i.getBullets().stream()
                    .filter(b -> !Constants.STATUS_DELETE.equals(b.getStatus()))
                    .sorted(Comparator.comparing(b -> b.getSortOrder() == null ? 0 : b.getSortOrder()))
                    .map(FeatureTabBulletResponse::from)
                    .collect(Collectors.toList());
            return FeatureTabItemResponse.builder()
                .id(i.getId())
                .title(i.getTitle())
                .titleKh(i.getTitleKh())
                .subtitle(i.getSubtitle())
                .subtitleKh(i.getSubtitleKh())
                .ctaLabel(i.getCtaLabel())
                .ctaLabelKh(i.getCtaLabelKh())
                .ctaHref(i.getCtaHref())
                .ctaButton(i.getCtaButton())
                .imageUrl(i.getImageUrl())
                .imageSide(i.getImageSide())
                .sortOrder(i.getSortOrder())
                .bullets(bulletResponses)
                .build();
        }
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
