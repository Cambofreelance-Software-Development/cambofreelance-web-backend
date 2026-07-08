-- Seed BUSINESS-type articles (power the Business types nav dropdown, /business-types landing & detail)
INSERT INTO public.articles
  (id, title, title_kh, slug, excerpt, excerpt_kh, content, content_kh,
   type, tags, sort_order, view_count, workflow_status, status, published_at, created_by)
VALUES
  (gen_random_uuid()::text, 'Restaurant', 'ភោជនីយដ្ឋាន', 'restaurant',
   'Table management, kitchen display, and fast order taking for dine-in and takeaway.',
   'គ្រប់គ្រងតុ បង្ហាញផ្ទះបាយ និងទទួលការបញ្ជាទិញរហ័ស សម្រាប់ញ៉ាំនៅកន្លែង និងយកទៅ។',
   '<p>Run your restaurant smoothly with floor plans, course firing to the kitchen display, split bills, and menu modifiers.</p><ul><li>Table & floor management</li><li>Kitchen Display System (KDS)</li><li>Split, merge and transfer bills</li><li>Menu modifiers and combos</li></ul>',
   '<p>ដំណើរការភោជនីយដ្ឋានរបស់អ្នកយ៉ាងរលូន ជាមួយប្លង់តុ បញ្ជូនទៅផ្ទះបាយ បំបែកវិក្កយបត្រ និងការកែម៉ឺនុយ។</p>',
   'BUSINESS', 'restaurant,food,business', 1, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'Café & Bar', 'ហាងកាហ្វេ និងបារ', 'cafe',
   'Quick checkout, modifiers, and open tabs built for cafés, coffee shops and bars.',
   'គិតលុយរហ័ស ការកែប្រែ និងវិក្កយបត្របើក សម្រាប់ហាងកាហ្វេ និងបារ។',
   '<p>Serve customers faster with one-tap favorites, drink modifiers, and open tabs for bars.</p><ul><li>One-tap popular items</li><li>Size and add-on modifiers</li><li>Open tabs and pre-authorization</li><li>Happy-hour pricing</li></ul>',
   '<p>បម្រើអតិថិជនលឿនជាមួយមុខម្ហូបពេញនិយម ការកែភេសជ្ជៈ និងវិក្កយបត្របើកសម្រាប់បារ។</p>',
   'BUSINESS', 'cafe,coffee,business', 2, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'Retail', 'លក់រាយ', 'retail',
   'Barcode scanning, inventory tracking, and loyalty for shops of any size.',
   'ស្កេនកូដ តាមដានស្តុក និងកម្មវិធីភក្តីភាព សម្រាប់ហាងគ្រប់ទំហំ។',
   '<p>Sell faster and keep perfect stock with barcode scanning, variants, and a built-in loyalty program.</p><ul><li>Barcode scanning and label printing</li><li>Product variants and matrices</li><li>Real-time inventory</li><li>Customer loyalty and discounts</li></ul>',
   '<p>លក់លឿន និងរក្សាស្តុកបានត្រឹមត្រូវ ជាមួយការស្កេនកូដ ប្រភេទផលិតផល និងកម្មវិធីភក្តីភាព។</p>',
   'BUSINESS', 'retail,shop,business', 3, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'Grocery', 'ហាងទំនិញ', 'grocery',
   'Fast scanning, weighted items, and stock control for grocery and mini-marts.',
   'ស្កេនរហ័ស ទំនិញថ្លឹង និងគ្រប់គ្រងស្តុក សម្រាប់ហាងទំនិញ និងមីនីម៉ាត។',
   '<p>Handle high volumes with fast scanning, scale integration for weighted items, and low-stock alerts.</p><ul><li>High-speed barcode checkout</li><li>Weighted / scale items</li><li>Low-stock and expiry alerts</li><li>Supplier purchase orders</li></ul>',
   '<p>គ្រប់គ្រងបរិមាណច្រើនជាមួយការស្កេនរហ័ស ទំនិញថ្លឹង និងការជូនដំណឹងស្តុកជិតអស់។</p>',
   'BUSINESS', 'grocery,market,business', 4, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'Salon & Spa', 'ហាងកាត់សក់ និងស្ប៉ា', 'salon',
   'Appointments, service menus, and staff commissions for beauty businesses.',
   'ការណាត់ជួប ម៉ឺនុយសេវាកម្ម និងកម្រៃបុគ្គលិក សម្រាប់អាជីវកម្មសម្ផស្ស។',
   '<p>Manage bookings, service menus, and staff performance with commissions and tips built in.</p><ul><li>Appointment booking</li><li>Service and package menus</li><li>Staff commissions and tips</li><li>Client history and preferences</li></ul>',
   '<p>គ្រប់គ្រងការកក់ ម៉ឺនុយសេវាកម្ម និងសមត្ថភាពបុគ្គលិក ជាមួយកម្រៃ និងទីប។</p>',
   'BUSINESS', 'salon,spa,business', 5, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'Service Business', 'អាជីវកម្មសេវាកម្ម', 'service',
   'Invoicing, quotes, and customer management for service providers.',
   'វិក្កយបត្រ សម្រង់តម្លៃ និងគ្រប់គ្រងអតិថិជន សម្រាប់អ្នកផ្តល់សេវាកម្ម។',
   '<p>Quote, invoice, and get paid faster while keeping every customer''s history in one place.</p><ul><li>Quotes and invoices</li><li>Service catalog</li><li>Customer management</li><li>Payments and receipts</li></ul>',
   '<p>ធ្វើសម្រង់តម្លៃ វិក្កយបត្រ និងទទួលប្រាក់លឿន ខណៈរក្សាប្រវត្តិអតិថិជនក្នុងកន្លែងតែមួយ។</p>',
   'BUSINESS', 'service,business', 6, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS')
ON CONFLICT DO NOTHING;
