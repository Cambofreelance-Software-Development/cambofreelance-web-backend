-- Help Center article type (Category rows in V62 hang off this).
INSERT INTO public.article_types (id, code, label, label_kh, icon, sort_order)
VALUES (gen_random_uuid(), 'HELP_CENTER', 'Help Center', 'មជ្ឈមណ្ឌលជំនួយ', 'life-buoy', 20)
ON CONFLICT (code) DO NOTHING;

-- Top-level topics
WITH hc_type AS (SELECT id FROM public.article_types WHERE code = 'HELP_CENTER')
INSERT INTO public.help_center_categories (id, article_type_id, parent_id, name, name_kh, slug, icon, display_order)
SELECT gen_random_uuid()::text, hc_type.id, NULL, v.name, v.name_kh, v.slug, v.icon, v.display_order
FROM hc_type, (VALUES
    ('Getting Started', 'ការចាប់ផ្តើម', 'getting-started', 'rocket',      1),
    ('Sales',           'ការលក់',        'sales',           'tag',         2),
    ('Items',           'ទំនិញ',         'items',           'package',     3),
    ('Inventory',       'ស្តុកទំនិញ',    'inventory',       'archive',     4),
    ('Employees',       'បុគ្គលិក',      'employees',       'users',       5),
    ('Customers',       'អតិថិជន',      'customers',       'user-circle', 6),
    ('Settings',        'ការកំណត់',     'settings',        'settings',    7),
    ('Hardware',        'ឧបករណ៍',       'hardware',        'cpu',         8),
    ('Payments',        'ការទូទាត់',    'payments',        'credit-card', 9)
) AS v(name, name_kh, slug, icon, display_order)
ON CONFLICT (slug) DO NOTHING;

-- Nested example: Sales -> Refunds (proves unlimited-depth nesting via parent_id)
INSERT INTO public.help_center_categories (id, article_type_id, parent_id, name, slug, icon, display_order)
SELECT gen_random_uuid()::text, s.article_type_id, s.id, 'Refunds', 'sales/refunds', 'rotate-ccw', 1
FROM public.help_center_categories s WHERE s.slug = 'sales'
ON CONFLICT (slug) DO NOTHING;

-- Getting Started articles (type = HELP_CENTER; topic membership comes from article_categories)
INSERT INTO public.articles
  (id, title, title_kh, slug, excerpt, content,
   type, tags, video_link, author_name, sort_order, view_count, workflow_status, status, published_at, created_by)
VALUES
  (gen_random_uuid()::text,
   'How to Get Started with SOPPOS POS', 'របៀបចាប់ផ្តើមប្រើ SOPPOS POS',
   'how-to-get-started-with-soppos-pos',
   'To begin using SOPPOS POS, follow these steps to download, install, and set up the app on your device.',
   '<h2>1. Download and Installation</h2><p>To begin using SOPPOS POS, follow these steps to download, install, and set up the app on your device.</p><p>Search for &quot;SOPPOS POS&quot; on the App Store or Google Play and install it on your phone or tablet.</p><h2>2. Create Your Account</h2><p>Open the app and sign up with your email address to create your SOPPOS account.</p>',
   'HELP_CENTER', 'getting-started',
   'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
   'SOPPOS Team', 1, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text,
   'Setting Up Your Shop in SOPPOS Back Office', 'ការរៀបចំហាងរបស់អ្នកនៅក្នុង SOPPOS Back Office',
   'setting-up-your-shop-in-soppos-back-office',
   'Configure your shop name, currency, tax rate and receipt template before your first sale.',
   '<p>Configure your shop name, currency, tax rate and receipt template before your first sale.</p>',
   'HELP_CENTER', 'getting-started',
   NULL, 'SOPPOS Team', 2, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text,
   'How to Add Items and Categories in SOPPOS POS', 'របៀបបន្ថែមទំនិញ និងប្រភេទនៅក្នុង SOPPOS POS',
   'how-to-add-items-and-categories-in-soppos-pos',
   'Add products, set prices, and organize them into categories so your staff can find them fast.',
   '<p>Add products, set prices, and organize them into categories so your staff can find them fast.</p>',
   'HELP_CENTER', 'getting-started',
   NULL, 'SOPPOS Team', 3, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text,
   'How to Make Sales', 'របៀបធ្វើការលក់',
   'how-to-make-sales',
   'Ring up your first sale: add items to the cart, apply discounts, and take payment.',
   '<p>Ring up your first sale: add items to the cart, apply discounts, and take payment.</p>',
   'HELP_CENTER', 'getting-started',
   NULL, 'SOPPOS Team', 4, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text,
   'How to Cancel Receipts in SOPPOS Back Office', 'របៀបលុបចោលបង្កាន់ដៃនៅក្នុង SOPPOS Back Office',
   'how-to-cancel-receipts-in-soppos-back-office',
   'Void or refund a completed sale from the Back Office when a receipt needs to be cancelled.',
   '<p>Void or refund a completed sale from the Back Office when a receipt needs to be cancelled.</p>',
   'HELP_CENTER', 'getting-started',
   NULL, 'SOPPOS Team', 5, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS')
ON CONFLICT (slug) DO NOTHING;

-- A sub-article demonstrating unlimited article nesting (parent_article_id -> "Details" level)
INSERT INTO public.articles
  (id, parent_article_id, title, slug, content, type, workflow_status, status, published_at, created_by)
SELECT gen_random_uuid()::text, a.id,
       'Download and Installation — Step by Step',
       'how-to-get-started-with-soppos-pos/download-and-installation',
       '<p>Detailed walkthrough for installing SOPPOS POS on iOS, Android, and desktop devices...</p>',
       'HELP_CENTER', 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'
FROM public.articles a WHERE a.slug = 'how-to-get-started-with-soppos-pos'
ON CONFLICT (slug) DO NOTHING;

-- Link Getting Started articles into the "Getting Started" category
INSERT INTO public.article_categories (article_id, category_id)
SELECT a.id, c.id
FROM public.articles a, public.help_center_categories c
WHERE c.slug = 'getting-started'
  AND a.slug IN (
    'how-to-get-started-with-soppos-pos',
    'setting-up-your-shop-in-soppos-back-office',
    'how-to-add-items-and-categories-in-soppos-pos',
    'how-to-make-sales',
    'how-to-cancel-receipts-in-soppos-back-office'
  )
ON CONFLICT DO NOTHING;

-- Cross-listed example: the same "How to Make Sales" article also shows up under Sales
-- (proves the many-to-many relationship — one article, multiple topics)
INSERT INTO public.article_categories (article_id, category_id)
SELECT a.id, c.id
FROM public.articles a, public.help_center_categories c
WHERE a.slug = 'how-to-make-sales' AND c.slug = 'sales'
ON CONFLICT DO NOTHING;
