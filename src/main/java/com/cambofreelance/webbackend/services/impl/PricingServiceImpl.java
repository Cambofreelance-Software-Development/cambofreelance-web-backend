package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.PricingFaqRequest;
import com.cambofreelance.webbackend.dto.request.PricingFeatureRequest;
import com.cambofreelance.webbackend.dto.request.PricingPlanRequest;
import com.cambofreelance.webbackend.dto.response.PricingFaqResponse;
import com.cambofreelance.webbackend.dto.response.PricingFeatureResponse;
import com.cambofreelance.webbackend.dto.response.PricingPlanResponse;
import com.cambofreelance.webbackend.dto.response.PublicPricingResponse;
import com.cambofreelance.webbackend.entities.PricingFaqEntity;
import com.cambofreelance.webbackend.entities.PricingFeatureEntity;
import com.cambofreelance.webbackend.entities.PricingPlanEntity;
import com.cambofreelance.webbackend.entities.PricingPlanFeatureEntity;
import com.cambofreelance.webbackend.entities.PricingPlanValueEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.PricingFaqRepository;
import com.cambofreelance.webbackend.repository.PricingFeatureRepository;
import com.cambofreelance.webbackend.repository.PricingPlanFeatureRepository;
import com.cambofreelance.webbackend.repository.PricingPlanRepository;
import com.cambofreelance.webbackend.repository.PricingPlanValueRepository;
import com.cambofreelance.webbackend.services.PricingService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final PricingPlanRepository        planRepository;
    private final PricingPlanFeatureRepository planFeatureRepository;
    private final PricingFeatureRepository     featureRepository;
    private final PricingPlanValueRepository   valueRepository;
    private final PricingFaqRepository         faqRepository;

    // ── Public ──────────────────────────────────────────────────────────────

    @Override
    public PublicPricingResponse getPublicPricing() {
        return PublicPricingResponse.builder()
            .plans(listPlans())
            .comparison(listFeatures())
            .faqs(listFaqs())
            .build();
    }

    // ── Plans ───────────────────────────────────────────────────────────────

    @Override
    public List<PricingPlanResponse> listPlans() {
        return planRepository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(p -> PricingPlanResponse.from(
                p, planFeatureRepository.findByPlanIdAndStatusOrderBySortOrderAsc(p.getId(), Constants.STATUS_ACTIVE)))
            .toList();
    }

    @Override
    public PricingPlanResponse getPlan(String id) {
        PricingPlanEntity plan = requirePlan(id);
        return PricingPlanResponse.from(
            plan, planFeatureRepository.findByPlanIdAndStatusOrderBySortOrderAsc(id, Constants.STATUS_ACTIVE));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "PRICING")
    public PricingPlanResponse createPlan(PricingPlanRequest request, String createdBy) {
        PricingPlanEntity plan = new PricingPlanEntity();
        plan.setId(UUID.randomUUID().toString());
        applyPlan(plan, request);
        plan.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        plan.setStatus(Constants.STATUS_ACTIVE);
        planRepository.save(plan);
        saveBullets(plan.getId(), request, createdBy);
        return getPlan(plan.getId());
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "PRICING", entityClass = PricingPlanEntity.class)
    public PricingPlanResponse updatePlan(String id, PricingPlanRequest request, String updatedBy) {
        PricingPlanEntity plan = requirePlan(id);
        applyPlan(plan, request);
        plan.setUpdatedBy(updatedBy);
        plan.setUpdatedAt(new Date());
        planRepository.save(plan);
        planFeatureRepository.deleteByPlanId(id);
        saveBullets(id, request, updatedBy);
        return getPlan(id);
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "PRICING", entityClass = PricingPlanEntity.class)
    public void deletePlan(String id) {
        PricingPlanEntity plan = requirePlan(id);
        plan.setStatus(Constants.STATUS_DELETE);
        planRepository.save(plan);
    }

    // ── Comparison features ─────────────────────────────────────────────────

    @Override
    public List<PricingFeatureResponse> listFeatures() {
        return featureRepository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(f -> PricingFeatureResponse.from(f, valueRepository.findByFeatureId(f.getId())))
            .toList();
    }

    @Override
    public PricingFeatureResponse getFeature(String id) {
        PricingFeatureEntity feature = requireFeature(id);
        return PricingFeatureResponse.from(feature, valueRepository.findByFeatureId(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "PRICING")
    public PricingFeatureResponse createFeature(PricingFeatureRequest request, String createdBy) {
        PricingFeatureEntity feature = new PricingFeatureEntity();
        feature.setId(UUID.randomUUID().toString());
        feature.setName(request.getName().trim());
        feature.setNameKh(request.getNameKh());
        feature.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        feature.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        feature.setStatus(Constants.STATUS_ACTIVE);
        featureRepository.save(feature);
        saveValues(feature.getId(), request);
        return getFeature(feature.getId());
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "PRICING", entityClass = PricingFeatureEntity.class)
    public PricingFeatureResponse updateFeature(String id, PricingFeatureRequest request, String updatedBy) {
        PricingFeatureEntity feature = requireFeature(id);
        feature.setName(request.getName().trim());
        feature.setNameKh(request.getNameKh());
        feature.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : feature.getSortOrder());
        feature.setUpdatedBy(updatedBy);
        feature.setUpdatedAt(new Date());
        featureRepository.save(feature);
        valueRepository.deleteByFeatureId(id);
        saveValues(id, request);
        return getFeature(id);
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "PRICING", entityClass = PricingFeatureEntity.class)
    public void deleteFeature(String id) {
        PricingFeatureEntity feature = requireFeature(id);
        feature.setStatus(Constants.STATUS_DELETE);
        featureRepository.save(feature);
    }

    // ── FAQs ────────────────────────────────────────────────────────────────

    @Override
    public List<PricingFaqResponse> listFaqs() {
        return faqRepository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(PricingFaqResponse::from)
            .toList();
    }

    @Override
    public PricingFaqResponse getFaq(String id) {
        return PricingFaqResponse.from(requireFaq(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "PRICING")
    public PricingFaqResponse createFaq(PricingFaqRequest request, String createdBy) {
        PricingFaqEntity entity = new PricingFaqEntity();
        entity.setId(UUID.randomUUID().toString());
        applyFaq(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        return PricingFaqResponse.from(faqRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "PRICING", entityClass = PricingFaqEntity.class)
    public PricingFaqResponse updateFaq(String id, PricingFaqRequest request, String updatedBy) {
        PricingFaqEntity entity = requireFaq(id);
        applyFaq(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : entity.getSortOrder());
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        return PricingFaqResponse.from(faqRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "PRICING", entityClass = PricingFaqEntity.class)
    public void deleteFaq(String id) {
        PricingFaqEntity entity = requireFaq(id);
        entity.setStatus(Constants.STATUS_DELETE);
        faqRepository.save(entity);
    }

    private void applyFaq(PricingFaqEntity entity, PricingFaqRequest request) {
        entity.setQuestion(request.getQuestion().trim());
        entity.setQuestionKh(request.getQuestionKh());
        entity.setAnswer(request.getAnswer().trim());
        entity.setAnswerKh(request.getAnswerKh());
    }

    private PricingFaqEntity requireFaq(String id) {
        return faqRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> notFound("PRICING_FAQ_NOT_FOUND", "FAQ not found: " + id));
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private void applyPlan(PricingPlanEntity plan, PricingPlanRequest r) {
        plan.setName(r.getName().trim());
        plan.setNameKh(r.getNameKh());
        plan.setDescription(r.getDescription());
        plan.setDescriptionKh(r.getDescriptionKh());
        plan.setPriceMonthly(r.getPriceMonthly() != null ? r.getPriceMonthly() : BigDecimal.ZERO);
        plan.setPriceYearly(r.getPriceYearly());
        plan.setCurrency(StringUtils.hasText(r.getCurrency()) ? r.getCurrency() : "$");
        plan.setIncludesNote(r.getIncludesNote());
        plan.setIncludesNoteKh(r.getIncludesNoteKh());
        plan.setBadge(r.getBadge());
        plan.setBadgeKh(r.getBadgeKh());
        plan.setHighlighted(Boolean.TRUE.equals(r.getHighlighted()));
        plan.setCtaLabel(r.getCtaLabel());
        plan.setCtaLabelKh(r.getCtaLabelKh());
        plan.setCtaLink(r.getCtaLink());
        plan.setSortOrder(r.getSortOrder() != null ? r.getSortOrder() : 0);
    }

    private void saveBullets(String planId, PricingPlanRequest request, String createdBy) {
        if (request.getFeatures() == null) return;
        int i = 0;
        for (PricingPlanRequest.Feature f : request.getFeatures()) {
            if (!StringUtils.hasText(f.getLabel())) continue;
            PricingPlanFeatureEntity b = new PricingPlanFeatureEntity();
            b.setId(UUID.randomUUID().toString());
            b.setPlanId(planId);
            b.setLabel(f.getLabel().trim());
            b.setLabelKh(f.getLabelKh());
            b.setSortOrder(f.getSortOrder() != null ? f.getSortOrder() : i);
            b.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
            b.setStatus(Constants.STATUS_ACTIVE);
            planFeatureRepository.save(b);
            i++;
        }
    }

    private void saveValues(String featureId, PricingFeatureRequest request) {
        if (request.getValues() == null) return;
        for (PricingFeatureRequest.Value v : request.getValues()) {
            if (!StringUtils.hasText(v.getPlanId())) continue;
            PricingPlanValueEntity pv = new PricingPlanValueEntity();
            pv.setId(UUID.randomUUID().toString());
            pv.setPlanId(v.getPlanId());
            pv.setFeatureId(featureId);
            pv.setValue(v.getValue());
            valueRepository.save(pv);
        }
    }

    private PricingPlanEntity requirePlan(String id) {
        return planRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> notFound("PRICING_PLAN_NOT_FOUND", "Plan not found: " + id));
    }

    private PricingFeatureEntity requireFeature(String id) {
        return featureRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> notFound("PRICING_FEATURE_NOT_FOUND", "Feature not found: " + id));
    }

    private AppException notFound(String code, String message) {
        AppException ex = new AppException(code, message);
        ex.setHttpStatus(HttpStatus.NOT_FOUND);
        return ex;
    }
}
