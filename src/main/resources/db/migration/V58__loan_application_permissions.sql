-- Loan Application permissions, enforced on /me/loan-applications/**.
-- These are tenant-facing capabilities: tenant admins assign the relevant subset to their own
-- custom roles (Loan Officer, Branch Manager, Auditor, ...) via the existing /me/tenant/roles API.

INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'loan-applications.create',  'Create Loan Application',          'LOAN_MANAGEMENT', 220),
  (gen_random_uuid()::text, 'loan-applications.view',    'View Loan Applications',            'LOAN_MANAGEMENT', 221),
  (gen_random_uuid()::text, 'loan-applications.update',  'Update Loan Application (Draft)',   'LOAN_MANAGEMENT', 222),
  (gen_random_uuid()::text, 'loan-applications.submit',  'Submit Loan Application',           'LOAN_MANAGEMENT', 223),
  (gen_random_uuid()::text, 'loan-applications.approve', 'Approve Loan Application',          'LOAN_MANAGEMENT', 224),
  (gen_random_uuid()::text, 'loan-applications.reject',  'Reject Loan Application',           'LOAN_MANAGEMENT', 225)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('SUPER_ADMIN', 'TENANT_ADMIN')
  AND p.code IN (
    'loan-applications.create',
    'loan-applications.view',
    'loan-applications.update',
    'loan-applications.submit',
    'loan-applications.approve',
    'loan-applications.reject'
  )
ON CONFLICT DO NOTHING;
