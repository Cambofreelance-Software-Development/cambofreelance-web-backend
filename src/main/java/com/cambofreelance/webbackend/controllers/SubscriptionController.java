package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.AutoRenewToggleRequest;
import com.cambofreelance.webbackend.dto.request.SubscriptionCheckoutRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // ── User ────────────────────────────────────────────────────────────────

    @PostMapping("/subscriptions/checkout")
    public ResponseEntity<Object> checkout(
        @RequestHeader(value = Constants.USER_ID) String userId,
        @Valid @RequestBody SubscriptionCheckoutRequest request
    ) {
        var result = subscriptionService.createCheckout(userId, request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @GetMapping("/subscriptions/me")
    public ResponseEntity<Object> mySubscription(
        @RequestHeader(value = Constants.USER_ID) String userId
    ) {
        var result = subscriptionService.getMySubscription(userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/subscriptions/payments")
    public ResponseEntity<Object> myPayments(
        @RequestHeader(value = Constants.USER_ID) String userId
    ) {
        var result = subscriptionService.getMyPayments(userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    /** Poll from the payment success page until the transaction settles. */
    @GetMapping("/subscriptions/transactions/{tranId}/status")
    public ResponseEntity<Object> transactionStatus(
        @RequestHeader(value = Constants.USER_ID) String userId,
        @PathVariable String tranId
    ) {
        var result = subscriptionService.checkTransaction(userId, tranId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    /** Self-service opt in/out of Card-on-File auto-renewal on the caller's active subscription. */
    @PutMapping("/subscriptions/me/auto-renew")
    public ResponseEntity<Object> toggleAutoRenew(
        @RequestHeader(value = Constants.USER_ID) String userId,
        @Valid @RequestBody AutoRenewToggleRequest request
    ) {
        var result = subscriptionService.setAutoRenew(userId, Boolean.TRUE.equals(request.getAutoRenew()));
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin ───────────────────────────────────────────────────────────────

    @GetMapping("/cms/subscriptions")
    @PreAuthorize("hasAuthority('subscription.view')")
    public ResponseEntity<Object> adminListSubscriptions(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = subscriptionService.adminListSubscriptions(page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/subscriptions/payments")
    @PreAuthorize("hasAuthority('subscription.view')")
    public ResponseEntity<Object> adminListPayments(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = subscriptionService.adminListPayments(page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    /** Manual verification — admin settles a payment PayWay could not confirm automatically. */
    @PutMapping("/cms/payments/{tranId}/verify")
    @PreAuthorize("hasAuthority('payment.verify')")
    public ResponseEntity<Object> manualVerify(
        @PathVariable String tranId,
        @Valid @RequestBody com.cambofreelance.webbackend.dto.request.ManualVerifyRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String adminId
    ) {
        var result = subscriptionService.manualVerify(tranId, Boolean.TRUE.equals(request.getApprove()), request.getNote(), adminId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    /** Payment workflow log for one transaction */
    @GetMapping("/cms/payments/{tranId}/logs")
    @PreAuthorize("hasAuthority('subscription.view')")
    public ResponseEntity<Object> paymentLogs(@PathVariable String tranId) {
        return new ResponseEntity<>(new MessageResponse(subscriptionService.getPaymentLogs(tranId), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    /** Admin override — e.g. a support request to turn off auto-renew for a customer. */
    @PutMapping("/cms/subscriptions/{subscriptionId}/auto-renew")
    @PreAuthorize("hasAuthority('subscription.manage')")
    public ResponseEntity<Object> adminToggleAutoRenew(
        @PathVariable String subscriptionId,
        @Valid @RequestBody AutoRenewToggleRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String adminId
    ) {
        var result = subscriptionService.adminSetAutoRenew(subscriptionId, Boolean.TRUE.equals(request.getAutoRenew()), adminId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
