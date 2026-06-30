-- Backfill: any user who registered a tenant via POST /tenants/register, whose tenant
-- has since been approved (status='ACT'), but who never received the TENANT_ADMIN role.
-- This happens when TenantServiceImpl.approve() ran roleRepository.findByCode("TENANT_ADMIN")
-- and the role didn't exist yet (silent .ifPresent() no-op at the time, then user got their
-- tenantId set but no role — leading to "Access denied" on every /me/tenant/** endpoint).
INSERT INTO public.user_roles (user_id, role_id)
SELECT u.user_id, r.id
FROM public.users u
JOIN public.tenants t ON t.id = u.tenant_id
CROSS JOIN public.roles r
WHERE r.code = 'TENANT_ADMIN'
  AND t.status = 'ACT'
  AND t.requested_by_user_id = u.user_id
  AND NOT EXISTS (
    SELECT 1 FROM public.user_roles ur
    WHERE ur.user_id = u.user_id AND ur.role_id = r.id
  )
ON CONFLICT DO NOTHING;
