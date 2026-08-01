package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.PricingFaqRequest;
import com.cambofreelance.webbackend.dto.request.PricingFeatureRequest;
import com.cambofreelance.webbackend.dto.request.PricingPlanRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.PricingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PricingController {

    private final PricingService pricingService;

    // ── Public ────────────────────────────────────────────────────────────────

    @GetMapping("/pricing")
    public ResponseEntity<Object> publicPricing() {
        var result = pricingService.getPublicPricing();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin: Plans ────────────────────────────────────────────────────────────

    @GetMapping("/cms/pricing/plans")
    @PreAuthorize("hasAuthority('pricing.view')")
    public ResponseEntity<Object> listPlans() {
        return new ResponseEntity<>(new MessageResponse(pricingService.listPlans(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/pricing/plans/{id}")
    @PreAuthorize("hasAuthority('pricing.view')")
    public ResponseEntity<Object> getPlan(@PathVariable String id) {
        return new ResponseEntity<>(new MessageResponse(pricingService.getPlan(id), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/pricing/plans")
    @PreAuthorize("hasAuthority('pricing.create')")
    public ResponseEntity<Object> createPlan(
        @Valid @RequestBody PricingPlanRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        return new ResponseEntity<>(new MessageResponse(pricingService.createPlan(request, userId), ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/pricing/plans/{id}")
    @PreAuthorize("hasAuthority('pricing.update')")
    public ResponseEntity<Object> updatePlan(
        @PathVariable String id,
        @Valid @RequestBody PricingPlanRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        return new ResponseEntity<>(new MessageResponse(pricingService.updatePlan(id, request, userId), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/pricing/plans/{id}")
    @PreAuthorize("hasAuthority('pricing.delete')")
    public ResponseEntity<Object> deletePlan(@PathVariable String id) {
        pricingService.deletePlan(id);
        return new ResponseEntity<>(new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin: Comparison features ──────────────────────────────────────────────

    @GetMapping("/cms/pricing/features")
    @PreAuthorize("hasAuthority('pricing.view')")
    public ResponseEntity<Object> listFeatures() {
        return new ResponseEntity<>(new MessageResponse(pricingService.listFeatures(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/pricing/features/{id}")
    @PreAuthorize("hasAuthority('pricing.view')")
    public ResponseEntity<Object> getFeature(@PathVariable String id) {
        return new ResponseEntity<>(new MessageResponse(pricingService.getFeature(id), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/pricing/features")
    @PreAuthorize("hasAuthority('pricing.create')")
    public ResponseEntity<Object> createFeature(
        @Valid @RequestBody PricingFeatureRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        return new ResponseEntity<>(new MessageResponse(pricingService.createFeature(request, userId), ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/pricing/features/{id}")
    @PreAuthorize("hasAuthority('pricing.update')")
    public ResponseEntity<Object> updateFeature(
        @PathVariable String id,
        @Valid @RequestBody PricingFeatureRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        return new ResponseEntity<>(new MessageResponse(pricingService.updateFeature(id, request, userId), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/pricing/features/{id}")
    @PreAuthorize("hasAuthority('pricing.delete')")
    public ResponseEntity<Object> deleteFeature(@PathVariable String id) {
        pricingService.deleteFeature(id);
        return new ResponseEntity<>(new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin: FAQs ──────────────────────────────────────────────────────────────

    @GetMapping("/cms/pricing/faqs")
    @PreAuthorize("hasAuthority('pricing.view')")
    public ResponseEntity<Object> listFaqs() {
        return new ResponseEntity<>(new MessageResponse(pricingService.listFaqs(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/pricing/faqs/{id}")
    @PreAuthorize("hasAuthority('pricing.view')")
    public ResponseEntity<Object> getFaq(@PathVariable String id) {
        return new ResponseEntity<>(new MessageResponse(pricingService.getFaq(id), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/pricing/faqs")
    @PreAuthorize("hasAuthority('pricing.create')")
    public ResponseEntity<Object> createFaq(
        @Valid @RequestBody PricingFaqRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        return new ResponseEntity<>(new MessageResponse(pricingService.createFaq(request, userId), ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/pricing/faqs/{id}")
    @PreAuthorize("hasAuthority('pricing.update')")
    public ResponseEntity<Object> updateFaq(
        @PathVariable String id,
        @Valid @RequestBody PricingFaqRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId
    ) {
        return new ResponseEntity<>(new MessageResponse(pricingService.updateFaq(id, request, userId), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/cms/pricing/faqs/{id}")
    @PreAuthorize("hasAuthority('pricing.delete')")
    public ResponseEntity<Object> deleteFaq(@PathVariable String id) {
        pricingService.deleteFaq(id);
        return new ResponseEntity<>(new MessageResponse("Deleted successfully", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
