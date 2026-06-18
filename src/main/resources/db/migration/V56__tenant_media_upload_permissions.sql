-- Grant TENANT_ADMIN the core media permissions needed for the file upload flow.
-- The upload flow is: POST /media/presign → upload to storage → POST /media/confirm/{id}
-- → POST /me/media-library.  Steps 1 and 3 require media.upload; the tenant-library step
-- requires media-library.upload (already granted in V55).  media.view / media.delete
-- are included so tenants can also list and remove their own uploaded files.

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code = 'TENANT_ADMIN'
  AND p.code IN (
    'media.upload',
    'media.confirm',
    'media.view',
    'media.delete'
  )
ON CONFLICT DO NOTHING;
