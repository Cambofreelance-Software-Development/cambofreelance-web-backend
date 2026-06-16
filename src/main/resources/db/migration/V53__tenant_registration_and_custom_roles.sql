-- Self-service tenant registration: who requested it, and why it was rejected (if it was)
ALTER TABLE public.tenants ADD COLUMN IF NOT EXISTS requested_by_user_id VARCHAR(255) REFERENCES public.users(user_id);
ALTER TABLE public.tenants ADD COLUMN IF NOT EXISTS rejection_reason VARCHAR(1000);

-- A PEN (pending approval) subscription has no real start/end date yet
ALTER TABLE public.subscriptions ALTER COLUMN start_date DROP NOT NULL;
ALTER TABLE public.subscriptions ALTER COLUMN end_date DROP NOT NULL;

-- Tenant-scoped custom roles: NULL tenant_id means a global/system role (ADMIN, SUPER_ADMIN, ...)
ALTER TABLE public.roles ADD COLUMN IF NOT EXISTS tenant_id VARCHAR(255) REFERENCES public.tenants(id);
CREATE INDEX IF NOT EXISTS idx_roles_tenant_id ON public.roles (tenant_id);

INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'tenants.approve',     'Approve/Reject Tenant Registrations', 'TENANT_MANAGEMENT',      89),
  (gen_random_uuid()::text, 'my-tenant.roles.manage', 'Manage My Tenant Custom Roles',     'TENANT_SELF_SERVICE',   105)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code = 'tenants.approve'
ON CONFLICT DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code = 'TENANT_ADMIN'
  AND p.code = 'my-tenant.roles.manage'
ON CONFLICT DO NOTHING;
