-- Normalize feature categories into their own table
CREATE TABLE IF NOT EXISTS public.category_feature (
    id          VARCHAR(36)   PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL,
    name_kh     VARCHAR(100),
    sort_order  INTEGER       NOT NULL DEFAULT 0,
    created_by  VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(255),
    updated_at  TIMESTAMP,
    status      VARCHAR(3)    NOT NULL DEFAULT 'ACT',
    CONSTRAINT uq_category_feature_name UNIQUE (name)
);

CREATE INDEX IF NOT EXISTS idx_category_feature_status     ON public.category_feature (status);
CREATE INDEX IF NOT EXISTS idx_category_feature_sort_order ON public.category_feature (sort_order);

-- Seed categories (mirror the ones used by the seeded features in V22)
INSERT INTO public.category_feature (id, name, name_kh, sort_order) VALUES
    (gen_random_uuid()::text, 'Sell',   'លក់',       1),
    (gen_random_uuid()::text, 'Manage', 'គ្រប់គ្រង', 2),
    (gen_random_uuid()::text, 'Grow',   'រីកចម្រើន', 3)
ON CONFLICT (name) DO NOTHING;

-- Link features to the new category table
ALTER TABLE public.features ADD COLUMN IF NOT EXISTS category_id VARCHAR(36)
    REFERENCES public.category_feature(id);

CREATE INDEX IF NOT EXISTS idx_features_category_id ON public.features (category_id);

-- Backfill existing rows by matching the old free-text category to the new names
UPDATE public.features f
SET category_id = c.id
FROM public.category_feature c
WHERE f.category IS NOT NULL
  AND LOWER(TRIM(f.category)) = LOWER(c.name);

-- Drop the now-redundant free-text columns
DROP INDEX IF EXISTS idx_features_category;
ALTER TABLE public.features DROP COLUMN IF EXISTS category;
ALTER TABLE public.features DROP COLUMN IF EXISTS category_kh;
