package com.cambofreelance.webbackend.multitenancy;

/** Request-scoped holder for the current tenant id, read by {@link SchemaTenantIdentifierResolver}. */
public final class TenantSchemaContext {

    private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

    public static void set(String tenantId) {
        CURRENT.set(tenantId);
    }

    public static String get() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }

    private TenantSchemaContext() {}
}
