package com.cambofreelance.webbackend.audit;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.services.AuditLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint pjp, Auditable auditable) throws Throwable {
        String oldValue = captureOldValue(auditable, pjp.getArgs());
        Object result   = pjp.proceed();
        try {
            String userId    = extractUserId();
            String username  = extractUsername();
            String ipAddress = extractIp();
            String userAgent = extractUserAgent();
            String entityId  = extractEntityId(result, pjp.getArgs());
            String newValue  = captureNewValue(result);
            String description = buildDescription(auditable, pjp, entityId);
            auditLogService.log(userId, username, auditable.action(), auditable.module(),
                entityId, description, oldValue, newValue, ipAddress, userAgent);
        } catch (Exception e) {
            log.warn("Audit aspect error: {}", e.getMessage());
        }
        return result;
    }

    // ── Old value (fetched before method runs) ────────────────────────────────

    private String captureOldValue(Auditable auditable, Object[] args) {
        if (auditable.entityClass() == Void.class) return null;
        String entityId = firstStringArg(args);
        if (entityId == null) return null;
        try {
            Object entity = entityManager.find(auditable.entityClass(), entityId);
            return entity != null ? objectMapper.writeValueAsString(entity) : null;
        } catch (Exception e) {
            log.debug("Could not capture old value: {}", e.getMessage());
            return null;
        }
    }

    // ── New value (serialised return object) ──────────────────────────────────

    private String captureNewValue(Object result) {
        if (result == null || result instanceof Page<?>) return null;
        try {
            return objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            return null;
        }
    }

    // ── Auth / request helpers ─────────────────────────────────────────────────

    private String extractUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : null;
    }

    private String extractUsername() {
        try {
            HttpServletRequest req = currentRequest();
            return req != null ? req.getHeader(Constants.CLIENT_USER_NAME) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String extractIp() {
        try {
            HttpServletRequest req = currentRequest();
            if (req == null) return null;
            String forwarded = req.getHeader("X-Forwarded-For");
            if (forwarded != null && !forwarded.isBlank()) return forwarded.split(",")[0].trim();
            return req.getRemoteAddr();
        } catch (Exception e) {
            return null;
        }
    }

    private String extractUserAgent() {
        try {
            HttpServletRequest req = currentRequest();
            return req != null ? req.getHeader("User-Agent") : null;
        } catch (Exception e) {
            return null;
        }
    }

    // ── Entity ID extraction ──────────────────────────────────────────────────

    private String extractEntityId(Object result, Object[] args) {
        if (result != null && !(result instanceof Page<?>)) {
            String id = tryGetId(result, "getId");
            if (id != null) return id;
            id = tryGetId(result, "getUserId");
            if (id != null) return id;
            id = tryGetId(result, "getRoleId");
            if (id != null) return id;
        }
        return firstStringArg(args);
    }

    private String firstStringArg(Object[] args) {
        if (args == null) return null;
        for (Object arg : args) {
            if (arg instanceof String s && !s.isBlank()) return s;
        }
        return null;
    }

    private String tryGetId(Object obj, String methodName) {
        try {
            Method m = obj.getClass().getMethod(methodName);
            Object val = m.invoke(obj);
            return val != null ? val.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String buildDescription(Auditable auditable, ProceedingJoinPoint pjp, String entityId) {
        if (!auditable.description().isBlank()) return auditable.description();
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String method = sig.getMethod().getName();
        if (entityId != null) return auditable.action() + " on " + auditable.module() + " [" + entityId + "] via " + method;
        return auditable.action() + " on " + auditable.module() + " via " + method;
    }

    private HttpServletRequest currentRequest() {
        ServletRequestAttributes attrs =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs != null ? attrs.getRequest() : null;
    }
}
