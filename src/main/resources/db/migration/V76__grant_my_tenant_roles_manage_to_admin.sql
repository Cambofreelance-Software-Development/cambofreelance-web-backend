-- Grant my-tenant.roles.manage to every role that already has any other my-tenant.* permission.
-- V53 only granted this permission to TENANT_ADMIN, so legacy ADMIN-role tenant accounts
-- (e.g. loan.admin) hit "Access denied" on GET /me/tenant/roles → /admin/my-tenant/roles.
-- Targeting "role already has any my-tenant.* permission" catches ADMIN, TENANT_ADMIN, and
-- any custom role provisioned with tenant self-service permissions, without us having to
-- enumerate role codes per environment.
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT DISTINCT rp.role_id, target.id
FROM public.role_permissions rp
JOIN public.permissions p ON p.id = rp.permission_id
JOIN public.permissions target ON target.code = 'my-tenant.roles.manage'
WHERE p.code LIKE 'my-tenant.%'
ON CONFLICT DO NOTHING;

-- Also grant it to the ADMIN role unconditionally, in case loan.admin's role has no
-- my-tenant.* permissions yet (some envs seed ADMIN with only legacy permissions).
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code = 'ADMIN'
  AND p.code = 'my-tenant.roles.manage'
ON CONFLICT DO NOTHING;
