package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.RepaymentInstallmentUpdateRequest;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.RepaymentScheduleExportService;
import com.cambofreelance.webbackend.services.RepaymentScheduleService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me/loan-applications")
@RequiredArgsConstructor
public class RepaymentScheduleController {

    private final RepaymentScheduleService repaymentScheduleService;
    private final RepaymentScheduleExportService exportService;

    @PostMapping("/{loanApplicationId}/schedule")
    @PreAuthorize("hasAuthority('repayment-schedule.generate')")
    public ResponseEntity<Object> generate(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String loanApplicationId
    ) {
        requireTenantId(tenantId);
        var result = repaymentScheduleService.generate(loanApplicationId, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.CREATED);
    }

    @GetMapping("/{loanApplicationId}/schedule")
    @PreAuthorize("hasAuthority('loan-applications.view')")
    public ResponseEntity<Object> getSchedule(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String loanApplicationId
    ) {
        requireTenantId(tenantId);
        var result = repaymentScheduleService.getSchedule(loanApplicationId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{loanApplicationId}/schedule/recalculate")
    @PreAuthorize("hasAuthority('repayment-schedule.generate')")
    public ResponseEntity<Object> recalculate(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String loanApplicationId
    ) {
        requireTenantId(tenantId);
        var result = repaymentScheduleService.recalculate(loanApplicationId, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/{loanApplicationId}/schedule/installments/{installmentId}")
    @PreAuthorize("hasAuthority('repayment-schedule.update')")
    public ResponseEntity<Object> updateInstallment(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String loanApplicationId,
        @PathVariable String installmentId,
        @Valid @RequestBody RepaymentInstallmentUpdateRequest request
    ) {
        requireTenantId(tenantId);
        var result = repaymentScheduleService.updateInstallment(installmentId, request, userId);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/{loanApplicationId}/schedule/mark-overdue")
    @PreAuthorize("hasAuthority('repayment-schedule.update')")
    public ResponseEntity<Object> markOverdue(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @RequestHeader(value = Constants.USER_ID, required = false) String userId,
        @PathVariable String loanApplicationId
    ) {
        requireTenantId(tenantId);
        int count = repaymentScheduleService.markOverdue(loanApplicationId, userId);
        return new ResponseEntity<>(
            new MessageResponse(Map.of("markedCount", count), ErrorCode.SUCCESS),
            HttpStatus.OK
        );
    }

    @GetMapping("/{loanApplicationId}/schedule/export/xlsx")
    @PreAuthorize("hasAuthority('loan-applications.view')")
    public ResponseEntity<byte[]> exportXlsx(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String loanApplicationId
    ) {
        requireTenantId(tenantId);
        var schedule = repaymentScheduleService.getSchedule(loanApplicationId);
        byte[] body = exportService.toXlsx(schedule);
        return downloadResponse(body, schedule.getLoanNumber(), "xlsx",
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    }

    @GetMapping("/{loanApplicationId}/schedule/export/docx")
    @PreAuthorize("hasAuthority('loan-applications.view')")
    public ResponseEntity<byte[]> exportDocx(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String loanApplicationId
    ) {
        requireTenantId(tenantId);
        var schedule = repaymentScheduleService.getSchedule(loanApplicationId);
        byte[] body = exportService.toDocx(schedule);
        return downloadResponse(body, schedule.getLoanNumber(), "docx",
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
    }

    @GetMapping("/{loanApplicationId}/schedule/export/pdf")
    @PreAuthorize("hasAuthority('loan-applications.view')")
    public ResponseEntity<byte[]> exportPdf(
        @RequestHeader(value = Constants.TENANT_ID, required = false) String tenantId,
        @PathVariable String loanApplicationId
    ) {
        requireTenantId(tenantId);
        var schedule = repaymentScheduleService.getSchedule(loanApplicationId);
        byte[] body = exportService.toPdf(schedule);
        return downloadResponse(body, schedule.getLoanNumber(), "pdf", MediaType.APPLICATION_PDF);
    }

    private ResponseEntity<byte[]> downloadResponse(byte[] body, String loanNumber,
                                                    String extension, MediaType mediaType) {
        String safeLoan = StringUtils.hasText(loanNumber) ? loanNumber.replaceAll("[^A-Za-z0-9_.-]", "_") : "schedule";
        String filename = "repayment-schedule-" + safeLoan + "." + extension;
        return ResponseEntity.ok()
            .contentType(mediaType)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .contentLength(body.length)
            .body(body);
    }

    private void requireTenantId(String tenantId) {
        if (!StringUtils.hasText(tenantId)) {
            throw new AppException("NO_TENANT_ASSIGNED", "Your account is not assigned to a tenant");
        }
    }
}
