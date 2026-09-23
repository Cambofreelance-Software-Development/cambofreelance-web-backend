-- Generic Taxonomy / Taxonomy-Item CRUD engine.
-- Lets admins manage reusable "lookup lists" (Country, Province/City, Business Type, ...)
-- that back <select> options on public forms (e.g. the Partner Application form),
-- instead of hardcoding those option lists in the frontend.

CREATE TABLE IF NOT EXISTS public.taxonomies (
    id              VARCHAR(36)   PRIMARY KEY,
    code            VARCHAR(80)   NOT NULL,
    name            VARCHAR(150)  NOT NULL,
    is_hierarchical BOOLEAN       NOT NULL DEFAULT FALSE,
    description     VARCHAR(500),
    remark          VARCHAR(500),
    created_by      VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      VARCHAR(255),
    updated_at      TIMESTAMP,
    status          VARCHAR(3)    NOT NULL DEFAULT 'ACT',
    CONSTRAINT uq_taxonomies_code UNIQUE (code)
);

CREATE TABLE IF NOT EXISTS public.taxonomy_items (
    id              VARCHAR(36)   PRIMARY KEY,
    code            VARCHAR(80)   NOT NULL,
    taxonomy_code   VARCHAR(80)   NOT NULL REFERENCES public.taxonomies(code),
    parent_code     VARCHAR(80),
    display_en      VARCHAR(150)  NOT NULL,
    display_km      VARCHAR(150),
    metadata        VARCHAR(1000),
    sort_order      INTEGER       NOT NULL DEFAULT 0,
    created_by      VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      VARCHAR(255),
    updated_at      TIMESTAMP,
    status          VARCHAR(3)    NOT NULL DEFAULT 'ACT',
    CONSTRAINT uq_taxonomy_items_taxonomy_code_code UNIQUE (taxonomy_code, code)
);

CREATE INDEX IF NOT EXISTS idx_taxonomy_items_taxonomy_status_sort
    ON public.taxonomy_items (taxonomy_code, status, sort_order);

-- Permissions (sort_order continues after the highest CONTENT_MANAGEMENT value used so far,
-- which is business_type_catalog.delete = 111 in V69).
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'taxonomy.view',        'View Taxonomies',       'CONTENT_MANAGEMENT', 112),
  (gen_random_uuid()::text, 'taxonomy.create',      'Create Taxonomies',     'CONTENT_MANAGEMENT', 113),
  (gen_random_uuid()::text, 'taxonomy.update',      'Update Taxonomies',     'CONTENT_MANAGEMENT', 114),
  (gen_random_uuid()::text, 'taxonomy.delete',      'Delete Taxonomies',     'CONTENT_MANAGEMENT', 115),
  (gen_random_uuid()::text, 'taxonomy-item.view',   'View Taxonomy Items',   'CONTENT_MANAGEMENT', 116),
  (gen_random_uuid()::text, 'taxonomy-item.create', 'Create Taxonomy Items', 'CONTENT_MANAGEMENT', 117),
  (gen_random_uuid()::text, 'taxonomy-item.update', 'Update Taxonomy Items', 'CONTENT_MANAGEMENT', 118),
  (gen_random_uuid()::text, 'taxonomy-item.delete', 'Delete Taxonomy Items', 'CONTENT_MANAGEMENT', 119)
ON CONFLICT (code) DO NOTHING;

-- Auto-assign to ADMIN / SUPER_ADMIN
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('taxonomy.view', 'taxonomy.create', 'taxonomy.update', 'taxonomy.delete',
                 'taxonomy-item.view', 'taxonomy-item.create', 'taxonomy-item.update', 'taxonomy-item.delete')
ON CONFLICT DO NOTHING;

-- ── Seed taxonomies ─────────────────────────────────────────────────────────
-- Starting point only — admins can edit/correct via the CRUD UI.

