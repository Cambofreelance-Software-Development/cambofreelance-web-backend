package com.cambofreelance.webbackend.configs.oauth2;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * Handles OAuth2 authentication failures.
 *
 * <p>This handler is called when the OAuth2 authorization code flow fails,
 * for example:
 *
 * <ul>
 *     <li>Invalid or expired state</li>
 *     <li>User denied Google consent</li>
 *     <li>Authorization code is invalid or already used</li>
 *     <li>Google token exchange fails</li>
 *     <li>OAuth2 authentication fails</li>
 * </ul>
 *
 * <p>The handler redirects the user to the configured frontend callback
 * instead of exposing Google's raw exception details.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationFailureHandler
    implements AuthenticationFailureHandler {

    private static final String ERROR_PARAM = "error";
    private static final String ERROR_CODE = "google_auth_failed";

    private static final Set<String> ALLOWED_HOSTS = Set.of(
        "localhost",
        "127.0.0.1"
    );

    private final CookieOAuth2AuthorizationRequestRepository
        cookieAuthorizationRequestRepository;

    @Value("${app.oauth2.default-redirect-uri:http://localhost:3000/en/auth/google/callback}")
    private String defaultRedirectUri;

    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException {

        String requestUri = request.getRequestURI();
        String state = request.getParameter("state");
        String code = request.getParameter("code");

        log.error(
            "Google OAuth2 authentication failed. " +
                "requestUri={}, statePresent={}, codePresent={}, " +
                "exceptionType={}, message={}",
            requestUri,
            state != null,
            code != null,
            exception.getClass().getSimpleName(),
            exception.getMessage(),
            exception
        );

        /*
         * Resolve and validate the frontend redirect URI before
         * deleting the OAuth cookies.
         */
        String redirectUri = resolveRedirectUri(request);

        /*
         * Always remove OAuth authorization cookies after a failed
         * authentication attempt.
         */
        try {
            cookieAuthorizationRequestRepository
                .removeAuthorizationRequestCookies(request, response);
        } catch (Exception e) {
            log.warn(
                "Failed to remove OAuth2 authorization request cookies",
                e
            );
        }

        /*
         * Build the redirect URL safely.
         */
        String redirectUrl = appendQueryParameter(
            redirectUri,
            ERROR_PARAM,
            ERROR_CODE
        );

        log.info(
            "Redirecting failed Google OAuth2 login to frontend: {}",
            redirectUri
        );

        response.sendRedirect(redirectUrl);
    }

    /**
     * Resolves the frontend redirect URI from the OAuth2 cookie.
     *
     * <p>The cookie value must be validated before it is used for
     * redirection to prevent an open redirect vulnerability.
     */
    private String resolveRedirectUri(HttpServletRequest request) {

        String redirectUri =
            CookieOAuth2AuthorizationRequestRepository
                .getCookie(
                    request,
                    CookieOAuth2AuthorizationRequestRepository
                        .REDIRECT_URI_COOKIE_NAME
                )
                .map(Cookie::getValue)
                .orElse(null);

        if (redirectUri == null || redirectUri.isBlank()) {
            log.debug(
                "OAuth2 redirect URI cookie not found. Using default redirect URI."
            );

            return defaultRedirectUri;
        }

        if (!isAllowedRedirectUri(redirectUri)) {
            log.warn(
                "Rejected invalid OAuth2 redirect URI: {}. " +
                    "Using default redirect URI instead.",
                redirectUri
            );

            return defaultRedirectUri;
        }

        return redirectUri;
    }

    /**
     * Validates that the redirect URI points to an allowed frontend.
     *
     * <p>For production, configure the allowed origins from application
     * properties instead of relying only on localhost.
     */
    private boolean isAllowedRedirectUri(String redirectUri) {

        try {
            URI uri = new URI(redirectUri);

            String scheme = uri.getScheme();
            String host = uri.getHost();

            if (scheme == null || host == null) {
                return false;
            }

            /*
             * Only HTTP/HTTPS are allowed.
             */
            if (!scheme.equalsIgnoreCase("http")
                && !scheme.equalsIgnoreCase("https")) {
                return false;
            }

            /*
             * Prevent credentials inside the redirect URI:
             *
             * https://user:password@example.com/...
             */
            if (uri.getUserInfo() != null) {
                return false;
            }

            /*
             * Development validation.
             *
             * Replace this with configured frontend origins
             * for production.
             */
            return ALLOWED_HOSTS.contains(host.toLowerCase());

        } catch (URISyntaxException e) {

            log.warn(
                "Invalid OAuth2 redirect URI syntax: {}",
                redirectUri
            );

            return false;
        }
    }

    /**
     * Appends a query parameter without creating an invalid URL.
     *
     * <p>Examples:
     *
     * <pre>
     * /callback
     *     -> /callback?error=google_auth_failed
     *
     * /callback?lang=en
     *     -> /callback?lang=en&error=google_auth_failed
     * </pre>
     */
    private String appendQueryParameter(
        String redirectUri,
        String parameter,
        String value
    ) {

        String separator;

        if (redirectUri.contains("?")) {

            if (redirectUri.endsWith("?")
                || redirectUri.endsWith("&")) {
                separator = "";
            } else {
                separator = "&";
            }

        } else {
            separator = "?";
        }

        return redirectUri
            + separator
            + parameter
            + "="
            + value;
    }
}