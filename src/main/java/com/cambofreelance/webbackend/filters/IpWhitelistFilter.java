package com.cambofreelance.webbackend.filters;

import com.cambofreelance.webbackend.caches.IpWhitelistCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpWhitelistFilter extends OncePerRequestFilter {

    private final IpWhitelistCache ipWhitelistCache;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {

        String uri = request.getRequestURI();

        // Only guard CMS paths
        if (!uri.startsWith("/cms/") || !ipWhitelistCache.isEnabled()) {
            chain.doFilter(request, response);
            return;
        }

        List<String> rules = ipWhitelistCache.getRules();
        // Enabled but no rules → still allow (prevents full lockout)
        if (rules.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        String clientIp = extractClientIp(request);
        if (isAllowed(clientIp, rules)) {
            chain.doFilter(request, response);
            return;
        }

        log.warn("IP {} blocked by whitelist for {}", clientIp, uri);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Map.of(
            "success", false,
            "code",    "IP_NOT_ALLOWED",
            "message", "Access denied: IP " + clientIp + " is not whitelisted"
        )));
    }

    private boolean isAllowed(String ip, List<String> rules) {
        // Always allow loopback
        if ("127.0.0.1".equals(ip) || "::1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            return true;
        }
        return rules.stream().anyMatch(rule -> matchesRule(ip, rule));
    }

    private boolean matchesRule(String ip, String rule) {
        try {
            if (rule.contains("/")) return matchesCidr(ip, rule);
            return InetAddress.getByName(ip).equals(InetAddress.getByName(rule));
        } catch (Exception e) {
            return false;
        }
    }

    private boolean matchesCidr(String ip, String cidr) {
        try {
            String[] parts = cidr.split("/");
            byte[] network = InetAddress.getByName(parts[0]).getAddress();
            byte[] client  = InetAddress.getByName(ip).getAddress();
            if (network.length != client.length) return false;
            int prefixLen = Integer.parseInt(parts[1]);
            int fullBytes = prefixLen / 8;
            int remainder = prefixLen % 8;
            for (int i = 0; i < fullBytes; i++) {
                if (network[i] != client[i]) return false;
            }
            if (remainder > 0 && fullBytes < network.length) {
                int mask = (0xFF << (8 - remainder)) & 0xFF;
                return (network[fullBytes] & mask) == (client[fullBytes] & mask);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) return forwarded.split(",")[0].trim();
        String addr = request.getRemoteAddr();
        return addr != null ? addr : "unknown";
    }
}
