package com.cambofreelance.webbackend.caches;

import java.time.YearMonth;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/** Tracks authenticated API requests per tenant per calendar month for usage monitoring. */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ApiUsageRedisCache {

    private static final String PREFIX = "API_USAGE:";
    private static final long TTL_DAYS = 60;

    private final StringRedisTemplate stringRedisTemplate;

    public void increment(String tenantId) {
        if (!StringUtils.hasText(tenantId)) return;
        String key = buildKey(tenantId, YearMonth.now());
        try {
            Long count = stringRedisTemplate.opsForValue().increment(key);
            if (count != null && count == 1L) {
                stringRedisTemplate.expire(key, TTL_DAYS, TimeUnit.DAYS);
            }
        } catch (DataAccessException ex) {
            log.error("Failed to increment API usage counter for tenantId={}", tenantId, ex);
        }
    }

    public long getMonthlyCount(String tenantId, YearMonth yearMonth) {
        if (!StringUtils.hasText(tenantId)) return 0;
        try {
            String value = stringRedisTemplate.opsForValue().get(buildKey(tenantId, yearMonth));
            return value == null ? 0 : Long.parseLong(value);
        } catch (DataAccessException ex) {
            log.error("Failed to read API usage counter for tenantId={}", tenantId, ex);
            return 0;
        }
    }

    private String buildKey(String tenantId, YearMonth yearMonth) {
        return PREFIX + tenantId + ":" + yearMonth;
    }
}
