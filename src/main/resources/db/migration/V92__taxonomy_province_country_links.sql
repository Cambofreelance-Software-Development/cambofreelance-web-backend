-- Wire PROVINCE items up to their COUNTRY parent via the existing parent_code column
-- (added in V91 but left unused for PROVINCE), and add a second country (Vietnam) so
-- the Country -> Province cascading dropdown behavior is demonstrably real with more
-- than one country.

-- ── Backfill: all 25 existing Cambodia provinces become children of the 'KH' country item ──

UPDATE public.taxonomy_items
SET parent_code = 'KH'
WHERE taxonomy_code = 'PROVINCE' AND parent_code IS NULL;

-- ── Mark PROVINCE as hierarchical now that it's parented by COUNTRY ────────────────

UPDATE public.taxonomies
SET is_hierarchical = TRUE
WHERE code = 'PROVINCE';

-- ── Seed a second COUNTRY item: Vietnam ─────────────────────────────────────────────

INSERT INTO public.taxonomy_items (id, code, taxonomy_code, parent_code, display_en, display_km, sort_order, created_by) VALUES
  (gen_random_uuid()::text, 'VN', 'COUNTRY', NULL, 'Vietnam', 'វៀតណាម', 2, 'SYS')
ON CONFLICT (taxonomy_code, code) DO NOTHING;

-- ── Seed a small, high-confidence starter set of Vietnam PROVINCE items ────────────
-- Deliberately NOT a full Vietnam province list: Vietnam's provincial boundaries were
-- significantly reorganized in 2025, so a fabricated complete list here would likely
-- be wrong. This is just enough to prove out the Country -> Province cascade; admins
-- should complete/correct the rest via the /admin/taxonomy-items CRUD UI.

INSERT INTO public.taxonomy_items (id, code, taxonomy_code, parent_code, display_en, display_km, sort_order, created_by) VALUES
  (gen_random_uuid()::text, 'HANOI',       'PROVINCE', 'VN', 'Hanoi',            'ហាណូយ',    1, 'SYS'),
  (gen_random_uuid()::text, 'HO_CHI_MINH', 'PROVINCE', 'VN', 'Ho Chi Minh City', 'ហូជីមិញ',   2, 'SYS'),
  (gen_random_uuid()::text, 'HAI_PHONG',   'PROVINCE', 'VN', 'Hai Phong',        'ហាយផុង',    3, 'SYS'),
  (gen_random_uuid()::text, 'DA_NANG',     'PROVINCE', 'VN', 'Da Nang',          'ដាណាំង',    4, 'SYS'),
  (gen_random_uuid()::text, 'CAN_THO',     'PROVINCE', 'VN', 'Can Tho',          'កឹនធើ',     5, 'SYS'),
  (gen_random_uuid()::text, 'HUE',         'PROVINCE', 'VN', 'Hue',              'ហ្វេ',      6, 'SYS')
ON CONFLICT (taxonomy_code, code) DO NOTHING;
