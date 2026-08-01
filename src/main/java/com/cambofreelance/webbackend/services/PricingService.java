package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.PricingFaqRequest;
import com.cambofreelance.webbackend.dto.request.PricingFeatureRequest;
import com.cambofreelance.webbackend.dto.request.PricingPlanRequest;
import com.cambofreelance.webbackend.dto.response.PricingFaqResponse;
import com.cambofreelance.webbackend.dto.response.PricingFeatureResponse;
import com.cambofreelance.webbackend.dto.response.PricingPlanResponse;
import com.cambofreelance.webbackend.dto.response.PublicPricingResponse;
import java.util.List;

public interface PricingService {

    PublicPricingResponse getPublicPricing();

    // Plans
    List<PricingPlanResponse> listPlans();

    PricingPlanResponse getPlan(String id);

    PricingPlanResponse createPlan(PricingPlanRequest request, String createdBy);

    PricingPlanResponse updatePlan(String id, PricingPlanRequest request, String updatedBy);

    void deletePlan(String id);

    // Comparison features
    List<PricingFeatureResponse> listFeatures();

    PricingFeatureResponse getFeature(String id);

    PricingFeatureResponse createFeature(PricingFeatureRequest request, String createdBy);

    PricingFeatureResponse updateFeature(String id, PricingFeatureRequest request, String updatedBy);

    void deleteFeature(String id);

    // FAQs
    List<PricingFaqResponse> listFaqs();

    PricingFaqResponse getFaq(String id);

    PricingFaqResponse createFaq(PricingFaqRequest request, String createdBy);

    PricingFaqResponse updateFaq(String id, PricingFaqRequest request, String updatedBy);

    void deleteFaq(String id);
}
