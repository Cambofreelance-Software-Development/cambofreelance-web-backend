INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'repayment-schedule.generate', 'Generate / Recalculate Schedule', 'LOAN_MANAGEMENT', 240),
  (gen_random_uuid()::text, 'repayment-schedule.update',   'Update Installment / Mark Overdue', 'LOAN_MANAGEMENT', 241)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('SUPER_ADMIN', 'TENANT_ADMIN')
  AND p.code IN ('repayment-schedule.generate', 'repayment-schedule.update')
ON CONFLICT DO NOTHING;
