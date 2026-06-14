-- Response-code management permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'response-codes.view',   'View Response Codes',   'RESPONSE_CODE_MANAGEMENT', 70),
  (gen_random_uuid()::text, 'response-codes.create', 'Create Response Codes', 'RESPONSE_CODE_MANAGEMENT', 71),
  (gen_random_uuid()::text, 'response-codes.update', 'Update Response Codes', 'RESPONSE_CODE_MANAGEMENT', 72),
  (gen_random_uuid()::text, 'response-codes.delete', 'Delete Response Codes', 'RESPONSE_CODE_MANAGEMENT', 73),
  (gen_random_uuid()::text, 'response-codes.manage', 'Manage Response Code Cache', 'RESPONSE_CODE_MANAGEMENT', 74)
ON CONFLICT (code) DO NOTHING;

-- Auto-assign all response-code permissions to the ADMIN role
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code = 'ADMIN'
  AND p.code IN (
    'response-codes.view',
    'response-codes.create',
    'response-codes.update',
    'response-codes.delete',
    'response-codes.manage'
  )
ON CONFLICT DO NOTHING;
