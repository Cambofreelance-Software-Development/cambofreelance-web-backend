-- Media Library permissions, enforced on /me/media-library/**.
-- Tenant-facing: allows tenant roles to manage their own uploaded file collections.

INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'media-library.view',   'View Media Library',             'MEDIA_LIBRARY', 210),
  (gen_random_uuid()::text, 'media-library.upload',  'Upload to Media Library',        'MEDIA_LIBRARY', 211),
  (gen_random_uuid()::text, 'media-library.update',  'Update Media Library Metadata',  'MEDIA_LIBRARY', 212),
  (gen_random_uuid()::text, 'media-library.delete',  'Delete from Media Library',      'MEDIA_LIBRARY', 213)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('SUPER_ADMIN', 'TENANT_ADMIN')
  AND p.code IN (
    'media-library.view', 'media-library.upload',
    'media-library.update', 'media-library.delete'
  )
ON CONFLICT DO NOTHING;
