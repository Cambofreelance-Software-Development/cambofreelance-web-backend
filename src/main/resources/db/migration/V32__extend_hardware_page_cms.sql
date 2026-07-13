-- Category enrichment: banner image, icon, section blurb, and "more models" help link
ALTER TABLE public.category_hardware
    ADD COLUMN IF NOT EXISTS description    TEXT,
    ADD COLUMN IF NOT EXISTS description_kh TEXT,
    ADD COLUMN IF NOT EXISTS image_id       VARCHAR(36) REFERENCES public.media_files(id),
    ADD COLUMN IF NOT EXISTS icon           VARCHAR(100),
    ADD COLUMN IF NOT EXISTS more_link      VARCHAR(500);

CREATE INDEX IF NOT EXISTS idx_category_hardware_image_id ON public.category_hardware (image_id);

-- Hardware item: comma-separated country availability (used by regional card readers)
ALTER TABLE public.hardware
    ADD COLUMN IF NOT EXISTS countries VARCHAR(500);

-- Seed default hardware page settings (hero + download CTAs)
INSERT INTO public.cms_settings (setting_id, setting_key, setting_value, setting_group) VALUES
    (gen_random_uuid()::text, 'hardware_hero_title',          'Hardware for POS', 'HARDWARE'),
    (gen_random_uuid()::text, 'hardware_hero_title_kh',       'ឧបករណ៍សម្រាប់ POS', 'HARDWARE'),
    (gen_random_uuid()::text, 'hardware_hero_subtitle',       'Tested and recommended for use', 'HARDWARE'),
    (gen_random_uuid()::text, 'hardware_hero_subtitle_kh',    'បានសាកល្បង និងណែនាំសម្រាប់ការប្រើប្រាស់', 'HARDWARE'),
    (gen_random_uuid()::text, 'hardware_hero_cta_label',      'Buy hardware', 'HARDWARE'),
    (gen_random_uuid()::text, 'hardware_hero_cta_label_kh',   'ទិញឧបករណ៍', 'HARDWARE'),
    (gen_random_uuid()::text, 'hardware_hero_cta_link',       '', 'HARDWARE'),
    (gen_random_uuid()::text, 'hardware_download_android_url','', 'HARDWARE'),
    (gen_random_uuid()::text, 'hardware_download_ios_url',    '', 'HARDWARE')
ON CONFLICT (setting_key) DO NOTHING;
