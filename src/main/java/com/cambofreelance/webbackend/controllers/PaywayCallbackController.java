package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.SubscriptionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Unauthenticated server-to-server pushback from ABA PayWay after a payment.
 * The payload is treated as a hint only — SubscriptionService re-verifies the
 * transaction against PayWay before changing any state, and always answers 200
 * so PayWay does not keep retrying.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class PaywayCallbackController {

    private final SubscriptionService subscriptionService;
    private final ObjectMapper objectMapper;

    @PostMapping("/payway/callback")
    public ResponseEntity<Object> callback(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> {
            if (v != null && v.length > 0) {
                params.put(k, v[0]);
            }
        });

        String rawBody = "";
        try {
            rawBody = StreamUtils.copyToString(request.getInputStream(), java.nio.charset.StandardCharsets.UTF_8);
            // JSON pushback variant: merge body fields into params
            if (params.isEmpty() && rawBody.trim().startsWith("{")) {
                JsonNode node = objectMapper.readTree(rawBody);
                node.fields().forEachRemaining(e -> {
                    if (e.getValue().isValueNode()) {
                        params.put(e.getKey(), e.getValue().asText());
                    }
                });
            }
        } catch (Exception e) {
            log.warn("[PayWay] failed to read callback body", e);
        }

        String rawPayload = params.isEmpty() ? rawBody : params.toString();
        log.info("[PayWay] callback received: {}", rawPayload);

        try {
            subscriptionService.handlePaywayCallback(params, rawPayload);
        } catch (Exception e) {
            // Never bubble errors back to PayWay — reconciliation/polling will settle it
            log.error("[PayWay] callback processing failed", e);
        }
        return new ResponseEntity<>(new MessageResponse("OK", ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
