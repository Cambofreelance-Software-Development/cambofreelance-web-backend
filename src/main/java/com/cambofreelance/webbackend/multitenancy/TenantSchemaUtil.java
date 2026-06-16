package com.cambofreelance.webbackend.multitenancy;

public final class TenantSchemaUtil {

    public static final String DEFAULT_SCHEMA = "public";

    /** Postgres schema name for a tenant id, e.g. "a1b2-c3d4" -> "tenant_a1b2_c3d4" */
    public static String schemaName(String tenantId) {
        return "tenant_" + tenantId.replace("-", "_").toLowerCase();
    }

    private TenantSchemaUtil() {}
}