INSERT INTO public.taxonomies (id, code, name, is_hierarchical, description, remark, created_by) VALUES
  (gen_random_uuid()::text, 'COUNTRY', 'Country', FALSE,
   'Countries used as select options on public forms (e.g. Partner Application).', NULL, 'SYS'),
  (gen_random_uuid()::text, 'PROVINCE', 'Province / City', FALSE,
   'Cambodian provinces/cities used as select options on public forms (e.g. Partner Application).', NULL, 'SYS'),
  (gen_random_uuid()::text, 'BUSINESS_TYPE', 'Business Type', FALSE,
   'Business types used as select options on public forms (e.g. Partner Application).', NULL, 'SYS')
ON CONFLICT (code) DO NOTHING;

-- ── Seed COUNTRY items ──────────────────────────────────────────────────────

INSERT INTO public.taxonomy_items (id, code, taxonomy_code, parent_code, display_en, display_km, sort_order, created_by) VALUES
  (gen_random_uuid()::text, 'KH', 'COUNTRY', NULL, 'Cambodia', 'កម្ពុជា', 1, 'SYS')
ON CONFLICT (taxonomy_code, code) DO NOTHING;

-- ── Seed PROVINCE items (25 provinces/cities, Phnom Penh = 1) ──────────────

INSERT INTO public.taxonomy_items (id, code, taxonomy_code, parent_code, display_en, display_km, sort_order, created_by) VALUES
  (gen_random_uuid()::text, 'PHNOM_PENH',       'PROVINCE', NULL, 'Phnom Penh',       'ភ្នំពេញ',              1,  'SYS'),
  (gen_random_uuid()::text, 'BANTEAY_MEANCHEY', 'PROVINCE', NULL, 'Banteay Meanchey', 'បន្ទាយមានជ័យ',          2,  'SYS'),
  (gen_random_uuid()::text, 'BATTAMBANG',       'PROVINCE', NULL, 'Battambang',       'បាត់ដំបង',              3,  'SYS'),
  (gen_random_uuid()::text, 'KAMPONG_CHAM',     'PROVINCE', NULL, 'Kampong Cham',     'កំពង់ចាម',              4,  'SYS'),
  (gen_random_uuid()::text, 'KAMPONG_CHHNANG',  'PROVINCE', NULL, 'Kampong Chhnang',  'កំពង់ឆ្នាំង',            5,  'SYS'),
  (gen_random_uuid()::text, 'KAMPONG_SPEU',     'PROVINCE', NULL, 'Kampong Speu',     'កំពង់ស្ពឺ',              6,  'SYS'),
  (gen_random_uuid()::text, 'KAMPONG_THOM',     'PROVINCE', NULL, 'Kampong Thom',     'កំពង់ធំ',               7,  'SYS'),
  (gen_random_uuid()::text, 'KAMPOT',           'PROVINCE', NULL, 'Kampot',           'កំពត',                 8,  'SYS'),
  (gen_random_uuid()::text, 'KANDAL',           'PROVINCE', NULL, 'Kandal',           'កណ្ដាល',               9,  'SYS'),
  (gen_random_uuid()::text, 'KEP',              'PROVINCE', NULL, 'Kep',              'កែប',                  10, 'SYS'),
  (gen_random_uuid()::text, 'KOH_KONG',         'PROVINCE', NULL, 'Koh Kong',         'កោះកុង',               11, 'SYS'),
  (gen_random_uuid()::text, 'KRATIE',           'PROVINCE', NULL, 'Kratie',           'ក្រចេះ',                12, 'SYS'),
  (gen_random_uuid()::text, 'MONDULKIRI',       'PROVINCE', NULL, 'Mondulkiri',       'មណ្ឌលគិរី',             13, 'SYS'),
  (gen_random_uuid()::text, 'ODDAR_MEANCHEY',   'PROVINCE', NULL, 'Oddar Meanchey',   'ឧត្តរមានជ័យ',           14, 'SYS'),
  (gen_random_uuid()::text, 'PAILIN',           'PROVINCE', NULL, 'Pailin',           'ប៉ៃលិន',                15, 'SYS'),
  (gen_random_uuid()::text, 'PREAH_VIHEAR',     'PROVINCE', NULL, 'Preah Vihear',     'ព្រះវិហារ',             16, 'SYS'),
  (gen_random_uuid()::text, 'PREY_VENG',        'PROVINCE', NULL, 'Prey Veng',        'ព្រៃវែង',               17, 'SYS'),
  (gen_random_uuid()::text, 'PURSAT',           'PROVINCE', NULL, 'Pursat',           'ពោធិ៍សាត់',             18, 'SYS'),
  (gen_random_uuid()::text, 'RATANAKIRI',       'PROVINCE', NULL, 'Ratanakiri',       'រតនគិរី',               19, 'SYS'),
  (gen_random_uuid()::text, 'SIEM_REAP',        'PROVINCE', NULL, 'Siem Reap',        'សៀមរាប',               20, 'SYS'),
  (gen_random_uuid()::text, 'PREAH_SIHANOUK',   'PROVINCE', NULL, 'Preah Sihanouk',   'ព្រះសីហនុ',             21, 'SYS'),
  (gen_random_uuid()::text, 'STUNG_TRENG',      'PROVINCE', NULL, 'Stung Treng',      'ស្ទឹងត្រែង',             22, 'SYS'),
  (gen_random_uuid()::text, 'SVAY_RIENG',       'PROVINCE', NULL, 'Svay Rieng',       'ស្វាយរៀង',              23, 'SYS'),
  (gen_random_uuid()::text, 'TAKEO',            'PROVINCE', NULL, 'Takeo',            'តាកែវ',                 24, 'SYS'),
  (gen_random_uuid()::text, 'TBOUNG_KHMUM',     'PROVINCE', NULL, 'Tboung Khmum',     'ត្បូងឃ្មុំ',              25, 'SYS')
