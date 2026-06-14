package com.cambofreelance.authenticationservice.services.impl;

import com.cambofreelance.authenticationservice.caches.TokenRedisCache;
import com.cambofreelance.authenticationservice.constants.Constants;
import com.cambofreelance.authenticationservice.dto.response.SessionResponse;
import com.cambofreelance.authenticationservice.entities.RefreshTokenEntity;
import com.cambofreelance.authenticationservice.logger.contants.ErrorCode;
import com.cambofreelance.authenticationservice.logger.exceptions.AppException;
import com.cambofreelance.authenticationservice.repository.RefreshTokenRepository;
import com.cambofreelance.authenticationservice.services.SessionService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionServiceImpl implements SessionService {

    private final RefreshTokenRepository repository;
    private final TokenRedisCache tokenRedisCache;

    @Override
    public List<SessionResponse> getMyActiveSessions(String userId, String currentDeviceId) {
        return repository.findAllByUserIdAndStatus(userId, Constants.STATUS_ACTIVE)
            .stream()
            .map(e -> toResponse(e, currentDeviceId))
            .collect(Collectors.toList());
    }

    @Override
    public void revokeMySession(String sessionId, String requestingUserId) {
        RefreshTokenEntity entity = repository.findById(sessionId)
            .orElseThrow(() -> new AppException(ErrorCode.GENERAL_ERROR, "Session not found"));
        if (!entity.getUserId().equals(requestingUserId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "You are not authorized to revoke this session");
        }
        revokeEntity(entity);
    }

    @Override
    public void revokeAllOtherSessions(String userId, String currentDeviceId) {
        List<RefreshTokenEntity> sessions = repository.findAllByUserIdAndStatus(userId, Constants.STATUS_ACTIVE);
        for (RefreshTokenEntity entity : sessions) {
            if (!entity.getDeviceId().equals(currentDeviceId)) {
                revokeEntity(entity);
            }
        }
    }

    @Override
    public List<SessionResponse> adminGetUserSessions(String userId) {
        return repository.findAllByUserIdAndStatus(userId, Constants.STATUS_ACTIVE)
            .stream()
            .map(e -> toResponse(e, null))
            .collect(Collectors.toList());
    }

    @Override
    public void adminRevokeSession(String sessionId) {
        RefreshTokenEntity entity = repository.findById(sessionId)
            .orElseThrow(() -> new AppException(ErrorCode.GENERAL_ERROR, "Session not found"));
        revokeEntity(entity);
    }

    @Override
    public void adminRevokeAllSessions(String userId) {
        List<RefreshTokenEntity> sessions = repository.findAllByUserIdAndStatus(userId, Constants.STATUS_ACTIVE);
        for (RefreshTokenEntity entity : sessions) {
            revokeEntity(entity);
        }
        log.info("Admin revoked all {} session(s) for userId={}", sessions.size(), userId);
    }

    private void revokeEntity(RefreshTokenEntity entity) {
        tokenRedisCache.revokeAccessToken(entity.getAccessToken());
        tokenRedisCache.revokeRefreshToken(entity.getRefreshToken());
        entity.setStatus(Constants.STATUS_DELETE);
        repository.save(entity);
        log.info("Revoked session id={} for userId={}", entity.getId(), entity.getUserId());
    }

    private SessionResponse toResponse(RefreshTokenEntity e, String currentDeviceId) {
        return SessionResponse.builder()
            .sessionId(e.getId())
            .deviceId(e.getDeviceId())
            .deviceName(e.getDeviceName() != null ? e.getDeviceName() : "Unknown Device")
            .deviceType(e.getDeviceType() != null ? e.getDeviceType() : "UNKNOWN")
            .browser(e.getBrowser())
            .os(e.getOsName())
            .ipAddress(e.getIpAddress())
            .lastActiveAt(e.getLastActiveAt())
            .createdAt(e.getCreatedAt())
            .trusted(e.isTrusted())
            .current(currentDeviceId != null && currentDeviceId.equals(e.getDeviceId()))
            .build();
    }
}
