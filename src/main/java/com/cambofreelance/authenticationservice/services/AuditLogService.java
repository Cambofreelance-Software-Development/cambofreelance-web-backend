package com.cambofreelance.authenticationservice.services;

import com.cambofreelance.authenticationservice.dto.response.AuditLogResponse;
import org.springframework.data.domain.Page;

public interface AuditLogService {

    void log(String userId, String username, String action, String module,
             String entityId, String description,
             String oldValue, String newValue,
             String ipAddress, String userAgent);

    Page<AuditLogResponse> list(String userId, String action, String module,
                                String search, String dateFrom, String dateTo,
                                int page, int size);

    int purge(int retentionDays);
}
