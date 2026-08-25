package com.cambofreelance.webbackend.configs.oauth2;

import com.cambofreelance.webbackend.dto.response.OAuthResponse;
import com.cambofreelance.webbackend.entities.UserEntity;
import com.cambofreelance.webbackend.services.OAuthAuthenticator;
import com.cambofreelance.webbackend.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Runs after Google's Authorization Code exchange succeeds. Verifies the email claim,
 * finds/creates the local account, mints our own JWT via the same path the password grant
 * uses, then 302-redirects the browser back to the caller's whitelisted redirect_uri with the
 * tokens in the URL fragment (never sent to any server, unlike a query string).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final OAuthAuthenticator oAuthAuthenticator;
    private final CookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Value("${app.oauth2.default-redirect-uri:http://localhost:3000/en/auth/google/callback}")
    private String defaultRedirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        String redirectUri = readCookie(request, CookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_COOKIE_NAME)
            .orElse(defaultRedirectUri);

        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            Boolean emailVerified = oAuth2User.getAttribute("email_verified");
            if (email == null || !Boolean.TRUE.equals(emailVerified)) {
                log.warn("Rejected Google login for unverified/missing email: {}", email);
                cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
                response.sendRedirect(redirectUri + "?error=email_not_verified");
                return;
            }

            UserEntity user = userService.findOrCreateGoogleUser(email);

            String deviceId = readCookie(request, CookieOAuth2AuthorizationRequestRepository.DEVICE_ID_COOKIE_NAME)
                .orElseGet(() -> UUID.randomUUID().toString());

            OAuthResponse tokens = oAuthAuthenticator.issueTokens(user, deviceId, request);

            String targetUrl = redirectUri + "#access_token=" + encode(tokens.getToken())
                + "&refresh_token=" + encode(tokens.getRefreshToken())
                + "&expires_in=" + tokens.getExpiresIn().getTime()
                + "&token_type=" + encode(tokens.getTokenType())
                + "&is_new_device=" + tokens.isNewDevice();

            cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
            response.sendRedirect(targetUrl);
        } catch (Exception e) {
            log.error("Google login failed", e);
            cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
            response.sendRedirect(redirectUri + "?error=google_auth_failed");
        }
    }

    private static Optional<String> readCookie(HttpServletRequest request, String name) {
        return CookieOAuth2AuthorizationRequestRepository.getCookie(request, name).map(Cookie::getValue);
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
