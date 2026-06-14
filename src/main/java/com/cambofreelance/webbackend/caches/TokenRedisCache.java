package com.cambofreelance.webbackend.caches;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.TokenCacheDto;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class TokenRedisCache {

    private final RedisTemplate<String, TokenCacheDto> tokenRedisTemplate;

    public TokenRedisCache(
        @Qualifier("tokenRedisTemplate") RedisTemplate<String, TokenCacheDto> tokenRedisTemplate) {
        this.tokenRedisTemplate = tokenRedisTemplate;
    }

    public void storeAccessToken(String token, TokenCacheDto dto, long ttlMs) {
        String key = Constants.REDIS_ACCESS_TOKEN_PREFIX + token;
        try {
            tokenRedisTemplate.opsForValue().set(key, dto, ttlMs, TimeUnit.MILLISECONDS);
            log.debug("Stored access token in Redis for userId={}", dto.getUserId());
        } catch (DataAccessException ex) {
            log.error("Failed to store access token in Redis for userId={}", dto.getUserId(), ex);
        }
    }

    public void storeRefreshToken(String token, TokenCacheDto dto, long ttlMs) {
        String key = Constants.REDIS_REFRESH_TOKEN_PREFIX + token;
        try {
            tokenRedisTemplate.opsForValue().set(key, dto, ttlMs, TimeUnit.MILLISECONDS);
            log.debug("Stored refresh token in Redis for userId={}", dto.getUserId());
        } catch (DataAccessException ex) {
            log.error("Failed to store refresh token in Redis for userId={}", dto.getUserId(), ex);
        }
    }

    public TokenCacheDto getAccessToken(String token) {
        String key = Constants.REDIS_ACCESS_TOKEN_PREFIX + token;
        try {
            return tokenRedisTemplate.opsForValue().get(key);
        } catch (DataAccessException ex) {
            log.error("Failed to get access token from Redis", ex);
            return null;
        }
    }

    public TokenCacheDto getRefreshToken(String token) {
        String key = Constants.REDIS_REFRESH_TOKEN_PREFIX + token;
        try {
            return tokenRedisTemplate.opsForValue().get(key);
        } catch (DataAccessException ex) {
            log.error("Failed to get refresh token from Redis", ex);
            return null;
        }
    }

    public void revokeAccessToken(String token) {
        String key = Constants.REDIS_ACCESS_TOKEN_PREFIX + token;
        try {
            tokenRedisTemplate.delete(key);
            log.debug("Revoked access token from Redis");
        } catch (DataAccessException ex) {
            log.error("Failed to revoke access token from Redis", ex);
        }
    }

    public void revokeRefreshToken(String token) {
        String key = Constants.REDIS_REFRESH_TOKEN_PREFIX + token;
        try {
            tokenRedisTemplate.delete(key);
            log.debug("Revoked refresh token from Redis");
        } catch (DataAccessException ex) {
            log.error("Failed to revoke refresh token from Redis", ex);
        }
    }

    public boolean isAccessTokenActive(String token) {
        String key = Constants.REDIS_ACCESS_TOKEN_PREFIX + token;
        try {
            return Boolean.TRUE.equals(tokenRedisTemplate.hasKey(key));
        } catch (DataAccessException ex) {
            log.error("Failed to check access token existence in Redis", ex);
            return false;
        }
    }
}
