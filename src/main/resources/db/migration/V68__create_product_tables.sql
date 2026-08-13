-- Product categories
CREATE TABLE IF NOT EXISTS public.category_product (
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
    CONSTRAINT uq_category_product_name UNIQUE (name)
);

CREATE INDEX IF NOT EXISTS idx_category_product_status     ON public.category_product (status);
CREATE INDEX IF NOT EXISTS idx_category_product_sort_order ON public.category_product (sort_order);

-- Products
CREATE TABLE IF NOT EXISTS public.product (
    id              VARCHAR(36)   PRIMARY KEY,
    name            VARCHAR(255)  NOT NULL,
    name_kh         VARCHAR(255),
    description     TEXT,
    description_kh  TEXT,
    price           VARCHAR(50),
    category_id     VARCHAR(36)   REFERENCES public.category_product(id),
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

CREATE INDEX IF NOT EXISTS idx_product_status      ON public.product (status);
CREATE INDEX IF NOT EXISTS idx_product_sort_order  ON public.product (sort_order);
CREATE INDEX IF NOT EXISTS idx_product_category_id ON public.product (category_id);

-- Permissions (category_product CRUD reuses these same product.* permissions)
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'product.view',   'View Products',   'CONTENT_MANAGEMENT', 104),
  (gen_random_uuid()::text, 'product.create', 'Create Products', 'CONTENT_MANAGEMENT', 105),
  (gen_random_uuid()::text, 'product.update', 'Update Products', 'CONTENT_MANAGEMENT', 106),
  (gen_random_uuid()::text, 'product.delete', 'Delete Products', 'CONTENT_MANAGEMENT', 107)
ON CONFLICT (code) DO NOTHING;

-- Auto-assign to ADMIN role
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('product.view', 'product.create', 'product.update', 'product.delete')
ON CONFLICT DO NOTHING;
