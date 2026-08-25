package com.cambofreelance.webbackend.caches;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * OTP store for SMS providers that don't manage verification themselves (e.g. PlasGate, which
 * only sends text — unlike Twilio Verify, which generates and checks the code on its side).
 * Keyed by phone number since {@link com.cambofreelance.webbackend.services.SmsService} only
 * carries a phone number, not a userId.
 */
@Component
@RequiredArgsConstructor
public class PhoneOtpCache {

    private static final String OTP_PREFIX = "sms:otp:";

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${otp.ttl-minutes:5}")
    private long otpTtlMinutes;

    public void store(String phoneNumber, String otp) {
        redisTemplate.opsForValue().set(OTP_PREFIX + phoneNumber, otp, otpTtlMinutes, TimeUnit.MINUTES);
    }

    public String get(String phoneNumber) {
        Object val = redisTemplate.opsForValue().get(OTP_PREFIX + phoneNumber);
        return val != null ? val.toString() : null;
    }

    public void delete(String phoneNumber) {
        redisTemplate.delete(OTP_PREFIX + phoneNumber);
    }
}
