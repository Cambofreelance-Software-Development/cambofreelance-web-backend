-- Pricing plans (the cards)
CREATE TABLE IF NOT EXISTS public.pricing_plan (
    id                VARCHAR(36)    PRIMARY KEY,
    name              VARCHAR(150)   NOT NULL,
    name_kh           VARCHAR(150),
    description       VARCHAR(500),
    description_kh    VARCHAR(500),
    price_monthly     NUMERIC(10,2)  NOT NULL DEFAULT 0,
    price_yearly      NUMERIC(10,2),
    currency          VARCHAR(8)     NOT NULL DEFAULT '$',
    includes_note     VARCHAR(255),
    includes_note_kh  VARCHAR(255),
    badge             VARCHAR(100),
    badge_kh          VARCHAR(100),
    highlighted       BOOLEAN        NOT NULL DEFAULT FALSE,
    cta_label         VARCHAR(150),
    cta_label_kh      VARCHAR(150),
    cta_link          VARCHAR(500),
    sort_order        INTEGER        NOT NULL DEFAULT 0,
    created_by        VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by        VARCHAR(255),
    updated_at        TIMESTAMP,
    status            VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);
CREATE INDEX IF NOT EXISTS idx_pricing_plan_status     ON public.pricing_plan (status);
CREATE INDEX IF NOT EXISTS idx_pricing_plan_sort_order ON public.pricing_plan (sort_order);

-- Bullet features shown on each plan card
CREATE TABLE IF NOT EXISTS public.pricing_plan_feature (
    id          VARCHAR(36)   PRIMARY KEY,
    plan_id     VARCHAR(36)   NOT NULL REFERENCES public.pricing_plan(id) ON DELETE CASCADE,
    label       VARCHAR(255)  NOT NULL,
    label_kh    VARCHAR(255),
    sort_order  INTEGER       NOT NULL DEFAULT 0,
    created_by  VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(255),
    updated_at  TIMESTAMP,
    status      VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);
CREATE INDEX IF NOT EXISTS idx_pricing_plan_feature_plan ON public.pricing_plan_feature (plan_id);

-- Comparison-table feature rows
CREATE TABLE IF NOT EXISTS public.pricing_feature (
    id          VARCHAR(36)   PRIMARY KEY,
    name        VARCHAR(255)  NOT NULL,
    name_kh     VARCHAR(255),
    sort_order  INTEGER       NOT NULL DEFAULT 0,
    created_by  VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(255),
    updated_at  TIMESTAMP,
    status      VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);
CREATE INDEX IF NOT EXISTS idx_pricing_feature_status     ON public.pricing_feature (status);
CREATE INDEX IF NOT EXISTS idx_pricing_feature_sort_order ON public.pricing_feature (sort_order);

-- Comparison-matrix cell values (plan x feature)
CREATE TABLE IF NOT EXISTS public.pricing_plan_value (
    id          VARCHAR(36)  PRIMARY KEY,
    plan_id     VARCHAR(36)  NOT NULL REFERENCES public.pricing_plan(id)    ON DELETE CASCADE,
    feature_id  VARCHAR(36)  NOT NULL REFERENCES public.pricing_feature(id) ON DELETE CASCADE,
    value       VARCHAR(255),
    CONSTRAINT uq_pricing_plan_value UNIQUE (plan_id, feature_id)
);
CREATE INDEX IF NOT EXISTS idx_pricing_plan_value_plan    ON public.pricing_plan_value (plan_id);
CREATE INDEX IF NOT EXISTS idx_pricing_plan_value_feature ON public.pricing_plan_value (feature_id);

-- Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'pricing.view',   'View Pricing',   'CONTENT_MANAGEMENT', 51),
  (gen_random_uuid()::text, 'pricing.create', 'Create Pricing', 'CONTENT_MANAGEMENT', 52),
  (gen_random_uuid()::text, 'pricing.update', 'Update Pricing', 'CONTENT_MANAGEMENT', 53),
  (gen_random_uuid()::text, 'pricing.delete', 'Delete Pricing', 'CONTENT_MANAGEMENT', 54)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('pricing.view', 'pricing.create', 'pricing.update', 'pricing.delete')
ON CONFLICT DO NOTHING;
