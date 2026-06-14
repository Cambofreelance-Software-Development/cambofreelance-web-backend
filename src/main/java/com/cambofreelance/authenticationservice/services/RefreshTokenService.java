package com.cambofreelance.authenticationservice.services;

import com.cambofreelance.authenticationservice.caches.TokenRedisCache;
import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.dto.SessionMetadataDto;
import com.cambofreelance.authenticationservice.dto.TokenCacheDto;
import com.cambofreelance.authenticationservice.entities.RefreshTokenEntity;
import com.cambofreelance.authenticationservice.entities.UserEntity;
import com.cambofreelance.authenticationservice.logger.exceptions.AppException;
import com.cambofreelance.authenticationservice.repository.RefreshTokenRepository;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenRedisCache tokenRedisCache;

    @Value("${authentication.jwtRefreshExpiration}")
    private Long refreshTokenDurationMs;

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByRefreshToken(token);
    }

    public RefreshTokenEntity createRefreshToken(String userId) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setUserId(userId);
        entity.setExpiredOn(Date.from(Instant.now().plusMillis(refreshTokenDurationMs)));
        entity.setRefreshToken(UUID.randomUUID().toString());
        entity = refreshTokenRepository.save(entity);
        return entity;
    }

    public RefreshTokenEntity createRefreshToken(String accessToken, String userId, String deviceId,
        String status, UserEntity userEntity) {
        RefreshTokenEntity entity = refreshTokenRepository
            .findByUserIdAndDeviceIdAndStatus(userId, deviceId, status)
            .orElse(null);

        if (entity == null) {
            entity = new RefreshTokenEntity();
            entity.setId(UUID.randomUUID().toString());
            entity.setUserId(userId);
            entity.setExpiredOn(Date.from(Instant.now().plusMillis(refreshTokenDurationMs)));
            entity.setRefreshToken(UUID.randomUUID().toString());
            entity.setDeviceId(deviceId);
            entity.setStatus(Constants.STATUS_ACTIVE);
            entity.setAccessToken(accessToken);
            entity.setClientId("web");
            entity = refreshTokenRepository.save(entity);
        } else {
            // rotate: revoke old tokens from Redis then update record
            tokenRedisCache.revokeAccessToken(entity.getAccessToken());
            tokenRedisCache.revokeRefreshToken(entity.getRefreshToken());
            entity.setAccessToken(accessToken);
            entity.setRefreshToken(UUID.randomUUID().toString());
            entity.setExpiredOn(Date.from(Instant.now().plusMillis(refreshTokenDurationMs)));
            entity = refreshTokenRepository.save(entity);
        }

        TokenCacheDto dto = buildTokenCacheDto(userEntity, deviceId);
        tokenRedisCache.storeRefreshToken(entity.getRefreshToken(), dto, refreshTokenDurationMs);
        return entity;
    }

    public RefreshTokenEntity createRefreshToken(String accessToken, String userId, String deviceId,
        String status, UserEntity userEntity, SessionMetadataDto metadata) {
        RefreshTokenEntity entity = refreshTokenRepository
            .findByUserIdAndDeviceIdAndStatus(userId, deviceId, status)
            .orElse(null);

        if (entity == null) {
            entity = new RefreshTokenEntity();
            entity.setId(UUID.randomUUID().toString());
            entity.setUserId(userId);
            entity.setExpiredOn(Date.from(Instant.now().plusMillis(refreshTokenDurationMs)));
            entity.setRefreshToken(UUID.randomUUID().toString());
            entity.setDeviceId(deviceId);
            entity.setStatus(Constants.STATUS_ACTIVE);
            entity.setAccessToken(accessToken);
            entity.setClientId("web");
            if (metadata != null) {
                entity.setIpAddress(metadata.getIpAddress());
                entity.setUserAgent(metadata.getUserAgent());
                entity.setBrowser(metadata.getBrowser());
                entity.setOsName(metadata.getOsName());
                entity.setDeviceType(metadata.getDeviceType());
                entity.setDeviceName(metadata.getDeviceName());
                entity.setNewDevice(metadata.isNewDevice());
                entity.setLastActiveAt(new Date());
            }
            entity = refreshTokenRepository.save(entity);
        } else {
            // rotate: revoke old tokens from Redis then update record
            tokenRedisCache.revokeAccessToken(entity.getAccessToken());
            tokenRedisCache.revokeRefreshToken(entity.getRefreshToken());
            entity.setAccessToken(accessToken);
            entity.setRefreshToken(UUID.randomUUID().toString());
            entity.setExpiredOn(Date.from(Instant.now().plusMillis(refreshTokenDurationMs)));
            entity.setLastActiveAt(new Date());
            entity.setNewDevice(false);
            if (metadata != null) {
                entity.setIpAddress(metadata.getIpAddress());
                entity.setUserAgent(metadata.getUserAgent());
                entity.setBrowser(metadata.getBrowser());
                entity.setOsName(metadata.getOsName());
                entity.setDeviceType(metadata.getDeviceType());
                entity.setDeviceName(metadata.getDeviceName());
            }
            entity = refreshTokenRepository.save(entity);
        }

        TokenCacheDto dto = buildTokenCacheDto(userEntity, deviceId);
        tokenRedisCache.storeRefreshToken(entity.getRefreshToken(), dto, refreshTokenDurationMs);
        return entity;
    }

    public RefreshTokenEntity getRefreshToken(String token) {
        // fast path: check Redis first
        TokenCacheDto cached = tokenRedisCache.getRefreshToken(token);
        if (cached == null) {
            log.debug("Refresh token not found in Redis, falling back to DB");
        }
        return refreshTokenRepository.findByRefreshTokenAndStatus(token, Constants.STATUS_ACTIVE)
            .orElse(null);
    }

    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiredOn().compareTo(Date.from(Instant.now())) < 0) {
            tokenRedisCache.revokeRefreshToken(token.getRefreshToken());
            tokenRedisCache.revokeAccessToken(token.getAccessToken());
            refreshTokenRepository.delete(token);
            throw new AppException(token.getRefreshToken(),
                "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public void revokeAllByUserId(String userId) {
        List<RefreshTokenEntity> sessions = refreshTokenRepository.findAllByUserIdAndStatus(
            userId, Constants.STATUS_ACTIVE);
        for (RefreshTokenEntity entity : sessions) {
            tokenRedisCache.revokeAccessToken(entity.getAccessToken());
            tokenRedisCache.revokeRefreshToken(entity.getRefreshToken());
            entity.setStatus(Constants.STATUS_DELETE);
            refreshTokenRepository.save(entity);
        }
        log.info("Revoked all {} session(s) for userId={}", sessions.size(), userId);
    }

    public void revokeByUserId(String userId, String deviceId) {
        refreshTokenRepository.findByUserIdAndDeviceIdAndStatus(userId, deviceId,
                Constants.STATUS_ACTIVE)
            .ifPresent(entity -> {
                tokenRedisCache.revokeAccessToken(entity.getAccessToken());
                tokenRedisCache.revokeRefreshToken(entity.getRefreshToken());
                entity.setStatus(Constants.STATUS_DELETE);
                refreshTokenRepository.save(entity);
                log.info("Revoked tokens for userId={} deviceId={}", userId, deviceId);
            });
    }

    private TokenCacheDto buildTokenCacheDto(UserEntity user, String deviceId) {
        return TokenCacheDto.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .deviceId(deviceId)
            .applicationId(user.getApplicationId())
            .userType(user.getUserType())
            .status(Constants.STATUS_ACTIVE)
            .build();
    }
}
