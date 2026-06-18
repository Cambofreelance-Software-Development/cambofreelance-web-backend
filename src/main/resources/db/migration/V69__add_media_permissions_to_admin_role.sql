-- Assign all media permissions to the ADMIN role
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code = 'ADMIN'
  AND p.code IN (
    'media.view',
    'media.upload',
    'media.delete',
    'media.confirm'
  )
ON CONFLICT DO NOTHING;
