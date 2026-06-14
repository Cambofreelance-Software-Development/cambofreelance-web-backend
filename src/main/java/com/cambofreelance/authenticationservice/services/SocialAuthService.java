package com.cambofreelance.authenticationservice.services;

import com.cambofreelance.authenticationservice.constants.ErrorCode;
import com.cambofreelance.authenticationservice.logger.exceptions.AppException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocialAuthService {

    private final WebClient.Builder webClientBuilder;

    @Value("${oauth.google.client-id:}")
    private String googleClientId;

    @Value("${oauth.google.client-secret:}")
    private String googleClientSecret;

    @Value("${oauth.github.client-id:}")
    private String githubClientId;

    @Value("${oauth.github.client-secret:}")
    private String githubClientSecret;

    public SocialUserInfo fetchUserInfo(String provider, String code, String redirectUri) {
        return switch (provider.toLowerCase()) {
            case "google" -> fetchGoogleUserInfo(code, redirectUri);
            case "github" -> fetchGithubUserInfo(code, redirectUri);
            default -> throw new AppException(ErrorCode.BAD_REQUEST, "Unsupported provider: " + provider);
        };
    }

    private SocialUserInfo fetchGoogleUserInfo(String code, String redirectUri) {
        WebClient client = webClientBuilder.build();

        // Exchange code for access token
        @SuppressWarnings("unchecked")
        Map<String, Object> tokenResponse = client.post()
            .uri("https://oauth2.googleapis.com/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData("code", code)
                .with("client_id", googleClientId)
                .with("client_secret", googleClientSecret)
                .with("redirect_uri", redirectUri)
                .with("grant_type", "authorization_code"))
            .retrieve()
            .bodyToMono(Map.class)
            .block();

        if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
            log.error("Google token exchange failed: {}", tokenResponse);
            throw new AppException(ErrorCode.UNAUTHORIZED, "Google authentication failed");
        }

        String accessToken = (String) tokenResponse.get("access_token");

        // Fetch user info
        @SuppressWarnings("unchecked")
        Map<String, Object> userInfo = client.get()
            .uri("https://www.googleapis.com/oauth2/v3/userinfo")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(Map.class)
            .block();

        if (userInfo == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Failed to fetch Google user info");
        }

        return SocialUserInfo.builder()
            .providerId((String) userInfo.get("sub"))
            .email((String) userInfo.get("email"))
            .name((String) userInfo.getOrDefault("name", ""))
            .provider("google")
            .build();
    }

    private SocialUserInfo fetchGithubUserInfo(String code, String redirectUri) {
        WebClient client = webClientBuilder.build();

        // Exchange code for access token
        @SuppressWarnings("unchecked")
        Map<String, Object> tokenResponse = client.post()
            .uri("https://github.com/login/oauth/access_token")
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(Map.of(
                "client_id", githubClientId,
                "client_secret", githubClientSecret,
                "code", code,
                "redirect_uri", redirectUri
            ))
            .retrieve()
            .bodyToMono(Map.class)
            .block();

        if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
            log.error("GitHub token exchange failed: {}", tokenResponse);
            throw new AppException(ErrorCode.UNAUTHORIZED, "GitHub authentication failed");
        }

        String accessToken = (String) tokenResponse.get("access_token");

        // Fetch user info
        @SuppressWarnings("unchecked")
        Map<String, Object> userInfo = client.get()
            .uri("https://api.github.com/user")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .header(HttpHeaders.ACCEPT, "application/vnd.github+json")
            .retrieve()
            .bodyToMono(Map.class)
            .block();

        if (userInfo == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Failed to fetch GitHub user info");
        }

        String email = (String) userInfo.get("email");
        if (email == null || email.isBlank()) {
            email = fetchGithubPrimaryEmail(client, accessToken);
        }

        String name = (String) userInfo.getOrDefault("name", userInfo.getOrDefault("login", ""));
        Object idObj = userInfo.get("id");
        String providerId = idObj != null ? String.valueOf(idObj) : "";

        return SocialUserInfo.builder()
            .providerId(providerId)
            .email(email)
            .name(name)
            .provider("github")
            .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private String fetchGithubPrimaryEmail(WebClient client, String accessToken) {
        List<Map> rawList = client.get()
            .uri("https://api.github.com/user/emails")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .header(HttpHeaders.ACCEPT, "application/vnd.github+json")
            .retrieve()
            .bodyToFlux(Map.class)
            .collectList()
            .block();

        List<Map<String, Object>> emails = rawList == null ? java.util.Collections.emptyList()
            : rawList.stream().map(m -> (Map<String, Object>) m).toList();

        if (emails.isEmpty()) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "No verified email found in GitHub account");
        }

        return emails.stream()
            .filter(e -> Boolean.TRUE.equals(e.get("primary")) && Boolean.TRUE.equals(e.get("verified")))
            .map(e -> (String) e.get("email"))
            .findFirst()
            .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED, "No verified primary email in GitHub account"));
    }

    @lombok.Builder
    @lombok.Getter
    public static class SocialUserInfo {
        private final String provider;
        private final String providerId;
        private final String email;
        private final String name;
    }
}