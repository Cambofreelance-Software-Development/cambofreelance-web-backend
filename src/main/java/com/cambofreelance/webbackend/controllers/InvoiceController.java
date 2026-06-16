package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.dto.request.GenerateInvoiceRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/cms/invoices")
    @PreAuthorize("hasAuthority('invoices.view')")
    public ResponseEntity<Object> listAll(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String tenantId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = invoiceService.listAll(status, tenantId, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/invoices/{id}")
    @PreAuthorize("hasAuthority('invoices.view')")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        var result = invoiceService.getById(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/cms/tenants/{tenantId}/invoices")
    @PreAuthorize("hasAuthority('invoices.view')")
    public ResponseEntity<Object> listForTenant(@PathVariable String tenantId) {
        var result = invoiceService.listForTenant(tenantId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/cms/tenants/{tenantId}/invoices/generate")
    @PreAuthorize("hasAuthority('invoices.generate')")
    public ResponseEntity<Object> generate(
        @PathVariable String tenantId,
        @RequestBody(required = false) GenerateInvoiceRequest request
    ) {
        GenerateInvoiceRequest req = request != null ? request : new GenerateInvoiceRequest();
        var result = invoiceService.generateInvoice(tenantId, req.getBillingPeriod(), req.getTax());
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @PostMapping("/cms/invoices/{id}/pay")
    @PreAuthorize("hasAuthority('invoices.pay')")
    public ResponseEntity<Object> recordPayment(@PathVariable String id) {
        var result = invoiceService.recordPayment(id);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
