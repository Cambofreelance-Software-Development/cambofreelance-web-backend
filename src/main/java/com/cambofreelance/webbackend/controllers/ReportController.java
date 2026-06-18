package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/customer-list")
    @PreAuthorize("hasAuthority('reports.view')")
    public ResponseEntity<Object> customerList(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId
    ) {
        requireTenantId(tenantId);
        return ok(reportService.customerList());
    }

    @GetMapping("/active-loans")
    @PreAuthorize("hasAuthority('reports.view')")
    public ResponseEntity<Object> activeLoans(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId
    ) {
        requireTenantId(tenantId);
        return ok(reportService.activeLoans());
    }

    @GetMapping("/closed-loans")
    @PreAuthorize("hasAuthority('reports.view')")
    public ResponseEntity<Object> closedLoans(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId
    ) {
        requireTenantId(tenantId);
        return ok(reportService.closedLoans());
    }

    @GetMapping("/loan-portfolio")
    @PreAuthorize("hasAuthority('reports.view')")
    public ResponseEntity<Object> loanPortfolio(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId
    ) {
        requireTenantId(tenantId);
        return ok(reportService.loanPortfolio());
    }

    @GetMapping("/daily-collection")
    @PreAuthorize("hasAuthority('reports.view')")
    public ResponseEntity<Object> dailyCollection(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) String dateFrom,
        @RequestParam(required = false) String dateTo
    ) {
        requireTenantId(tenantId);
        return ok(reportService.dailyCollection(dateFrom, dateTo));
    }

    @GetMapping("/monthly-collection")
    @PreAuthorize("hasAuthority('reports.view')")
    public ResponseEntity<Object> monthlyCollection(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) Integer year
    ) {
        requireTenantId(tenantId);
        return ok(reportService.monthlyCollection(year));
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasAuthority('reports.view')")
    public ResponseEntity<Object> overdueReport(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId
    ) {
        requireTenantId(tenantId);
        return ok(reportService.overdueReport());
    }

    @GetMapping("/interest-income")
    @PreAuthorize("hasAuthority('reports.view')")
    public ResponseEntity<Object> interestIncome(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) Integer year
    ) {
        requireTenantId(tenantId);
        return ok(reportService.interestIncome(year));
    }

    @GetMapping("/service-fee-income")
    @PreAuthorize("hasAuthority('reports.view')")
    public ResponseEntity<Object> serviceFeeIncome(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestParam(required = false) Integer year
    ) {
        requireTenantId(tenantId);
        return ok(reportService.serviceFeeIncome(year));
    }

    @GetMapping("/outstanding-balance")
    @PreAuthorize("hasAuthority('reports.view')")
    public ResponseEntity<Object> outstandingBalance(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId
    ) {
        requireTenantId(tenantId);
        return ok(reportService.outstandingBalance());
    }

    private ResponseEntity<Object> ok(Object data) {
        return ResponseEntity.ok(new MessageResponse(data, ErrorCode.SUCCESS));
    }

    private void requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException("TENANT_ID_REQUIRED", "X-Tenant-ID header is required");
        }
    }
}
