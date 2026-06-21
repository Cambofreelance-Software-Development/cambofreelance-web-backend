CREATE TABLE IF NOT EXISTS public.authors (
    id          VARCHAR(36)   PRIMARY KEY,
    name        VARCHAR(255)  NOT NULL,
    name_kh     VARCHAR(255),
    bio         TEXT,
    email       VARCHAR(255),
    avatar_id   VARCHAR(36)   REFERENCES public.media_files(id),
    sort_order  INTEGER       NOT NULL DEFAULT 0,
    created_by  VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(255),
    updated_at  TIMESTAMP,
    status      VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_authors_status     ON public.authors (status);
CREATE INDEX IF NOT EXISTS idx_authors_sort_order ON public.authors (sort_order);

-- Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'authors.view',   'View Authors',   'ARTICLE_MANAGEMENT', 39),
  (gen_random_uuid()::text, 'authors.create', 'Create Authors', 'ARTICLE_MANAGEMENT', 40),
  (gen_random_uuid()::text, 'authors.update', 'Update Authors', 'ARTICLE_MANAGEMENT', 41),
  (gen_random_uuid()::text, 'authors.delete', 'Delete Authors', 'ARTICLE_MANAGEMENT', 42)
ON CONFLICT (code) DO NOTHING;

-- Auto-assign to ADMIN role
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('authors.view', 'authors.create', 'authors.update', 'authors.delete')
ON CONFLICT DO NOTHING;
