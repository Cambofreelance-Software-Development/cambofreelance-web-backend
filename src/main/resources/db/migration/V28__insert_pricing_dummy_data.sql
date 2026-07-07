-- Plans
INSERT INTO public.pricing_plan
  (id, name, name_kh, description, description_kh, price_monthly, currency,
   includes_note, includes_note_kh, badge, badge_kh, highlighted, cta_label, cta_label_kh, cta_link, sort_order)
VALUES
  (gen_random_uuid()::text, 'Starter', 'ស្ដាធើ', 'Basic foundation for small shops', 'គម្រោងមូលដ្ឋានសម្រាប់ហាងតូច',
   9, '$', NULL, NULL, NULL, NULL, FALSE, 'Start now', 'ចាប់ផ្ដើមឥឡូវនេះ', '/register', 1),
  (gen_random_uuid()::text, 'Growth', 'គ្រូធ', 'For growing businesses', 'សម្រាប់អាជីវកម្មកំពុងរីកចម្រើន',
   19, '$', 'Everything in $9 plus:', 'រាប់បញ្ចូលទាំង $9 និង៖', NULL, NULL, FALSE, 'Start now', 'ចាប់ផ្ដើមឥឡូវនេះ', '/register', 2),
  (gen_random_uuid()::text, 'Complete', 'ខមផ្លីត', 'Complete business solution', 'ដំណោះស្រាយអាជីវកម្មពេញលេញ',
   25, '$', 'Everything in $19 plus:', 'រាប់បញ្ចូលទាំង $19 និង៖', 'Most Popular', 'ពេញនិយមបំផុត', TRUE, 'Choose $25', 'ជ្រើសរើសយក $25', '/register', 3),
  (gen_random_uuid()::text, 'Enterprise', 'អង្គភាព', 'For large enterprises', 'សម្រាប់សហគ្រាសខ្នាតធំ',
   99, '$', 'Everything in $25 plus:', 'រាប់បញ្ចូលទាំង $25 និង៖', NULL, NULL, FALSE, 'Contact sales', 'ទាក់ទងផ្នែកលក់', '/contact', 4),
  (gen_random_uuid()::text, 'Ultimate', 'កំពូល', 'Top-tier solution', 'ដំណោះស្រាយកម្រិតខ្ពស់បំផុត',
   150, '$', 'Everything included:', 'រាប់បញ្ចូលទាំងអស់៖', NULL, NULL, FALSE, 'Contact sales', 'ទាក់ទងផ្នែកលក់', '/contact', 5)
ON CONFLICT DO NOTHING;

-- Plan card bullets
INSERT INTO public.pricing_plan_feature (id, plan_id, label, label_kh, sort_order)
SELECT gen_random_uuid()::text, pl.id, b.label, b.label_kh, b.sort_order
FROM (VALUES
  (9,   'POS Cashier',            NULL, 1),
  (9,   'Inventory',              NULL, 2),
  (9,   '1 Store',                NULL, 3),
  (9,   '2 Users',                NULL, 4),
  (9,   '1,000 Products',         NULL, 5),
  (19,  'Purchasing',             NULL, 1),
  (19,  'Expense Account',        NULL, 2),
  (19,  'Promotion',              NULL, 3),
  (19,  '10 Users',               NULL, 4),
  (19,  'Unlimited Products',     NULL, 5),
  (25,  'CRM & Marketing',        NULL, 1),
  (25,  'Advanced Inventory',     NULL, 2),
  (25,  'Sales (SO/DO)',          NULL, 3),
  (25,  'REST API Access',        NULL, 4),
  (25,  '2 Stores / 50 Users',    NULL, 5),
  (99,  'HR & Payroll',           NULL, 1),
  (99,  'Manufacturing / Project',NULL, 2),
  (99,  'Finance & AI Tools',     NULL, 3),
  (99,  'Security & Audit Logs',  NULL, 4),
  (99,  '4 Stores / 100 Users',   NULL, 5),
  (150, 'Full Menu Structure',    NULL, 1),
  (150, 'Advanced Configuration', NULL, 2),
  (150, 'Multi Branch Support',   NULL, 3),
  (150, 'Priority 24/7 Support',  NULL, 4),
  (150, '10 Stores / 100 Users',  NULL, 5)
) AS b(price, label, label_kh, sort_order)
JOIN public.pricing_plan pl ON pl.price_monthly = b.price;

