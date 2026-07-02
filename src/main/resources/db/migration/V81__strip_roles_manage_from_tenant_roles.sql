-- V76 granted my-tenant.roles.manage to every role holding any other my-tenant.* permission.
-- That was meant only for system roles (ADMIN, TENANT_ADMIN) but its WHERE clause matched
-- tenant-scoped custom roles too (e.g. the seeded "Administrator" role has my-tenant.users.manage
-- etc.), since it didn't filter on roles.tenant_id. my-tenant.roles.manage is not in
-- TenantAssignablePermissions.ALL, so a tenant-scoped role should never hold it — remove it.
DELETE FROM public.role_permissions rp
USING public.roles r, public.permissions p
WHERE rp.role_id = r.id
  AND rp.permission_id = p.id
  AND r.tenant_id IS NOT NULL
  AND p.code = 'my-tenant.roles.manage';
