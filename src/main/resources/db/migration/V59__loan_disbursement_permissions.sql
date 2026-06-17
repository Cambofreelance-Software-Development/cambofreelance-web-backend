INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'loan-disbursements.create',         'Generate Disbursement',       'LOAN_MANAGEMENT', 230),
  (gen_random_uuid()::text, 'loan-disbursements.view',           'View Disbursements',           'LOAN_MANAGEMENT', 231),
  (gen_random_uuid()::text, 'loan-disbursements.upload-contract','Upload Signed Contract',       'LOAN_MANAGEMENT', 232),
  (gen_random_uuid()::text, 'loan-disbursements.disburse',       'Disburse Loan',                'LOAN_MANAGEMENT', 233)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('SUPER_ADMIN', 'TENANT_ADMIN')
  AND p.code IN (
    'loan-disbursements.create',
    'loan-disbursements.view',
    'loan-disbursements.upload-contract',
    'loan-disbursements.disburse'
  )
ON CONFLICT DO NOTHING;
