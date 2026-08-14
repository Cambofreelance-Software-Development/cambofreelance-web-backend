-- Business-Type Catalog categories (separate from the existing cms_business_type_groups/tags
-- taxonomy used by the homepage section — this is a standalone Product-style catalog).
CREATE TABLE IF NOT EXISTS public.category_business_type_catalog (
    id          VARCHAR(36)   PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL,
    name_kh     VARCHAR(100),
    description     TEXT,
    description_kh  TEXT,
    image_id    VARCHAR(36)   REFERENCES public.media_files(id),
    icon        VARCHAR(100),
    more_link   VARCHAR(500),
    sort_order  INTEGER       NOT NULL DEFAULT 0,
    created_by  VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(255),
    updated_at  TIMESTAMP,
    status      VARCHAR(3)    NOT NULL DEFAULT 'ACT',
    CONSTRAINT uq_category_business_type_catalog_name UNIQUE (name)
);

CREATE INDEX IF NOT EXISTS idx_category_business_type_catalog_status     ON public.category_business_type_catalog (status);
CREATE INDEX IF NOT EXISTS idx_category_business_type_catalog_sort_order ON public.category_business_type_catalog (sort_order);

-- Business-Type Catalog items
CREATE TABLE IF NOT EXISTS public.business_type_catalog (
    id              VARCHAR(36)   PRIMARY KEY,
    name            VARCHAR(255)  NOT NULL,
    name_kh         VARCHAR(255),
    description     TEXT,
    description_kh  TEXT,
    price           VARCHAR(50),
    category_id     VARCHAR(36)   REFERENCES public.category_business_type_catalog(id),
    image_id        VARCHAR(36)   REFERENCES public.media_files(id),
    icon            VARCHAR(100),
    link            VARCHAR(500),
    sort_order      INTEGER       NOT NULL DEFAULT 0,
    created_by      VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      VARCHAR(255),
    updated_at      TIMESTAMP,
    status          VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_business_type_catalog_status      ON public.business_type_catalog (status);
CREATE INDEX IF NOT EXISTS idx_business_type_catalog_sort_order  ON public.business_type_catalog (sort_order);
CREATE INDEX IF NOT EXISTS idx_business_type_catalog_category_id ON public.business_type_catalog (category_id);

-- Permissions (category CRUD reuses these same business_type_catalog.* permissions)
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'business_type_catalog.view',   'View Business-Type Catalog',   'CONTENT_MANAGEMENT', 108),
  (gen_random_uuid()::text, 'business_type_catalog.create', 'Create Business-Type Catalog', 'CONTENT_MANAGEMENT', 109),
  (gen_random_uuid()::text, 'business_type_catalog.update', 'Update Business-Type Catalog', 'CONTENT_MANAGEMENT', 110),
  (gen_random_uuid()::text, 'business_type_catalog.delete', 'Delete Business-Type Catalog', 'CONTENT_MANAGEMENT', 111)
ON CONFLICT (code) DO NOTHING;

-- Auto-assign to ADMIN role
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('business_type_catalog.view', 'business_type_catalog.create', 'business_type_catalog.update', 'business_type_catalog.delete')
ON CONFLICT DO NOTHING;
