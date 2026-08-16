-- Client accounts (self-registered users, role PUBLIC_USER) need to upload a company
-- logo via the shared media library (POST /media/presign, /media/confirm), but that
-- role currently carries no permissions at all. Grant only media.upload — not
-- media.view/media.delete — so clients can upload a file and get back a URL without
-- being able to browse or delete the shared media library.
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code = 'PUBLIC_USER'
  AND p.code = 'media.upload'
ON CONFLICT DO NOTHING;
