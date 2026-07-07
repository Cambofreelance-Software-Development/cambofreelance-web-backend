-- Hardware categories (POS printers, Barcode scanners, Cash drawers, …)
CREATE TABLE IF NOT EXISTS public.category_hardware (
    id          VARCHAR(36)   PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL,
    name_kh     VARCHAR(100),
    sort_order  INTEGER       NOT NULL DEFAULT 0,
    created_by  VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(255),
    updated_at  TIMESTAMP,
    status      VARCHAR(3)    NOT NULL DEFAULT 'ACT',
    CONSTRAINT uq_category_hardware_name UNIQUE (name)
);

CREATE INDEX IF NOT EXISTS idx_category_hardware_status     ON public.category_hardware (status);
CREATE INDEX IF NOT EXISTS idx_category_hardware_sort_order ON public.category_hardware (sort_order);

-- Hardware products
CREATE TABLE IF NOT EXISTS public.hardware (
    id              VARCHAR(36)   PRIMARY KEY,
    name            VARCHAR(255)  NOT NULL,
    name_kh         VARCHAR(255),
    brand           VARCHAR(150),
    description     TEXT,
    description_kh  TEXT,
    connectivity    VARCHAR(255),
    category_id     VARCHAR(36)   REFERENCES public.category_hardware(id),
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

CREATE INDEX IF NOT EXISTS idx_hardware_status      ON public.hardware (status);
CREATE INDEX IF NOT EXISTS idx_hardware_sort_order  ON public.hardware (sort_order);
CREATE INDEX IF NOT EXISTS idx_hardware_category_id ON public.hardware (category_id);

-- Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'hardware.view',   'View Hardware',   'CONTENT_MANAGEMENT', 47),
  (gen_random_uuid()::text, 'hardware.create', 'Create Hardware', 'CONTENT_MANAGEMENT', 48),
  (gen_random_uuid()::text, 'hardware.update', 'Update Hardware', 'CONTENT_MANAGEMENT', 49),
  (gen_random_uuid()::text, 'hardware.delete', 'Delete Hardware', 'CONTENT_MANAGEMENT', 50)
ON CONFLICT (code) DO NOTHING;

-- Auto-assign to ADMIN role
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('hardware.view', 'hardware.create', 'hardware.update', 'hardware.delete')
ON CONFLICT DO NOTHING;
