package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.BillingSettingsRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    /** Current user's invoices */
    @GetMapping("/subscriptions/invoices")
    public ResponseEntity<Object> myInvoices(@RequestHeader(Constants.USER_ID) String userId) {
        return new ResponseEntity<>(new MessageResponse(billingService.listMyInvoices(userId), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    /** Print-ready detail for one of the current user's invoices */
    @GetMapping("/subscriptions/invoices/{id}")
    public ResponseEntity<Object> myInvoiceDetail(
        @RequestHeader(Constants.USER_ID) String userId,
        @PathVariable String id
    ) {
        return new ResponseEntity<>(new MessageResponse(billingService.getMyInvoiceDetail(userId, id), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Admin ───────────────────────────────────────────────────────────────

    @GetMapping("/cms/invoices")
    @PreAuthorize("hasAuthority('invoice.view')")
    public ResponseEntity<Object> listInvoices(
        @RequestParam(required = false) String invoiceStatus,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return new ResponseEntity<>(new MessageResponse(billingService.adminListInvoices(invoiceStatus, page, size), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/invoices/{id}")
    @PreAuthorize("hasAuthority('invoice.view')")
    public ResponseEntity<Object> invoiceDetail(@PathVariable String id) {
        return new ResponseEntity<>(new MessageResponse(billingService.adminGetInvoiceDetail(id), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/billing/settings")
    @PreAuthorize("hasAuthority('billing.settings')")
    public ResponseEntity<Object> getSettings() {
        return new ResponseEntity<>(new MessageResponse(billingService.getSettings(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/cms/billing/settings")
    @PreAuthorize("hasAuthority('billing.settings')")
    public ResponseEntity<Object> updateSettings(
        @RequestBody BillingSettingsRequest request,
        @RequestHeader(value = Constants.USER_ID, required = false) String adminId
    ) {
        return new ResponseEntity<>(new MessageResponse(billingService.updateSettings(request, adminId), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/billing/summary")
    @PreAuthorize("hasAuthority('invoice.view')")
    public ResponseEntity<Object> summary() {
        return new ResponseEntity<>(new MessageResponse(billingService.summary(), ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