-- Comparison feature rows
INSERT INTO public.pricing_feature (id, name, name_kh, sort_order)
VALUES
  (gen_random_uuid()::text, 'Dashboard & Reports',        NULL, 1),
  (gen_random_uuid()::text, 'POS Cashier & Items',        NULL, 2),
  (gen_random_uuid()::text, 'Inventory & Adv. Inventory', NULL, 3),
  (gen_random_uuid()::text, 'Purchasing & Purchase',      NULL, 4),
  (gen_random_uuid()::text, 'Accounting & Finance',       NULL, 5),
  (gen_random_uuid()::text, 'CRM & Marketing (Coupon)',   NULL, 6),
  (gen_random_uuid()::text, 'Sales (SO/DO) & API',        NULL, 7),
  (gen_random_uuid()::text, 'HR & Manufacturing',         NULL, 8),
  (gen_random_uuid()::text, 'Project & Service',          NULL, 9),
  (gen_random_uuid()::text, 'AI & Security (Audit)',      NULL, 10),
  (gen_random_uuid()::text, 'Configuration & Menus',      NULL, 11),
  (gen_random_uuid()::text, 'Stores / Users / Products',  NULL, 12)
ON CONFLICT DO NOTHING;

-- Comparison matrix values (plan price x feature name -> value)
INSERT INTO public.pricing_plan_value (id, plan_id, feature_id, value)
SELECT gen_random_uuid()::text, pl.id, f.id, v.value
FROM (VALUES
  (9,   'Dashboard & Reports',        'View'),
  (19,  'Dashboard & Reports',        'View'),
  (25,  'Dashboard & Reports',        'Export'),
  (99,  'Dashboard & Reports',        'Export'),
  (150, 'Dashboard & Reports',        'Export'),
  (9,   'POS Cashier & Items',        'yes'),
  (19,  'POS Cashier & Items',        'yes'),
  (25,  'POS Cashier & Items',        'yes'),
  (99,  'POS Cashier & Items',        'yes'),
  (150, 'POS Cashier & Items',        'yes'),
  (9,   'Inventory & Adv. Inventory', 'yes'),
  (19,  'Inventory & Adv. Inventory', 'yes'),
  (25,  'Inventory & Adv. Inventory', 'yes'),
  (99,  'Inventory & Adv. Inventory', 'yes'),
  (150, 'Inventory & Adv. Inventory', 'yes'),
  (9,   'Purchasing & Purchase',      'no'),
  (19,  'Purchasing & Purchase',      'yes'),
  (25,  'Purchasing & Purchase',      'yes'),
  (99,  'Purchasing & Purchase',      'yes'),
  (150, 'Purchasing & Purchase',      'yes'),
  (9,   'Accounting & Finance',       'no'),
  (19,  'Accounting & Finance',       'yes'),
  (25,  'Accounting & Finance',       'yes'),
  (99,  'Accounting & Finance',       'yes'),
  (150, 'Accounting & Finance',       'yes'),
  (9,   'CRM & Marketing (Coupon)',   'no'),
  (19,  'CRM & Marketing (Coupon)',   'no'),
  (25,  'CRM & Marketing (Coupon)',   'yes'),
  (99,  'CRM & Marketing (Coupon)',   'yes'),
  (150, 'CRM & Marketing (Coupon)',   'yes'),
  (9,   'Sales (SO/DO) & API',        'no'),
  (19,  'Sales (SO/DO) & API',        'no'),
  (25,  'Sales (SO/DO) & API',        'yes'),
  (99,  'Sales (SO/DO) & API',        'yes'),
  (150, 'Sales (SO/DO) & API',        'yes'),
  (9,   'HR & Manufacturing',         'no'),
  (19,  'HR & Manufacturing',         'no'),
  (25,  'HR & Manufacturing',         'no'),
  (99,  'HR & Manufacturing',         'yes'),
  (150, 'HR & Manufacturing',         'yes'),
  (9,   'Project & Service',          'no'),
  (19,  'Project & Service',          'no'),
  (25,  'Project & Service',          'no'),
  (99,  'Project & Service',          'yes'),
  (150, 'Project & Service',          'yes'),
  (9,   'AI & Security (Audit)',      'no'),
  (19,  'AI & Security (Audit)',      'no'),
  (25,  'AI & Security (Audit)',      'no'),
  (99,  'AI & Security (Audit)',      'yes'),
  (150, 'AI & Security (Audit)',      'yes'),
  (9,   'Configuration & Menus',      'no'),
  (19,  'Configuration & Menus',      'no'),
  (25,  'Configuration & Menus',      'no'),
  (99,  'Configuration & Menus',      'no'),
  (150, 'Configuration & Menus',      'yes'),
  (9,   'Stores / Users / Products',  '1 Store / 2 Users / 1K'),
  (19,  'Stores / Users / Products',  '1 Store / 10 Users / ∞'),
  (25,  'Stores / Users / Products',  '2 Stores / 50 Users / ∞'),
  (99,  'Stores / Users / Products',  '4 Stores / 100 Users / ∞'),
  (150, 'Stores / Users / Products',  '10 Stores / 100 Users / ∞')
) AS v(price, fname, value)
JOIN public.pricing_plan    pl ON pl.price_monthly = v.price
JOIN public.pricing_feature f  ON f.name = v.fname;
