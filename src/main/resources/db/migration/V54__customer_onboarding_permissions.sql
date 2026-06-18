-- Customer Onboarding (borrower registration/KYC) permissions, enforced on /me/customers/**.
-- These are tenant-facing capabilities: tenant admins assign the relevant subset to their own
-- custom roles (Loan Officer, Branch Manager, Auditor, ...) via the existing /me/tenant/roles API.

INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'customers.create',           'Create Customer (Onboarding)',     'CUSTOMER_ONBOARDING', 110),
  (gen_random_uuid()::text, 'customers.view',              'View Customers',                   'CUSTOMER_ONBOARDING', 111),
  (gen_random_uuid()::text, 'customers.update',            'Update Customer Draft',             'CUSTOMER_ONBOARDING', 112),
  (gen_random_uuid()::text, 'customers.submit',            'Submit Customer for Verification', 'CUSTOMER_ONBOARDING', 113),
  (gen_random_uuid()::text, 'customers.verify',            'Verify Customer KYC',               'CUSTOMER_ONBOARDING', 114),
  (gen_random_uuid()::text, 'customers.approve',           'Approve Customer (Activate)',       'CUSTOMER_ONBOARDING', 115),
  (gen_random_uuid()::text, 'customers.reject',            'Reject Customer',                   'CUSTOMER_ONBOARDING', 116),
  (gen_random_uuid()::text, 'customers.activate',          'Activate/Deactivate Customer',      'CUSTOMER_ONBOARDING', 117),
  (gen_random_uuid()::text, 'customer-documents.upload',   'Upload Customer KYC Document',      'CUSTOMER_ONBOARDING', 118),
  (gen_random_uuid()::text, 'customer-documents.view',     'View Customer KYC Documents',        'CUSTOMER_ONBOARDING', 119),
  (gen_random_uuid()::text, 'customer-documents.delete',   'Delete Customer KYC Document',       'CUSTOMER_ONBOARDING', 120)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('SUPER_ADMIN', 'TENANT_ADMIN')
  AND p.code IN (
    'customers.create', 'customers.view', 'customers.update', 'customers.submit',
    'customers.verify', 'customers.approve', 'customers.reject', 'customers.activate',
    'customer-documents.upload', 'customer-documents.view', 'customer-documents.delete'
  )
ON CONFLICT DO NOTHING;
