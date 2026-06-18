INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'dashboard.view', 'View Dashboard', 'SYSTEM', 10),
  (gen_random_uuid()::text, 'reports.view',   'View Reports',   'SYSTEM', 11)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM public.roles r CROSS JOIN public.permissions p
WHERE r.code IN ('SUPER_ADMIN', 'TENANT_ADMIN')
  AND p.code IN ('dashboard.view', 'reports.view')
ON CONFLICT DO NOTHING;
