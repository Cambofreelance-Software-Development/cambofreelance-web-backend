INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'payments.create',  'Receive Payment',  'LOAN_MANAGEMENT', 240),
  (gen_random_uuid()::text, 'payments.view',    'View Payments',    'LOAN_MANAGEMENT', 241),
  (gen_random_uuid()::text, 'payments.reverse', 'Reverse Payment',  'LOAN_MANAGEMENT', 242)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('SUPER_ADMIN', 'TENANT_ADMIN')
  AND p.code IN ('payments.create', 'payments.view', 'payments.reverse')
ON CONFLICT DO NOTHING;