ON CONFLICT (taxonomy_code, code) DO NOTHING;

-- ── Seed BUSINESS_TYPE items ────────────────────────────────────────────────

INSERT INTO public.taxonomy_items (id, code, taxonomy_code, parent_code, display_en, display_km, sort_order, created_by) VALUES
  (gen_random_uuid()::text, 'RETAIL',            'BUSINESS_TYPE', NULL, 'Retail',                  'លក់រាយ',                    1, 'SYS'),
  (gen_random_uuid()::text, 'WHOLESALE',         'BUSINESS_TYPE', NULL, 'Wholesale / Distributor', 'លក់ដុំ / ចែកចាយ',            2, 'SYS'),
  (gen_random_uuid()::text, 'RESTAURANT',        'BUSINESS_TYPE', NULL, 'Restaurant / Cafe',       'ភោជនីយដ្ឋាន / កាហ្វេ',        3, 'SYS'),
  (gen_random_uuid()::text, 'HOSPITALITY',       'BUSINESS_TYPE', NULL, 'Hospitality',             'សេវាកម្មបដិសណ្ឋារកិច្ច',      4, 'SYS'),
  (gen_random_uuid()::text, 'IT_RESELLER',       'BUSINESS_TYPE', NULL, 'IT Reseller',             'អ្នកចែកចាយផលិតផលព័ត៌មានវិទ្យា', 5, 'SYS'),
  (gen_random_uuid()::text, 'SYSTEM_INTEGRATOR', 'BUSINESS_TYPE', NULL, 'System Integrator',       'អ្នកបញ្ចូលប្រព័ន្ធ',          6, 'SYS'),
  (gen_random_uuid()::text, 'OTHER',             'BUSINESS_TYPE', NULL, 'Other',                   'ផ្សេងៗ',                    7, 'SYS')
ON CONFLICT (taxonomy_code, code) DO NOTHING;
