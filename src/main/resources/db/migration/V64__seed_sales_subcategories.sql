-- Rename the earlier nesting demo ("Refunds") to match the real Sales sub-topic structure,
-- and add its two sibling sub-topics.
UPDATE public.help_center_categories SET name = 'Sales & Refunds' WHERE slug = 'sales/refunds';

INSERT INTO public.help_center_categories (id, article_type_id, parent_id, name, slug, icon, display_order)
SELECT gen_random_uuid()::text, s.article_type_id, s.id, v.name, v.slug, v.icon, v.display_order
FROM public.help_center_categories s,
     (VALUES
        ('Sale Screen',      'sales/sale-screen',       'monitor',  2),
        ('Receipts & Bills', 'sales/receipts-and-bills', 'receipt', 3)
     ) AS v(name, slug, icon, display_order)
WHERE s.slug = 'sales'
ON CONFLICT (slug) DO NOTHING;

-- Sales & Refunds articles
INSERT INTO public.articles
  (id, title, slug, excerpt, content, type, tags, author_name, sort_order, view_count, workflow_status, status, published_at, created_by)
VALUES
  (gen_random_uuid()::text, 'How to Issue a Refund on SOPPOS POS', 'how-to-issue-a-refund-on-soppos-pos',
   'Process a full or partial refund for a completed sale.',
   '<p>Process a full or partial refund for a completed sale directly from the receipt list.</p>',
   'HELP_CENTER', 'sales', 'SOPPOS Team', 1, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'How to Sell Items by Weight', 'how-to-sell-items-by-weight',
   'Set up weighted items and ring them up using a connected scale.',
   '<p>Set up weighted items and ring them up using a connected scale, or enter the weight manually.</p>',
   'HELP_CENTER', 'sales', 'SOPPOS Team', 2, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'Offline Use of SOPPOS POS', 'offline-use-of-soppos-pos',
   'Keep selling even when the internet connection drops.',
   '<p>SOPPOS POS keeps working without an internet connection — sales sync automatically once you''re back online.</p>',
   'HELP_CENTER', 'sales', 'SOPPOS Team', 3, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'Dining Options', 'dining-options',
   'Configure dine-in, takeaway, and delivery options for your menu.',
   '<p>Configure dine-in, takeaway, and delivery options so staff can tag each sale correctly.</p>',
   'HELP_CENTER', 'sales', 'SOPPOS Team', 4, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'How to Sell Liquids', 'how-to-sell-liquids',
   'Sell drinks and other liquids by volume with fractional quantities.',
   '<p>Sell drinks and other liquids by volume, supporting fractional quantities like 0.5L.</p>',
   'HELP_CENTER', 'sales', 'SOPPOS Team', 5, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS')
ON CONFLICT (slug) DO NOTHING;

INSERT INTO public.article_categories (article_id, category_id)
SELECT a.id, c.id
FROM public.articles a, public.help_center_categories c
WHERE c.slug = 'sales/refunds'
  AND a.slug IN (
    'how-to-issue-a-refund-on-soppos-pos', 'how-to-sell-items-by-weight',
    'offline-use-of-soppos-pos', 'dining-options', 'how-to-sell-liquids',
    'how-to-make-sales'
  )
ON CONFLICT DO NOTHING;

-- Sale Screen articles
INSERT INTO public.articles
  (id, title, slug, excerpt, content, type, tags, author_name, sort_order, view_count, workflow_status, status, published_at, created_by)
VALUES
  (gen_random_uuid()::text, 'How to Arrange Sale Screen in SOPPOS POS', 'how-to-arrange-sale-screen-in-soppos-pos',
   'Reorder categories and items on the sale screen to match your workflow.',
   '<p>Reorder categories and items on the sale screen to match your checkout workflow.</p>',
   'HELP_CENTER', 'sales', 'SOPPOS Team', 6, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'How to Use Favorites on the Sale Screen of Smartphones', 'how-to-use-favorites-on-the-sale-screen-of-smartphones',
   'Pin your best-selling items to the Favorites tab for faster checkout.',
   '<p>Pin your best-selling items to the Favorites tab for faster checkout on smaller screens.</p>',
   'HELP_CENTER', 'sales', 'SOPPOS Team', 7, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'How to Change Home Sale Screen Layout', 'how-to-change-home-sale-screen-layout',
   'Switch between grid and list layouts for the sale screen.',
   '<p>Switch between grid and list layouts for the sale screen, and choose how many columns to show.</p>',
   'HELP_CENTER', 'sales', 'SOPPOS Team', 8, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS')
ON CONFLICT (slug) DO NOTHING;

INSERT INTO public.article_categories (article_id, category_id)
SELECT a.id, c.id
FROM public.articles a, public.help_center_categories c
WHERE c.slug = 'sales/sale-screen'
  AND a.slug IN (
    'how-to-arrange-sale-screen-in-soppos-pos',
    'how-to-use-favorites-on-the-sale-screen-of-smartphones',
    'how-to-change-home-sale-screen-layout'
  )
ON CONFLICT DO NOTHING;

-- Receipts & Bills articles
INSERT INTO public.articles
  (id, title, slug, excerpt, content, type, tags, author_name, sort_order, view_count, workflow_status, status, published_at, created_by)
VALUES
  (gen_random_uuid()::text, 'How to Print Bill', 'how-to-print-bill',
   'Print a bill for the table before payment is taken.',
   '<p>Print a bill for the table before payment is taken, without closing out the sale.</p>',
   'HELP_CENTER', 'sales', 'SOPPOS Team', 9, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'How to Reprint a Receipt', 'how-to-reprint-a-receipt',
   'Reprint a receipt for a past sale from the receipt history.',
   '<p>Reprint a receipt for a past sale from the receipt history at any time.</p>',
   'HELP_CENTER', 'sales', 'SOPPOS Team', 10, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'How to Customize Your Receipt Template', 'how-to-customize-your-receipt-template',
   'Add your logo, address, and a custom footer message to printed receipts.',
   '<p>Add your logo, address, and a custom footer message to printed receipts from Back Office settings.</p>',
   'HELP_CENTER', 'sales', 'SOPPOS Team', 11, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS')
ON CONFLICT (slug) DO NOTHING;

INSERT INTO public.article_categories (article_id, category_id)
SELECT a.id, c.id
FROM public.articles a, public.help_center_categories c
WHERE c.slug = 'sales/receipts-and-bills'
  AND a.slug IN ('how-to-print-bill', 'how-to-reprint-a-receipt', 'how-to-customize-your-receipt-template')
ON CONFLICT DO NOTHING;
