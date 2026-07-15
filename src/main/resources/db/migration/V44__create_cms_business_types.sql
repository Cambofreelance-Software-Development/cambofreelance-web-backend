CREATE TABLE IF NOT EXISTS public.cms_business_type_groups (
    id            VARCHAR(36)  PRIMARY KEY,
    title         VARCHAR(255) NOT NULL,
    title_kh      VARCHAR(255),
    icon          VARCHAR(100),
    sort_order    INTEGER      NOT NULL DEFAULT 0,
    created_by    VARCHAR(255) NOT NULL DEFAULT 'SYS',
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by    VARCHAR(255),
    updated_at    TIMESTAMP,
    status        VARCHAR(3)   NOT NULL DEFAULT 'ACT'
);

CREATE TABLE IF NOT EXISTS public.cms_business_type_tags (
    id            VARCHAR(36)  PRIMARY KEY,
    group_id      VARCHAR(36)  NOT NULL REFERENCES public.cms_business_type_groups(id) ON DELETE CASCADE,
    label         VARCHAR(255) NOT NULL,
    label_kh      VARCHAR(255),
    sort_order    INTEGER      NOT NULL DEFAULT 0,
    created_by    VARCHAR(255) NOT NULL DEFAULT 'SYS',
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by    VARCHAR(255),
    updated_at    TIMESTAMP,
    status        VARCHAR(3)   NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_business_type_groups_status     ON public.cms_business_type_groups (status);
CREATE INDEX IF NOT EXISTS idx_business_type_groups_sort_order ON public.cms_business_type_groups (sort_order);
CREATE INDEX IF NOT EXISTS idx_business_type_tags_group_id     ON public.cms_business_type_tags (group_id);
CREATE INDEX IF NOT EXISTS idx_business_type_tags_status       ON public.cms_business_type_tags (status);

-- Seed defaults mirroring the 3 groups previously hardcoded in BusinessTypesSection.tsx
DO $$
DECLARE
    food_id    VARCHAR(36) := gen_random_uuid()::text;
    retail_id  VARCHAR(36) := gen_random_uuid()::text;
    service_id VARCHAR(36) := gen_random_uuid()::text;
BEGIN
    INSERT INTO public.cms_business_type_groups (id, title, title_kh, icon, sort_order) VALUES
        (food_id,    'Food & Drink', 'អាហារ និងភេសជ្ជៈ', 'Utensils',    1),
        (retail_id,  'Retail',       'រាយតម្លៃ',         'ShoppingBag', 2),
        (service_id, 'Services',     'សេវាកម្ម',         'Sparkles',    3);

    INSERT INTO public.cms_business_type_tags (id, group_id, label, label_kh, sort_order) VALUES
        (gen_random_uuid()::text, food_id, 'Restaurant',        'ភោជនីយដ្ឋាន',      1),
        (gen_random_uuid()::text, food_id, 'Café',              'ហាងកាហ្វេ',        2),
        (gen_random_uuid()::text, food_id, 'Coffee shop',       'ហាងកាហ្វេ',        3),
        (gen_random_uuid()::text, food_id, 'Fast food',         'អាហាររហ័ស',        4),
        (gen_random_uuid()::text, food_id, 'Pizzeria',          'ភីហ្សា',           5),
        (gen_random_uuid()::text, food_id, 'Bar',               'បារ',              6),
        (gen_random_uuid()::text, food_id, 'Bakery',            'ហាងនំបុ័ង',        7),

        (gen_random_uuid()::text, retail_id, 'Grocery store',     'ហាងលក់គ្រឿងទេស', 1),
        (gen_random_uuid()::text, retail_id, 'Convenience store', 'ហាងលក់ភ្លាមៗ',    2),
        (gen_random_uuid()::text, retail_id, 'Liquor store',      'ហាងលក់ស្រា',      3),
        (gen_random_uuid()::text, retail_id, 'Fashion',           'ម៉ូត',            4),
        (gen_random_uuid()::text, retail_id, 'Jewelry',           'គ្រឿងអលង្ការ',    5),
        (gen_random_uuid()::text, retail_id, 'Kiosk',             'តូបលក់',          6),

        (gen_random_uuid()::text, service_id, 'Transportation', 'ដឹកជញ្ជូន',        1),
        (gen_random_uuid()::text, service_id, 'Carwash',        'លាងឡាន',           2),
        (gen_random_uuid()::text, service_id, 'Salon',          'ហាងកាត់សក់',       3),
        (gen_random_uuid()::text, service_id, 'Rentals',        'ជួល',              4),
        (gen_random_uuid()::text, service_id, 'Hotels',         'សណ្ឋាគារ',          5);
END $$;

-- Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'business_types.view',   'View Business Types',   'CONTENT_MANAGEMENT', 92),
  (gen_random_uuid()::text, 'business_types.create', 'Create Business Types', 'CONTENT_MANAGEMENT', 93),
  (gen_random_uuid()::text, 'business_types.update', 'Update Business Types', 'CONTENT_MANAGEMENT', 94),
  (gen_random_uuid()::text, 'business_types.delete', 'Delete Business Types', 'CONTENT_MANAGEMENT', 95)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('business_types.view', 'business_types.create', 'business_types.update', 'business_types.delete')
ON CONFLICT DO NOTHING;
