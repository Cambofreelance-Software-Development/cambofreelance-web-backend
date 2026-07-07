CREATE TABLE IF NOT EXISTS public.features (
    id              VARCHAR(36)   PRIMARY KEY,
    title           VARCHAR(255)  NOT NULL,
    title_kh        VARCHAR(255),
    description     TEXT,
    description_kh  TEXT,
    category        VARCHAR(100),
    category_kh     VARCHAR(100),
    icon            VARCHAR(100),
    image_id        VARCHAR(36)   REFERENCES public.media_files(id),
    link            VARCHAR(500),
    sort_order      INTEGER       NOT NULL DEFAULT 0,
    created_by      VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      VARCHAR(255),
    updated_at      TIMESTAMP,
    status          VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_features_status     ON public.features (status);
CREATE INDEX IF NOT EXISTS idx_features_sort_order ON public.features (sort_order);
CREATE INDEX IF NOT EXISTS idx_features_category   ON public.features (category);

-- Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'features.view',   'View Features',   'CONTENT_MANAGEMENT', 43),
  (gen_random_uuid()::text, 'features.create', 'Create Features', 'CONTENT_MANAGEMENT', 44),
  (gen_random_uuid()::text, 'features.update', 'Update Features', 'CONTENT_MANAGEMENT', 45),
  (gen_random_uuid()::text, 'features.delete', 'Delete Features', 'CONTENT_MANAGEMENT', 46)
ON CONFLICT (code) DO NOTHING;

-- Auto-assign to ADMIN role
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('features.view', 'features.create', 'features.update', 'features.delete')
ON CONFLICT DO NOTHING;
