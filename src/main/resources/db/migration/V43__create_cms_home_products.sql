CREATE TABLE IF NOT EXISTS public.cms_home_products (
    id            VARCHAR(36)  PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    name_kh       VARCHAR(255),
    description   TEXT,
    description_kh TEXT,
    icon          VARCHAR(100),
    icon_bg       VARCHAR(255),
    href          VARCHAR(500),
    sort_order    INTEGER      NOT NULL DEFAULT 0,
    created_by    VARCHAR(255) NOT NULL DEFAULT 'SYS',
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by    VARCHAR(255),
    updated_at    TIMESTAMP,
    status        VARCHAR(3)   NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_cms_home_products_status     ON public.cms_home_products (status);
CREATE INDEX IF NOT EXISTS idx_cms_home_products_sort_order ON public.cms_home_products (sort_order);

-- Seed defaults mirroring the 5 product cards previously hardcoded in ProductsSection.tsx.
-- The `icon` value is a Lucide icon name; the `icon_bg` is a Tailwind class string.
INSERT INTO public.cms_home_products (id, name, name_kh, description, description_kh, icon, icon_bg, href, sort_order) VALUES
    (gen_random_uuid()::text,
     'SOPPOS POS – Point of sale', 'SOPPOS POS – ចំណុចលក់',
     'Transform your smartphone or tablet into a free, powerful point of sale.',
     'ប្រែក្លាយស្មាតហ្វូន ឬថេប្លេតរបស់អ្នកទៅជាចំណុចលក់ដ៏មានឥទ្ធិភាព ដោយឥតគិតថ្លៃ។',
     'Cpu', 'bg-emerald-100 dark:bg-emerald-900/40', '/features', 1),
    (gen_random_uuid()::text,
     'SOPPOS Back Office', 'SOPPOS Back Office',
     'Easily manage your business in a browser from any device, anywhere.',
     'គ្រប់គ្រងអាជីវកម្មរបស់អ្នកយ៉ាងងាយស្រួលក្នុងកម្មវិធីរុករក ពីឧបករណ៍ណាមួយ គ្រប់ទីកន្លែង។',
     'Monitor', 'bg-orange-100 dark:bg-orange-900/40', '/features', 2),
    (gen_random_uuid()::text,
     'SOPPOS Dashboard', 'SOPPOS Dashboard',
     'Instant access to your store''s sales analytics and inventory in your pocket.',
     'ចូលប្រើភ្លាមៗនូវការវិភាគការលក់ និងស្តុកនៃហាងរបស់អ្នកនៅក្នុងហោប៉ៅ។',
     'LayoutDashboard', 'bg-blue-100 dark:bg-blue-900/40', '/features', 3),
    (gen_random_uuid()::text,
     'SOPPOS KDS', 'SOPPOS KDS',
     'Kitchen Display System: let your cooking staff know what to prepare instantly.',
     'ប្រព័ន្ធបង្ហាញផ្ទះបាយ៖ ឲ្យបុគ្គលិកចម្អិនអាហារដឹងភ្លាមៗនូវអ្វីដែលត្រូវរៀបចំ។',
     'Utensils', 'bg-purple-100 dark:bg-purple-900/40', '/features', 4),
    (gen_random_uuid()::text,
     'SOPPOS CDS', 'SOPPOS CDS',
     'Customer Display System: show order information at the time of purchase.',
     'ប្រព័ន្ធបង្ហាញអតិថិជន៖ បង្ហាញព័ត៌មានការបញ្ជាទិញនៅពេលទិញ។',
     'User', 'bg-teal-100 dark:bg-teal-900/40', '/features', 5);

-- Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'home_products.view',   'View Home Products',   'CONTENT_MANAGEMENT', 88),
  (gen_random_uuid()::text, 'home_products.create', 'Create Home Products', 'CONTENT_MANAGEMENT', 89),
  (gen_random_uuid()::text, 'home_products.update', 'Update Home Products', 'CONTENT_MANAGEMENT', 90),
  (gen_random_uuid()::text, 'home_products.delete', 'Delete Home Products', 'CONTENT_MANAGEMENT', 91)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('home_products.view', 'home_products.create', 'home_products.update', 'home_products.delete')
ON CONFLICT DO NOTHING;
