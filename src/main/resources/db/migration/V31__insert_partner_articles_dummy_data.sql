-- Seed PARTNER-type articles (partner stores shown on /partner)
INSERT INTO public.articles
  (id, title, title_kh, slug, excerpt, excerpt_kh, content, content_kh,
   type, tags, sort_order, view_count, workflow_status, status, published_at, created_by)
VALUES
  (gen_random_uuid()::text, 'TechMart Electronics', 'ថេកម៉ាត អេឡិចត្រូនិច', 'partner-techmart',
   'A leading electronics retailer running its stores on our POS across 5 locations.',
   'ហាងលក់អេឡិចត្រូនិចឈានមុខគេ ដែលដំណើរការហាងជាមួយ POS របស់យើងនៅ ៥ទីតាំង។',
   '<p>TechMart uses our POS and inventory system to manage thousands of SKUs across five stores with real-time stock sync.</p>',
   '<p>ថេកម៉ាត ប្រើប្រព័ន្ធ POS និងស្តុករបស់យើង ដើម្បីគ្រប់គ្រង SKU រាប់ពាន់នៅ ៥ហាង។</p>',
   'PARTNER', 'partner,retail', 1, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'Coffee Corner', 'ខាហ្វេ ខនើ', 'partner-coffee-corner',
   'A cozy café chain using our POS and loyalty program to delight regulars.',
   'ខ្សែសង្វាក់ហាងកាហ្វេ ដែលប្រើ POS និងកម្មវិធីភក្តីភាពរបស់យើង។',
   '<p>Coffee Corner speeds up service with one-tap favorites and keeps customers coming back with built-in loyalty.</p>',
   '<p>ខាហ្វេ ខនើ បង្កើនល្បឿនសេវាកម្ម និងរក្សាអតិថិជនឱ្យត្រឡប់មកវិញ។</p>',
   'PARTNER', 'partner,cafe', 2, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'Green Grocer', 'គ្រីន គ្រូសើរ', 'partner-green-grocer',
   'A neighborhood grocery relying on fast scanning and stock control every day.',
   'ហាងទំនិញក្នុងសង្កាត់ ដែលពឹងផ្អែកលើការស្កេនរហ័ស និងគ្រប់គ្រងស្តុក។',
   '<p>Green Grocer handles high daily volume with weighted items and low-stock alerts built into the POS.</p>',
   '<p>គ្រីន គ្រូសើរ គ្រប់គ្រងបរិមាណច្រើនប្រចាំថ្ងៃ ជាមួយទំនិញថ្លឹង និងការជូនដំណឹងស្តុក។</p>',
   'PARTNER', 'partner,grocery', 3, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'Style Studio', 'ស្តាល ស្ទូឌីយោ', 'partner-style-studio',
   'A salon & spa managing appointments and staff commissions with our platform.',
   'ហាងកាត់សក់ និងស្ប៉ា គ្រប់គ្រងការណាត់ជួប និងកម្រៃបុគ្គលិកជាមួយវេទិការបស់យើង។',
   '<p>Style Studio books appointments, tracks staff commissions, and keeps client history all in one place.</p>',
   '<p>ស្តាល ស្ទូឌីយោ កក់ការណាត់ជួប តាមដានកម្រៃបុគ្គលិក និងរក្សាប្រវត្តិអតិថិជន។</p>',
   'PARTNER', 'partner,salon', 4, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'The Book Haven', 'ដឹ ប៊ុក ហេវិន', 'partner-book-haven',
   'An independent bookstore using our POS to manage a large, varied catalog.',
   'ហាងលក់សៀវភៅឯករាជ្យ ដែលប្រើ POS របស់យើងគ្រប់គ្រងផលិតផលច្រើនប្រភេទ។',
   '<p>The Book Haven tracks thousands of titles with barcode scanning and easy reordering.</p>',
   '<p>ដឹ ប៊ុក ហេវិន តាមដានចំណងជើងរាប់ពាន់ ជាមួយការស្កេនកូដ។</p>',
   'PARTNER', 'partner,retail', 5, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'Fresh Bites Restaurant', 'ហ្វ្រេស បៃ រ៉េស្តូរ៉ង់', 'partner-fresh-bites',
   'A busy restaurant running table service and a kitchen display with our system.',
   'ភោជនីយដ្ឋានមមាញឹក ដែលដំណើរការសេវាតុ និងបង្ហាញផ្ទះបាយជាមួយប្រព័ន្ធរបស់យើង។',
   '<p>Fresh Bites fires orders straight to the kitchen display and turns tables faster with split billing.</p>',
   '<p>ហ្វ្រេស បៃ បញ្ជូនការបញ្ជាទិញទៅផ្ទះបាយ និងបង្វិលតុលឿនជាមួយការបំបែកវិក្កយបត្រ។</p>',
   'PARTNER', 'partner,restaurant', 6, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS')
ON CONFLICT DO NOTHING;
