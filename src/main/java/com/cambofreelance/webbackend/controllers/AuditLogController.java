package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasAuthority('audit-log.view')")
    public ResponseEntity<Object> list(
        @RequestParam(required = false) String userId,
        @RequestParam(required = false) String action,
        @RequestParam(required = false) String module,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String dateFrom,
        @RequestParam(required = false) String dateTo,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        var result = auditLogService.list(userId, action, module, search, dateFrom, dateTo, page, size);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/purge")
    @PreAuthorize("hasAuthority('audit-log.purge')")
    public ResponseEntity<Object> purge(
        @RequestParam(defaultValue = "90") int days
    ) {
        int deleted = auditLogService.purge(days);
        return new ResponseEntity<>(
            new MessageResponse("Purged " + deleted + " audit log entries older than " + days + " days", ErrorCode.SUCCESS),
            HttpStatus.OK);
    }
}
