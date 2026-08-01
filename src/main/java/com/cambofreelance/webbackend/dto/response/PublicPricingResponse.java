package com.cambofreelance.webbackend.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicPricingResponse {

    private List<PricingPlanResponse> plans;
    private List<PricingFeatureResponse> comparison;
    private List<PricingFaqResponse> faqs;
}
