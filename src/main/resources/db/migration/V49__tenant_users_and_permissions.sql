-- Link users to a tenant for data isolation
ALTER TABLE public.users ADD COLUMN IF NOT EXISTS tenant_id VARCHAR(255) REFERENCES public.tenants(id);
CREATE INDEX IF NOT EXISTS idx_users_tenant_id ON public.users (tenant_id);

-- Tenant management + tenant-user assignment permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'tenants.view',        'View Tenants',            'TENANT_MANAGEMENT', 80),
  (gen_random_uuid()::text, 'tenants.create',      'Create Tenants',          'TENANT_MANAGEMENT', 81),
  (gen_random_uuid()::text, 'tenants.update',      'Update Tenants',          'TENANT_MANAGEMENT', 82),
  (gen_random_uuid()::text, 'tenants.delete',      'Delete Tenants',          'TENANT_MANAGEMENT', 83),
  (gen_random_uuid()::text, 'tenants.status',      'Change Tenant Status',    'TENANT_MANAGEMENT', 84),
  (gen_random_uuid()::text, 'tenants.dashboard',   'View Tenant Dashboard',   'TENANT_MANAGEMENT', 85),
  (gen_random_uuid()::text, 'tenant-users.view',   'View Tenant Users',       'TENANT_MANAGEMENT', 86),
  (gen_random_uuid()::text, 'tenant-users.assign', 'Assign User to Tenant',   'TENANT_MANAGEMENT', 87),
  (gen_random_uuid()::text, 'tenant-users.remove', 'Remove User from Tenant', 'TENANT_MANAGEMENT', 88)
ON CONFLICT (code) DO NOTHING;

-- Auto-assign all tenant permissions to the ADMIN role
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code = 'ADMIN'
  AND p.code IN (
    'tenants.view', 'tenants.create', 'tenants.update', 'tenants.delete', 'tenants.status', 'tenants.dashboard',
    'tenant-users.view', 'tenant-users.assign', 'tenant-users.remove'
  )
ON CONFLICT DO NOTHING;
