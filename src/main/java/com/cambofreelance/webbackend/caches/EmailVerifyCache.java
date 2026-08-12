package com.cambofreelance.webbackend.caches;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailVerifyCache {

    private static final String PREFIX  = "email:verify:";
    private static final long   TTL_MIN = 15;

    private final RedisTemplate<String, Object> redisTemplate;

    public void store(String userId, String otp) {
        redisTemplate.opsForValue().set(PREFIX + userId, otp, TTL_MIN, TimeUnit.MINUTES);
    }

    public String get(String userId) {
        Object val = redisTemplate.opsForValue().get(PREFIX + userId);
        return val != null ? val.toString() : null;
    }

    public void delete(String userId) {
        redisTemplate.delete(PREFIX + userId);
    }
}
