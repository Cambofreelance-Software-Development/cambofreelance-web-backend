-- Tenants should NOT access the global /media endpoints (GET /media, DELETE /media/{id})
-- which return data from all tenants without isolation.
-- Tenants use /me/media-library (media-library.view / media-library.delete from V55) for
-- their own scoped library, and only need media.upload + media.confirm for the upload flow.

DELETE FROM public.role_permissions
WHERE role_id = (SELECT id FROM public.roles WHERE code = 'TENANT_ADMIN')
  AND permission_id IN (
    SELECT id FROM public.permissions WHERE code IN ('media.view', 'media.delete')
  );
