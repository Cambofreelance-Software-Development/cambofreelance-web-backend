-- Discriminates which product an app release belongs to (SOPPOS_POS, ID_ALL, ...)
ALTER TABLE app_releases
    ADD COLUMN IF NOT EXISTS product_key VARCHAR(30) NOT NULL DEFAULT 'SOPPOS_POS';

CREATE INDEX IF NOT EXISTS idx_app_releases_product_key ON app_releases (product_key);
