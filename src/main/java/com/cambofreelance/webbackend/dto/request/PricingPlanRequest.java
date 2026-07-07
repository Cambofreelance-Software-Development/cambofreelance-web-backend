package com.cambofreelance.webbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class PricingPlanRequest {

    @NotBlank
    private String name;

    private String nameKh;
    private String description;
    private String descriptionKh;
    private BigDecimal priceMonthly = BigDecimal.ZERO;
    private BigDecimal priceYearly;
    private String currency = "$";
    private String includesNote;
    private String includesNoteKh;
    private String badge;
    private String badgeKh;
    private Boolean highlighted = false;
    private String ctaLabel;
    private String ctaLabelKh;
    private String ctaLink;
    private Integer sortOrder = 0;

    private List<Feature> features;

    @Data
    public static class Feature {
        private String label;
        private String labelKh;
        private Integer sortOrder = 0;
    }
}
