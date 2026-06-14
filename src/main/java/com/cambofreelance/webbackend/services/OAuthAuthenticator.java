package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.caches.TokenRedisCache;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.ErrorCode;
import com.cambofreelance.webbackend.dto.SessionMetadataDto;
import com.cambofreelance.webbackend.dto.TokenCacheDto;
import com.cambofreelance.webbackend.dto.request.OAuthRequest;
import com.cambofreelance.webbackend.dto.response.OAuthResponse;
import com.cambofreelance.webbackend.entities.RefreshTokenEntity;
import com.cambofreelance.webbackend.entities.UserEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.RefreshTokenRepository;
import com.cambofreelance.webbackend.utils.JwtUtils;
import com.cambofreelance.webbackend.utils.UserAgentParser;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthAuthenticator {

    private final RefreshTokenService refreshTokenService;
    private final TokenRedisCache tokenRedisCache;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final SocialAuthService socialAuthService;
    private final UserAgentParser userAgentParser;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${authentication.jwtExpiration}")
    private long jwtExpirationMs;

    @Value("${authentication.jwtRefreshExpiration}")
    private long jwtRefreshExpirationMs;

    @Value("${session.max-concurrent:5}")
    private int maxConcurrentSessions;

    public OAuthResponse createToken(OAuthRequest request, HttpServletRequest httpRequest) throws AppException {
        OAuthResponse response = new OAuthResponse();
        Date issuedAt = new Date();

        if (request.getGrantType().equals(Constants.PASSWORD)) {
            log.info("Creating token for user: {}", request.getUsername());
            UserEntity user = userService.authUser(request);
            if (user == null) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "User not found");
            }
            if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
            }

            // New device detection
            boolean isKnownDevice = refreshTokenRepository.existsByUserIdAndDeviceId(
                user.getUserId(), request.getDeviceId());

            // Concurrent session control (only for new devices)
            if (!isKnownDevice) {
                List<RefreshTokenEntity> activeSessions = refreshTokenRepository
                    .findAllByUserIdAndStatus(user.getUserId(), Constants.STATUS_ACTIVE);
                if (activeSessions.size() >= maxConcurrentSessions) {
                    RefreshTokenEntity oldest = activeSessions.stream()
                        .min(Comparator.comparing(e -> e.getCreatedAt()))
                        .orElseThrow();
                    tokenRedisCache.revokeAccessToken(oldest.getAccessToken());
                    tokenRedisCache.revokeRefreshToken(oldest.getRefreshToken());
                    oldest.setStatus(Constants.STATUS_DELETE);
                    refreshTokenRepository.save(oldest);
                    log.info("Evicted oldest session for userId={} to enforce max={}", user.getUserId(), maxConcurrentSessions);
                }
            }

            // Build metadata
            SessionMetadataDto metadata = buildSessionMetadata(httpRequest, !isKnownDevice);

            String accessToken = jwtUtils.generateJwtToken(user, issuedAt, request.getDeviceId());
            RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(
                accessToken, user.getUserId(), request.getDeviceId(), Constants.STATUS_ACTIVE, user, metadata);

            // store access token in Redis with TTL
            TokenCacheDto dto = buildTokenCacheDto(user, request.getDeviceId());
            tokenRedisCache.storeAccessToken(accessToken, dto, jwtExpirationMs);

            response.setToken(accessToken);
            response.setRefreshToken(refreshToken.getRefreshToken());
            response.setExpiresIn(issuedAt);
            response.setNewDevice(!isKnownDevice);

        } else if (request.getGrantType().equals(Constants.REFRESH_TOKEN)) {
            log.info("Refreshing token with refresh_token: {}", request.getRefreshToken());
            RefreshTokenEntity existingRefresh = refreshTokenService.getRefreshToken(
                request.getRefreshToken());
            if (existingRefresh == null) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "Refresh token not found");
            }
            refreshTokenService.verifyExpiration(existingRefresh);

            UserEntity user = userService.getUserById(existingRefresh.getUserId());
            if (user == null) {
                throw new AppException(ErrorCode.UNAUTHORIZED, "User not found");
            }

            // revoke the old access token from Redis
            tokenRedisCache.revokeAccessToken(existingRefresh.getAccessToken());

            SessionMetadataDto metadata = buildSessionMetadata(httpRequest, false);

            String accessToken = jwtUtils.generateJwtToken(user, issuedAt, request.getDeviceId());
            RefreshTokenEntity newRefresh = refreshTokenService.createRefreshToken(
                accessToken, user.getUserId(), request.getDeviceId(), Constants.STATUS_ACTIVE, user, metadata);

            // store new access token in Redis
            TokenCacheDto dto = buildTokenCacheDto(user, request.getDeviceId());
            tokenRedisCache.storeAccessToken(accessToken, dto, jwtExpirationMs);

            response.setToken(accessToken);
            response.setRefreshToken(newRefresh.getRefreshToken());
            response.setExpiresIn(issuedAt);

        } else if (request.getGrantType().equals(Constants.SOCIAL)) {
            log.info("Social login via provider: {}", request.getProvider());
            if (request.getProvider() == null || request.getCode() == null) {
                throw new AppException(ErrorCode.BAD_REQUEST, "provider and code are required for social login");
            }

            SocialAuthService.SocialUserInfo info = socialAuthService.fetchUserInfo(
                request.getProvider(), request.getCode(), request.getRedirectUri());

            UserEntity user = userService.findOrCreateSocialUser(info);

            SessionMetadataDto metadata = buildSessionMetadata(httpRequest, true);

            String accessToken = jwtUtils.generateJwtToken(user, issuedAt, request.getDeviceId());
            RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(
                accessToken, user.getUserId(), request.getDeviceId(), Constants.STATUS_ACTIVE, user, metadata);

            TokenCacheDto dto = buildTokenCacheDto(user, request.getDeviceId());
            tokenRedisCache.storeAccessToken(accessToken, dto, jwtExpirationMs);

            response.setToken(accessToken);
            response.setRefreshToken(refreshToken.getRefreshToken());
            response.setExpiresIn(issuedAt);
            response.setNewDevice(true);

        } else {
            log.error("Invalid grant type: {}", request.getGrantType());
            throw new AppException(ErrorCode.BAD_REQUEST, "Unsupported grant type");
        }

        response.setTokenType(Constants.BEARER);
        response.setScope(List.of("read", "write"));
        return response;
    }

    public void revokeToken(String accessToken, String userId, String deviceId) {
        tokenRedisCache.revokeAccessToken(accessToken);
        refreshTokenService.revokeByUserId(userId, deviceId);
    }

    private SessionMetadataDto buildSessionMetadata(HttpServletRequest req, boolean isNewDevice) {
        String ua = req.getHeader("User-Agent");
        UserAgentParser.ParsedUserAgent parsed = userAgentParser.parse(ua);
        String ip = Optional.ofNullable(req.getHeader("X-Forwarded-For"))
            .map(h -> h.split(",")[0].trim())
            .orElse(req.getRemoteAddr());
        return SessionMetadataDto.builder()
            .ipAddress(ip)
            .userAgent(ua)
            .browser(parsed.getBrowser())
            .osName(parsed.getOs())
            .deviceType(parsed.getDeviceType())
            .deviceName(parsed.getDeviceName())
            .newDevice(isNewDevice)
            .build();
    }

    private TokenCacheDto buildTokenCacheDto(UserEntity user, String deviceId) {
        return TokenCacheDto.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .deviceId(deviceId)
            .applicationId(user.getApplicationId())
            .userType(user.getUserType())
            .status(Constants.STATUS_ACTIVE)
            .permissions(userService.getPermissionCodes(user.getUserId()))
            .build();
    }
}
