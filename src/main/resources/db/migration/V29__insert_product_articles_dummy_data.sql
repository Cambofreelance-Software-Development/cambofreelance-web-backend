-- Seed PRODUCTS-type articles (power the Products nav dropdown, /products landing & detail pages)
INSERT INTO public.articles
  (id, title, title_kh, slug, excerpt, excerpt_kh, content, content_kh,
   type, tags, sort_order, view_count, workflow_status, status, published_at, created_by)
VALUES
  (gen_random_uuid()::text,
   'POS – Point of Sale', 'ការលក់ (POS)', 'pos',
   'Transform your smartphone or tablet into a free, powerful point of sale.',
   'ប្រែក្លាយស្មាតហ្វូន ឬថេប្លេតរបស់អ្នកទៅជាប្រព័ន្ធ POS ដ៏មានឥទ្ធិភាព ដោយឥតគិតថ្លៃ។',
   '<p>Ring up sales, apply discounts, and accept multiple payment types — even while offline. The POS app works on Android and iOS phones and tablets.</p><ul><li>Fast checkout with barcode scanning</li><li>Works offline and syncs automatically</li><li>Print or email digital receipts</li><li>Manage refunds and open tickets</li></ul>',
   '<p>គិតលុយ បញ្ចុះតម្លៃ និងទទួលការទូទាត់ច្រើនប្រភេទ — ទោះបីគ្មានអ៊ីនធឺណិត។ កម្មវិធី POS ដំណើរការលើទូរស័ព្ទ និងថេប្លេត Android និង iOS។</p>',
   'PRODUCTS', 'pos,product,sale', 1, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text,
   'Back Office', 'ការិយាល័យខាងក្រោយ', 'back-office',
   'Easily manage your business in a browser from any device, anywhere.',
   'គ្រប់គ្រងអាជីវកម្មរបស់អ្នកយ៉ាងងាយស្រួលក្នុងកម្មវិធីរុករក ពីគ្រប់ឧបករណ៍ គ្រប់ទីកន្លែង។',
   '<p>The Back Office is your web command center: manage inventory, employees, pricing, and view detailed reports from any browser.</p><ul><li>Real-time inventory across all stores</li><li>Employee roles and permissions</li><li>Sales, purchase and stock reports</li><li>Export data to Excel, PDF and more</li></ul>',
   '<p>ការិយាល័យខាងក្រោយ គឺជាមជ្ឈមណ្ឌលបញ្ជាតាមអ៊ីនធឺណិត៖ គ្រប់គ្រងស្តុក បុគ្គលិក តម្លៃ និងមើលរបាយការណ៍លម្អិត។</p>',
   'PRODUCTS', 'back-office,office,product', 2, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text,
   'Dashboard', 'ផ្ទាំងគ្រប់គ្រង', 'dashboard',
   'Instant access to your store''s sales analytics and inventory in your pocket.',
   'ចូលប្រើការវិភាគការលក់ និងស្តុកទំនិញរបស់ហាងអ្នកភ្លាមៗ ក្នុងហោប៉ៅរបស់អ្នក។',
   '<p>The Dashboard app puts live business analytics in your pocket — track revenue, top products, and stock levels wherever you are.</p><ul><li>Live sales and revenue charts</li><li>Best-selling items at a glance</li><li>Low-stock alerts</li><li>Multi-store comparison</li></ul>',
   '<p>កម្មវិធី Dashboard ដាក់ការវិភាគអាជីវកម្មផ្ទាល់ក្នុងហោប៉ៅរបស់អ្នក — តាមដានចំណូល ទំនិញលក់ដាច់ និងកម្រិតស្តុក។</p>',
   'PRODUCTS', 'dashboard,analytics,product', 3, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS')
ON CONFLICT DO NOTHING;
