-- Permissions for platform admins to view/manage a specific tenant's custom roles
-- from /cms/tenants/{tenantId}/roles (distinct from my-tenant.roles.manage, which is
-- the tenant admin's own self-service permission scoped to their own tenant).
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'tenant-roles.view',   'View Tenant Roles',   'TENANT_MANAGEMENT', 89),
  (gen_random_uuid()::text, 'tenant-roles.manage', 'Manage Tenant Roles', 'TENANT_MANAGEMENT', 90)
ON CONFLICT (code) DO NOTHING;

-- Auto-assign to the ADMIN role, alongside the other tenant-users.* permissions.
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code = 'ADMIN'
  AND p.code IN ('tenant-roles.view', 'tenant-roles.manage')
ON CONFLICT DO NOTHING;
