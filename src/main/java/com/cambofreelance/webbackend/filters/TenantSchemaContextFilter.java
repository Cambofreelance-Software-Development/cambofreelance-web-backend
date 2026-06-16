package com.cambofreelance.webbackend.filters;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.multitenancy.TenantSchemaContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Runs after AuthTokenFilter so it can read the Tenant-Id header that filter sets on the
 * mutated request. Populates TenantSchemaContext for the duration of the request so
 * Hibernate routes tenant-scoped entities to that tenant's schema; clears it afterwards
 * so the thread (and any pooled connection) never leaks a tenant's search_path.
 */
@Component
public class TenantSchemaContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {

        String tenantId = request.getHeader(Constants.TENANT_ID);
        try {
            if (StringUtils.hasText(tenantId)) {
                TenantSchemaContext.set(tenantId);
            }
            filterChain.doFilter(request, response);
        } finally {
            TenantSchemaContext.clear();
        }
    }
}
