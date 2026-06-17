INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'collections.view',   'View Collections',   'LOAN_MANAGEMENT', 250),
  (gen_random_uuid()::text, 'collections.manage', 'Manage Collections', 'LOAN_MANAGEMENT', 251)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM public.roles r CROSS JOIN public.permissions p
WHERE r.code IN ('SUPER_ADMIN', 'TENANT_ADMIN')
  AND p.code IN ('collections.view', 'collections.manage')
ON CONFLICT DO NOTHING;
