package com.cambofreelance.webbackend.dto.response;

import com.cambofreelance.webbackend.entities.PricingPlanEntity;
import com.cambofreelance.webbackend.entities.PricingPlanFeatureEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PricingPlanResponse {

    private String id;
    private String name;
    private String nameKh;
    private String description;
    private String descriptionKh;
    private BigDecimal priceMonthly;
    private BigDecimal priceYearly;
    private String currency;
    private String includesNote;
    private String includesNoteKh;
    private String badge;
    private String badgeKh;
    private Boolean highlighted;
    private String ctaLabel;
    private String ctaLabelKh;
    private String ctaLink;
    private Integer sortOrder;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private List<Feature> features;

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Feature {
        private String id;
        private String label;
        private String labelKh;
        private Integer sortOrder;

        public static Feature from(PricingPlanFeatureEntity e) {
            return Feature.builder()
                .id(e.getId())
                .label(e.getLabel())
                .labelKh(e.getLabelKh())
                .sortOrder(e.getSortOrder())
                .build();
        }
    }

    public static PricingPlanResponse from(PricingPlanEntity e, List<PricingPlanFeatureEntity> bullets) {
        return PricingPlanResponse.builder()
            .id(e.getId())
            .name(e.getName())
            .nameKh(e.getNameKh())
            .description(e.getDescription())
            .descriptionKh(e.getDescriptionKh())
            .priceMonthly(e.getPriceMonthly())
            .priceYearly(e.getPriceYearly())
            .currency(e.getCurrency())
            .includesNote(e.getIncludesNote())
            .includesNoteKh(e.getIncludesNoteKh())
            .badge(e.getBadge())
            .badgeKh(e.getBadgeKh())
            .highlighted(e.getHighlighted())
            .ctaLabel(e.getCtaLabel())
            .ctaLabelKh(e.getCtaLabelKh())
            .ctaLink(e.getCtaLink())
            .sortOrder(e.getSortOrder())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .features(bullets == null ? null : bullets.stream().map(Feature::from).toList())
            .build();
    }
}
