-- Ensure role codes are unique so the seed inserts below can use ON CONFLICT
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'roles_code_key'
    ) THEN
        ALTER TABLE public.roles ADD CONSTRAINT roles_code_key UNIQUE (code);
    END IF;
END $$;

-- Tenant self-service permissions (used by the /me/tenant/** endpoints)
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'my-tenant.users.manage',         'Manage My Tenant Users',         'TENANT_SELF_SERVICE', 101),
  (gen_random_uuid()::text, 'my-tenant.subscription.view',    'View My Tenant Subscription',    'TENANT_SELF_SERVICE', 102),
  (gen_random_uuid()::text, 'my-tenant.subscription.upgrade', 'Upgrade My Tenant Package',       'TENANT_SELF_SERVICE', 103),
  (gen_random_uuid()::text, 'my-tenant.usage.view',           'View My Tenant Usage Statistics', 'TENANT_SELF_SERVICE', 104)
ON CONFLICT (code) DO NOTHING;

-- System roles
INSERT INTO public.roles (id, code, name, convention, level, status, description, created_by, created_at)
VALUES
  (gen_random_uuid()::text, 'SUPER_ADMIN',  'Super Admin',  'lower_conversion', 0,  'ACT',
   'Full platform access: tenants, subscription packages, billing, and system configuration', 'SYSTEM', CURRENT_TIMESTAMP),
  (gen_random_uuid()::text, 'TENANT_ADMIN', 'Tenant Admin', 'lower_conversion', 10, 'ACT',
   'Manages a single tenant: its users, subscription, and usage statistics', 'SYSTEM', CURRENT_TIMESTAMP)
ON CONFLICT (code) DO NOTHING;

-- SUPER_ADMIN: every permission that exists today
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code = 'SUPER_ADMIN'
ON CONFLICT DO NOTHING;

-- TENANT_ADMIN: only the self-service permissions, scoped to their own tenant
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code = 'TENANT_ADMIN'
  AND p.code IN (
    'my-tenant.users.manage',
    'my-tenant.subscription.view',
    'my-tenant.subscription.upgrade',
    'my-tenant.usage.view'
  )
ON CONFLICT DO NOTHING;

-- Give the bootstrap super.admin account the new SUPER_ADMIN role too
INSERT INTO public.user_roles (user_id, role_id)
SELECT u.user_id, r.id
FROM public.users u
CROSS JOIN public.roles r
WHERE u.user_name = 'super.admin' AND r.code = 'SUPER_ADMIN'
ON CONFLICT DO NOTHING;
