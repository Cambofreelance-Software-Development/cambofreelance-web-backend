package com.cambofreelance.webbackend.caches;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/** OTP store for the register-time verification step. Keyed by channel (PHONE/EMAIL) + userId so a user can verify via either without the two colliding. */
@Component
@RequiredArgsConstructor
public class RegisterOtpCache {

    private static final String OTP_PREFIX       = "register:otp:";
    private static final String COOLDOWN_PREFIX  = "register:otp:cooldown:";
    private static final String DAILY_COUNT_PREFIX = "register:otp:daily:";

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${otp.ttl-minutes:5}")
    private long otpTtlMinutes;

    @Value("${otp.resend-cooldown-seconds:60}")
    private long resendCooldownSeconds;

    @Value("${otp.daily-limit:5}")
    private long dailyLimit;

    public void store(String channel, String userId, String otp) {
        redisTemplate.opsForValue().set(key(OTP_PREFIX, channel, userId), otp, otpTtlMinutes, TimeUnit.MINUTES);
    }

    public String get(String channel, String userId) {
        Object val = redisTemplate.opsForValue().get(key(OTP_PREFIX, channel, userId));
        return val != null ? val.toString() : null;
    }

    public void delete(String channel, String userId) {
        redisTemplate.delete(key(OTP_PREFIX, channel, userId));
    }

    /** True if a code was already sent on this channel within the cooldown window. */
    public boolean isOnCooldown(String channel, String userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key(COOLDOWN_PREFIX, channel, userId)));
    }

    public void markSent(String channel, String userId) {
        redisTemplate.opsForValue().set(key(COOLDOWN_PREFIX, channel, userId), "1", resendCooldownSeconds, TimeUnit.SECONDS);
    }

    /** True once a channel has already been sent {@code otp.daily-limit} times within the last 24h. */
    public boolean isDailySendLimitExceeded(String channel, String userId) {
        Object val = redisTemplate.opsForValue().get(key(DAILY_COUNT_PREFIX, channel, userId));
        long count = val != null ? Long.parseLong(val.toString()) : 0;
        return count >= dailyLimit;
    }

    /** Increments the rolling 24h send counter, starting a fresh window on the first send. */
    public void incrementDailySendCount(String channel, String userId) {
        String redisKey = key(DAILY_COUNT_PREFIX, channel, userId);
        Long count = redisTemplate.opsForValue().increment(redisKey);
        if (count != null && count == 1L) {
            redisTemplate.expire(redisKey, 1, TimeUnit.DAYS);
        }
    }

    private static String key(String prefix, String channel, String userId) {
        return prefix + channel + ":" + userId;
    }
}
