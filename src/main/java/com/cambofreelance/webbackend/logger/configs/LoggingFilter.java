package com.cambofreelance.webbackend.logger.configs;

import com.cambofreelance.webbackend.logger.contants.enums.AppLoggerMode;
import com.cambofreelance.webbackend.logger.dto.AppLogger;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.logger.utils.LoggerUtils;
//import io.opentelemetry.api.trace.Span;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "applogger.enabled", havingValue = "true")
public class LoggingFilter extends OncePerRequestFilter {

    private static final int MAX_LOG_SIZE = 2048;

    private final AppLogger appLogger;

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        if (contentAsByteArray == null || contentAsByteArray.length == 0) return "";
        try {
            return new String(contentAsByteArray,
                (characterEncoding != null ? characterEncoding : StandardCharsets.UTF_8).toString());
        } catch (Exception e) {
            appLogger.setParam("Error in LoggingFilter.getStringValue()", e.getMessage());
            return new String(contentAsByteArray, StandardCharsets.UTF_8);
        }
    }

    private String truncate(String str) {
        if (str == null) return "";
        return str.length() > MAX_LOG_SIZE ? str.substring(0, MAX_LOG_SIZE) + "...[truncated]" : str;
    }

    private boolean isBinaryContent(HttpServletResponse response) {
        String ct = response.getContentType();
        if (ct == null) return false;
        return ct.startsWith("image/") || ct.startsWith("video/") || ct.startsWith("audio/")
            || ct.equals("application/octet-stream") || ct.equals("application/pdf");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain)
        throws ServletException, IOException {

        RequestWrapper requestWrapper = new RequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String uri = request.getRequestURI();
        List<String> unTrackUri = Arrays.asList(
            "/actuator/health/readiness",
            "/actuator/health/liveness"
        );

        Instant start = Instant.now();

        try {
            String requestBody = truncate(StringUtils.trim(requestWrapper.getBody()));

            appLogger.setUrl(request.getRequestURI());
            appLogger.setHttpMethod(request.getMethod());
            appLogger.setRequest(LoggerUtils.maskSensitiveData(requestBody));
            appLogger.setServiceName(LoggerUtils.getServiceName(request));
            appLogger.setRemoteAddress(request.getRemoteAddr());

            // Bind trace/span IDs into MDC
//            Span currentSpan = Span.current();
//            if (currentSpan != null && currentSpan.getSpanContext().isValid()) {
//                MDC.put("traceId", currentSpan.getSpanContext().getTraceId());
//                MDC.put("spanId", currentSpan.getSpanContext().getSpanId());
//            }

            filterChain.doFilter(requestWrapper, responseWrapper);

        } catch (Exception ex) {
            // Unwrap NestedServletException to check if the root cause is a handled business error
            Throwable root = (ex instanceof ServletException se && se.getRootCause() != null)
                    ? se.getRootCause() : ex;
            if (root instanceof AppException appEx) {
                // Business exception that slipped past @ControllerAdvice (e.g., its own handler threw).
                // Write a minimal JSON error response directly so the client gets a proper 4xx, not a 500.
                if (!responseWrapper.isCommitted()) {
                    int statusCode = appEx.getHttpStatus() != null ? appEx.getHttpStatus().value() : 400;
                    String msg = appEx.getMessage() != null ? appEx.getMessage() : appEx.getErrorCode();
                    // Escape any quotes in msg to keep JSON valid
                    String safeMsgForJson = msg.replace("\"", "\\\"");
                    String body = "{\"code\":\"" + appEx.getErrorCode()
                            + "\",\"success\":false,\"message\":\"" + safeMsgForJson
                            + "\",\"timestamp\":" + System.currentTimeMillis() + ",\"data\":null}";
                    responseWrapper.setStatus(statusCode);
                    responseWrapper.setContentType("application/json;charset=UTF-8");
                    responseWrapper.getWriter().write(body);
                    responseWrapper.getWriter().flush();
                    log.warn("AppException escaped @ControllerAdvice [{}]: {}", appEx.getErrorCode(), msg);
                }
                // Do not re-throw — response is written; finally block will copy it
            } else {
                appLogger.setException(ex);
                throw ex;
            }

        } finally {
            Instant end = Instant.now();

            String responseBody;
            if (isBinaryContent(response)) {
                int size = responseWrapper.getContentAsByteArray().length;
                responseBody = "[binary: " + response.getContentType() + ", " + size + " bytes]";
            } else {
                responseBody = truncate(getStringValue(
                    responseWrapper.getContentAsByteArray(),
                    response.getCharacterEncoding()
                ));
                responseBody = LoggerUtils.maskSensitiveData(responseBody);
            }

            appLogger.setResponse(responseBody);
            appLogger.setDuration(LoggerUtils.durationToTimer(start, end));

            if (unTrackUri.stream().noneMatch(uri::equals)) {
                switch (AppLoggerMode.getFromProperty()) {
                    case FULL, PARTIAL -> LoggerUtils.logRequestResponse(appLogger);
                    case FULL_PARTIAL -> LoggerUtils.logResponse(appLogger);
                    default -> log.debug("AppLoggerMode not configured, skipping logs.");
                }
            }

            responseWrapper.copyBodyToResponse();

            // Cleanup MDC to avoid leaking across threads
            MDC.remove("traceId");
            MDC.remove("spanId");
        }
    }
}
