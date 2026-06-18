package com.cambofreelance.webbackend.constants;

import java.util.Set;

/**
 * The only permission codes a Tenant Admin may grant to a custom role within their own
 * tenant. Deliberately the same set TENANT_ADMIN itself holds (see migrations V52/V53) —
 * a Tenant Admin can never grant a permission they don't themselves have.
 */
public final class TenantAssignablePermissions {

    public static final Set<String> ALL = Set.of(
        "my-tenant.users.manage",
        "my-tenant.subscription.view",
        "my-tenant.subscription.upgrade",
        "my-tenant.usage.view"
    );

    private TenantAssignablePermissions() {}
}
