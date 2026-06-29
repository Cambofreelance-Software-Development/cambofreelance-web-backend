-- V63 granted dashboard.view + reports.view only to SUPER_ADMIN and TENANT_ADMIN,
-- so tenant accounts on the legacy ADMIN role (e.g. loan.admin) saw their Dashboard
-- sidebar item disappear once the frontend started gating it on the permission.
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code = 'ADMIN'
  AND p.code IN ('dashboard.view', 'reports.view')
ON CONFLICT DO NOTHING;
