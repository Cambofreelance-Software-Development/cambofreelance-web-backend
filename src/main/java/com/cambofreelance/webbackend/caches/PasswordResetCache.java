package com.cambofreelance.webbackend.caches;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PasswordResetCache {

    private static final String PREFIX  = "pwd:reset:";
    private static final long   TTL_MIN = 15;

    private final RedisTemplate<String, Object> redisTemplate;

    public void store(String email, String otp) {
        redisTemplate.opsForValue().set(PREFIX + email, otp, TTL_MIN, TimeUnit.MINUTES);
    }

    public String get(String email) {
        Object val = redisTemplate.opsForValue().get(PREFIX + email);
        return val != null ? val.toString() : null;
    }

    public void delete(String email) {
        redisTemplate.delete(PREFIX + email);
    }
}
