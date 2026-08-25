package com.cambofreelance.webbackend.configs.oauth2;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

/**
 * Stores the in-flight OAuth2AuthorizationRequest (and the caller-supplied redirect_uri /
 * device_id) in short-lived cookies instead of the HttpSession, since this app runs fully
 * stateless (SessionCreationPolicy.STATELESS, no server-side session).
 */
@Component
@Slf4j
public class CookieOAuth2AuthorizationRequestRepository
    implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    static final String AUTH_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    static final String REDIRECT_URI_COOKIE_NAME = "oauth2_redirect_uri";
    static final String DEVICE_ID_COOKIE_NAME = "oauth2_device_id";
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    // Defaulted so a profile that omits app.oauth2.* (e.g. a bare/test context) doesn't fail to boot.
    @Value("${app.oauth2.authorized-redirect-uris:http://localhost:3000/en/auth/google/callback}")
    private List<String> authorizedRedirectUris;

    @Value("${app.oauth2.default-redirect-uri:http://localhost:3000/en/auth/google/callback}")
    private String defaultRedirectUri;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Optional<Cookie> cookie = getCookie(request, AUTH_REQUEST_COOKIE_NAME);
        if (cookie.isEmpty()) {
            log.warn("No {} cookie on {} — incoming state={}, code-present={}", AUTH_REQUEST_COOKIE_NAME,
                request.getRequestURI(), request.getParameter("state"), request.getParameter("code") != null);
            return null;
        }
        OAuth2AuthorizationRequest authorizationRequest = deserialize(cookie.get(), OAuth2AuthorizationRequest.class);
        log.info("Loaded authorization request, storedState={} incomingState={}", authorizationRequest.getState(),
            request.getParameter("state"));
        return authorizationRequest;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
        HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            deleteCookie(request, response, AUTH_REQUEST_COOKIE_NAME);
            deleteCookie(request, response, REDIRECT_URI_COOKIE_NAME);
            deleteCookie(request, response, DEVICE_ID_COOKIE_NAME);
            return;
        }

        log.info("Saving authorization request, state={}", authorizationRequest.getState());
        addCookie(response, AUTH_REQUEST_COOKIE_NAME, serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);

        String redirectUri = resolveRedirectUri(request.getParameter("redirect_uri"));
        addCookie(response, REDIRECT_URI_COOKIE_NAME, redirectUri, COOKIE_EXPIRE_SECONDS);

        String deviceId = request.getParameter("device_id");
        if (deviceId != null && !deviceId.isBlank()) {
            addCookie(response, DEVICE_ID_COOKIE_NAME, deviceId, COOKIE_EXPIRE_SECONDS);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
        HttpServletResponse response) {
        OAuth2AuthorizationRequest authorizationRequest = loadAuthorizationRequest(request);
        removeAuthorizationRequestCookies(request, response);
        return authorizationRequest;
    }

    void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        deleteCookie(request, response, AUTH_REQUEST_COOKIE_NAME);
        deleteCookie(request, response, REDIRECT_URI_COOKIE_NAME);
        deleteCookie(request, response, DEVICE_ID_COOKIE_NAME);
    }

    /**
     * Validates the caller-supplied redirect_uri against the configured whitelist so this
     * endpoint can't be used as an open redirect; falls back to the default on any mismatch.
     */
    private String resolveRedirectUri(String candidate) {
        if (candidate == null || candidate.isBlank()) {
            return defaultRedirectUri;
        }
        try {
            URI candidateUri = URI.create(candidate);
            boolean authorized = authorizedRedirectUris.stream().anyMatch(uri -> {
                URI allowed = URI.create(uri);
                return allowed.getHost().equalsIgnoreCase(candidateUri.getHost())
                    && allowed.getPort() == candidateUri.getPort()
                    && allowed.getScheme().equalsIgnoreCase(candidateUri.getScheme());
            });
            if (authorized) {
                return candidate;
            }
            log.warn("Rejected unauthorized OAuth2 redirect_uri: {}", candidate);
        } catch (IllegalArgumentException e) {
            log.warn("Malformed OAuth2 redirect_uri: {}", candidate);
        }
        return defaultRedirectUri;
    }

    static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies).filter(c -> c.getName().equals(name)).findFirst();
    }

    static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        getCookie(request, name).ifPresent(cookie -> {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        });
    }

    private static String serialize(Object object) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize((Serializable) object));
    }

    private static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
