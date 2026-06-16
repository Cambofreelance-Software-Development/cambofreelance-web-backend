package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.caches.ApiUsageRedisCache;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.response.UsageMetric;
import com.cambofreelance.webbackend.dto.response.UsageMetricsResponse;
import com.cambofreelance.webbackend.entities.SubscriptionEntity;
import com.cambofreelance.webbackend.entities.SubscriptionPackageEntity;
import com.cambofreelance.webbackend.repository.SubscriptionPackageRepository;
import com.cambofreelance.webbackend.repository.SubscriptionRepository;
import com.cambofreelance.webbackend.repository.UserRepository;
import com.cambofreelance.webbackend.services.UsageMetricsService;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsageMetricsServiceImpl implements UsageMetricsService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPackageRepository packageRepository;
    private final UserRepository userRepository;
    private final ApiUsageRedisCache apiUsageRedisCache;

    @Override
    public UsageMetricsResponse getUsageForTenant(String tenantId) {
        SubscriptionPackageEntity pkg = subscriptionRepository
            .findByTenantIdAndStatus(tenantId, Constants.STATUS_ACTIVE)
            .map(SubscriptionEntity::getPackageId)
            .flatMap(packageRepository::findById)
            .orElse(null);

        long activeUsers = userRepository.countByTenantIdAndStatus(tenantId, Constants.STATUS_ACTIVE);
        long apiRequests = apiUsageRedisCache.getMonthlyCount(tenantId, YearMonth.now());

        return UsageMetricsResponse.builder()
            .tenantId(tenantId)
            .packageName(pkg != null ? pkg.getName() : null)
            .totalCustomers(UsageMetric.builder()
                .current(0)
                .limit(pkg != null ? pkg.getMaxCustomers() : null)
                .tracked(false)
                .build())
            .totalLoans(UsageMetric.builder()
                .current(0)
                .limit(pkg != null ? pkg.getMaxLoans() : null)
                .tracked(false)
                .build())
            .activeUsers(UsageMetric.builder()
                .current(activeUsers)
                .limit(pkg != null ? pkg.getMaxUsers() : null)
                .tracked(true)
                .build())
            .storageUsage(UsageMetric.builder()
                .current(0)
                .limit(null)
                .tracked(false)
                .build())
            .apiRequests(UsageMetric.builder()
                .current(apiRequests)
                .limit(null)
                .tracked(true)
                .build())
            .build();
    }
}
