package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.dto.request.SubscriptionCreateRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.SubscriptionService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/cms/subscriptions/dashboard")
    @PreAuthorize("hasAuthority('subscriptions.view')")
    public ResponseEntity<Object> dashboard() {
        var result = subscriptionService.getDashboardStats();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/subscriptions")
    @PreAuthorize("hasAuthority('subscriptions.view')")
    public ResponseEntity<Object> listAll(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String paymentStatus,
        @RequestParam(required = false) String tenantId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = subscriptionService.listAll(status, paymentStatus, tenantId, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/tenants/{tenantId}/subscriptions")
    @PreAuthorize("hasAuthority('subscriptions.view')")
    public ResponseEntity<Object> history(@PathVariable String tenantId) {
        var result = subscriptionService.listHistoryForTenant(tenantId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/tenants/{tenantId}/subscriptions/current")
    @PreAuthorize("hasAuthority('subscriptions.view')")
    public ResponseEntity<Object> current(@PathVariable String tenantId) {
        var result = subscriptionService.getCurrentForTenant(tenantId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/tenants/{tenantId}/subscriptions")
    @PreAuthorize("hasAuthority('subscriptions.create')")
    public ResponseEntity<Object> subscribe(
        @PathVariable String tenantId,
        @Valid @RequestBody SubscriptionCreateRequest request
    ) {
        var result = subscriptionService.subscribe(tenantId, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PutMapping("/cms/tenants/{tenantId}/subscriptions/upgrade")
    @PreAuthorize("hasAuthority('subscriptions.update')")
    public ResponseEntity<Object> upgrade(
        @PathVariable String tenantId,
        @Valid @RequestBody SubscriptionCreateRequest request
    ) {
        var result = subscriptionService.upgrade(tenantId, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/cms/tenants/{tenantId}/subscriptions/downgrade")
    @PreAuthorize("hasAuthority('subscriptions.update')")
    public ResponseEntity<Object> downgrade(
        @PathVariable String tenantId,
        @Valid @RequestBody SubscriptionCreateRequest request
    ) {
        var result = subscriptionService.downgrade(tenantId, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/subscriptions/{id}/renew")
    @PreAuthorize("hasAuthority('subscriptions.update')")
    public ResponseEntity<Object> renew(@PathVariable String id) {
        var result = subscriptionService.renew(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/subscriptions/{id}/suspend")
    @PreAuthorize("hasAuthority('subscriptions.update')")
    public ResponseEntity<Object> suspend(@PathVariable String id) {
        var result = subscriptionService.suspend(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/subscriptions/{id}/resume")
    @PreAuthorize("hasAuthority('subscriptions.update')")
    public ResponseEntity<Object> resume(@PathVariable String id) {
        var result = subscriptionService.resume(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/cms/subscriptions/{id}/payment-status")
    @PreAuthorize("hasAuthority('subscriptions.update')")
    public ResponseEntity<Object> updatePaymentStatus(
        @PathVariable String id,
        @RequestBody Map<String, String> body
    ) {
        var result = subscriptionService.updatePaymentStatus(id, body.getOrDefault("paymentStatus", ""));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/cms/subscriptions/{id}/auto-renewal")
    @PreAuthorize("hasAuthority('subscriptions.update')")
    public ResponseEntity<Object> updateAutoRenewal(
        @PathVariable String id,
        @RequestBody Map<String, Boolean> body
    ) {
        var result = subscriptionService.updateAutoRenewal(id, Boolean.TRUE.equals(body.get("autoRenewal")));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/subscriptions/{id}/cancel")
    @PreAuthorize("hasAuthority('subscriptions.cancel')")
    public ResponseEntity<Object> cancel(@PathVariable String id) {
        var result = subscriptionService.cancel(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
