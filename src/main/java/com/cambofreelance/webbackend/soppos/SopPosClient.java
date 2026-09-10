package com.cambofreelance.webbackend.soppos;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Thin client for the SOP POS System's Client Registration API — the service that provisions and
 * updates a customer's actual POS tenant. Every method is server-to-server, authenticated with a
 * static {@code X-Api-Key} header.
 *
 * <ul>
 *   <li>{@code POST /api/registration} — register a new client for a subscription (returns the
 *       tenant's access details: clientCode, backendUrl, emenuUrl, rootUser, rootPassword)</li>
 *   <li>{@code PATCH /api/registration/{id}} — update an existing client's plan and/or validity
 *       period; {@code {id}} is the same UUID passed as {@code id} on the original POST</li>
 * </ul>
 *
 * Inert until {@code soppos.enabled=true} and both {@code soppos.base-url} / {@code soppos.api-key}
 * are set — {@link #isEnabled()} is checked by the caller before any sync is attempted.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SopPosClient {

    public static final String REGISTRATION_PATH = "/api/registration";

    /** SOP POS accepts only these plan tier codes (see the API's plan reference table). */
    public static final Set<Integer> VALID_PLAN_CODES = Set.of(0, 9, 19, 25, 39, 99, 150, 200, 250);

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(20);

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${soppos.enabled:false}")
    private boolean enabled;

    @Value("${soppos.base-url:}")
    private String baseUrl;

    @Value("${soppos.api-key:}")
    private String apiKey;

    public boolean isEnabled() {
        return enabled && StringUtils.hasText(baseUrl) && StringUtils.hasText(apiKey);
    }

    /** Access details returned by the registration API. */
    public record RegistrationResult(String clientCode, String backendUrl, String emenuUrl,
                                     String rootUser, String rootPassword) {}

    /**
     * Registers a new POS client for a subscription. {@code subscriptionId} is sent as the
     * registration {@code id} so later {@link #updatePlanAndPeriod} calls address the same client.
     */
    public RegistrationResult register(String subscriptionId, int planCode, Date periodStart, Date periodEnd) {
        requirePlanCode(planCode);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("id", subscriptionId);
        body.put("planName", String.valueOf(planCode));
        if (periodStart != null) body.put("currentPeriodStart", iso(periodStart));
        if (periodEnd != null) body.put("currentPeriodEnd", iso(periodEnd));

        JsonNode root = send(HttpMethod.POST, REGISTRATION_PATH, body, subscriptionId);
        return new RegistrationResult(
            text(root, "clientCode"),
            text(root, "backendUrl"),
            firstNonBlank(text(root, "ememuUrl"), text(root, "emenuUrl")),
            text(root, "rootUser"),
            text(root, "rootPassword"));
    }

    /**
     * Updates an existing POS client's plan tier and validity window (renewal or plan change) and
     * returns the client's current access details as echoed back by the API.
     */
    public RegistrationResult updatePlanAndPeriod(String registrationId, int planCode, Date periodStart, Date periodEnd) {
        requirePlanCode(planCode);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("planName", String.valueOf(planCode));
        if (periodStart != null) body.put("currentPeriodStart", iso(periodStart));
        if (periodEnd != null) body.put("currentPeriodEnd", iso(periodEnd));

        JsonNode root = send(HttpMethod.PATCH, REGISTRATION_PATH + "/" + registrationId, body, registrationId);
        return new RegistrationResult(
            text(root, "clientCode"),
            text(root, "backendUrl"),
            firstNonBlank(text(root, "ememuUrl"), text(root, "emenuUrl")),
            text(root, "rootUser"),
            text(root, "rootPassword"));
    }

    private void requirePlanCode(int planCode) {
        if (!VALID_PLAN_CODES.contains(planCode)) {
            throw new IllegalArgumentException("Plan code " + planCode
                + " is not a valid SOP POS plan (allowed: " + VALID_PLAN_CODES + ")");
        }
    }

    private JsonNode send(HttpMethod method, String path, Map<String, Object> body, String ref) {
        if (!isEnabled()) {
            throw new IllegalStateException("SOP POS integration is not configured (soppos.base-url / soppos.api-key)");
        }
        String response;
        try {
            response = webClient.method(method)
                .uri(baseUrl + path)
                .header("X-Api-Key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();
        } catch (WebClientResponseException e) {
            log.warn("[SopPos] {} {} HTTP {} ref={}: {}", method, path, e.getStatusCode(), ref, e.getResponseBodyAsString());
            throw new IllegalStateException("SOP POS API returned " + e.getStatusCode().value() + " — "
                + extractMessage(e.getResponseBodyAsString()), e);
        } catch (Exception e) {
            log.error("[SopPos] {} {} call failed ref={}", method, path, ref, e);
            throw new IllegalStateException("SOP POS API is unreachable: " + e.getMessage(), e);
        }
        log.info("[SopPos] {} {} ref={} response={}", method, path, ref, abbreviate(response));
        try {
            return objectMapper.readTree(StringUtils.hasText(response) ? response : "{}");
        } catch (Exception e) {
            throw new IllegalStateException("SOP POS API returned an unparseable response", e);
        }
    }

    private String extractMessage(String responseBody) {
        if (!StringUtils.hasText(responseBody)) return "no response body";
        try {
            JsonNode node = objectMapper.readTree(responseBody);
            if (node.hasNonNull("message")) return node.get("message").asText();
        } catch (Exception ignored) {
            // fall through
        }
        return abbreviate(responseBody);
    }

    private static String iso(Date d) {
        return DateTimeFormatter.ISO_INSTANT.format(d.toInstant());
    }

    private static String text(JsonNode node, String field) {
        return node != null && node.hasNonNull(field) ? node.get(field).asText() : null;
    }

    private static String firstNonBlank(String a, String b) {
        return StringUtils.hasText(a) ? a : b;
    }

    private static String abbreviate(String s) {
        if (s == null) return null;
        return s.length() > 300 ? s.substring(0, 300) + "..." : s;
    }
}
