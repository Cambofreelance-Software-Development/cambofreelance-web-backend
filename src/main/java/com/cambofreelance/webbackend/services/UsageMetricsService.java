package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.response.UsageMetricsResponse;

public interface UsageMetricsService {

    UsageMetricsResponse getUsageForTenant(String tenantId);
}
