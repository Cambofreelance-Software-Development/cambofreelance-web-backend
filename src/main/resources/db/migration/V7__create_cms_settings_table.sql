CREATE TABLE IF NOT EXISTS public.cms_settings (
    setting_id   VARCHAR(255) PRIMARY KEY,
    setting_key  VARCHAR(100) NOT NULL,
    setting_value TEXT,
    setting_group VARCHAR(50) NOT NULL,
    created_by   VARCHAR(255) NOT NULL DEFAULT 'SYS',
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by   VARCHAR(255),
    updated_at   TIMESTAMP,
    status       VARCHAR(3)   NOT NULL DEFAULT 'ACT',
    CONSTRAINT uq_cms_settings_key UNIQUE (setting_key)
);

CREATE INDEX idx_cms_settings_group ON public.cms_settings (setting_group);

-- Seed default general settings
INSERT INTO public.cms_settings (setting_id, setting_key, setting_value, setting_group) VALUES
    (gen_random_uuid()::text, 'site_name',        'CamboFreelance CMS', 'GENERAL'),
    (gen_random_uuid()::text, 'environment',      'DEV',                'GENERAL'),
    (gen_random_uuid()::text, 'default_language', 'en',                 'GENERAL'),
    (gen_random_uuid()::text, 'time_zone',        'Asia/Phnom_Penh',    'GENERAL');

-- Seed default SEO settings
INSERT INTO public.cms_settings (setting_id, setting_key, setting_value, setting_group) VALUES
    (gen_random_uuid()::text, 'seo_title',             'CamboFreelance', 'SEO'),
    (gen_random_uuid()::text, 'seo_description',       '',               'SEO'),
    (gen_random_uuid()::text, 'seo_keywords',          '',               'SEO'),
    (gen_random_uuid()::text, 'seo_canonical_url',     '',               'SEO'),
    (gen_random_uuid()::text, 'seo_robots',            'index,follow',   'SEO'),
    (gen_random_uuid()::text, 'seo_og_title',          '',               'SEO'),
    (gen_random_uuid()::text, 'seo_og_description',    '',               'SEO'),
    (gen_random_uuid()::text, 'seo_og_image',          '',               'SEO');

-- Seed default CDN settings
INSERT INTO public.cms_settings (setting_id, setting_key, setting_value, setting_group) VALUES
    (gen_random_uuid()::text, 'cdn_enabled',       'false', 'CDN'),
    (gen_random_uuid()::text, 'cdn_provider',      '',      'CDN'),
    (gen_random_uuid()::text, 'cdn_base_url',      '',      'CDN'),
    (gen_random_uuid()::text, 'cdn_custom_domain', '',      'CDN');

-- Seed default storage settings
INSERT INTO public.cms_settings (setting_id, setting_key, setting_value, setting_group) VALUES
    (gen_random_uuid()::text, 'storage_provider',   'MINIO', 'STORAGE'),
    (gen_random_uuid()::text, 'storage_bucket',     '',      'STORAGE'),
    (gen_random_uuid()::text, 'storage_access_key', '',      'STORAGE'),
    (gen_random_uuid()::text, 'storage_secret_key', '',      'STORAGE'),
    (gen_random_uuid()::text, 'storage_endpoint',   '',      'STORAGE'),
    (gen_random_uuid()::text, 'storage_region',     '',      'STORAGE'),
    (gen_random_uuid()::text, 'storage_public_url', '',      'STORAGE');