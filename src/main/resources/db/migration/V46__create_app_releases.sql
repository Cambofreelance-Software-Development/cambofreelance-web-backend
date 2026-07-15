-- App releases (SOPPOS POS app versions per platform for the Release App Management screen)
CREATE TABLE IF NOT EXISTS public.app_releases (
    id                VARCHAR(36)   PRIMARY KEY,
    app_name          VARCHAR(150)  NOT NULL,
    platform          VARCHAR(30)   NOT NULL,
    version_name      VARCHAR(50)   NOT NULL,
    version_code      INTEGER,
    download_url      VARCHAR(500),
    file_size         VARCHAR(50),
    min_os_version    VARCHAR(50),
    release_notes     TEXT,
    release_notes_kh  TEXT,
    force_update      BOOLEAN       NOT NULL DEFAULT FALSE,
    release_date      DATE,
    created_by        VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by        VARCHAR(255),
    updated_at        TIMESTAMP,
    status            VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_app_releases_status       ON public.app_releases (status);
CREATE INDEX IF NOT EXISTS idx_app_releases_platform     ON public.app_releases (platform);
CREATE INDEX IF NOT EXISTS idx_app_releases_release_date ON public.app_releases (release_date);

-- Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'app_releases.view',   'View App Releases',   'CONTENT_MANAGEMENT', 100),
  (gen_random_uuid()::text, 'app_releases.create', 'Create App Releases', 'CONTENT_MANAGEMENT', 101),
  (gen_random_uuid()::text, 'app_releases.update', 'Update App Releases', 'CONTENT_MANAGEMENT', 102),
  (gen_random_uuid()::text, 'app_releases.delete', 'Delete App Releases', 'CONTENT_MANAGEMENT', 103)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('app_releases.view', 'app_releases.create', 'app_releases.update', 'app_releases.delete')
ON CONFLICT DO NOTHING;