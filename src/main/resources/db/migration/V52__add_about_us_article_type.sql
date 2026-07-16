-- Register ABOUT-US as an article type and seed the /about page content
INSERT INTO public.article_types (id, code, label, label_kh, sort_order)
VALUES (gen_random_uuid(), 'ABOUT-US', 'About Us', 'អំពីយើង', 16)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.articles
  (id, title, title_kh, slug, excerpt, excerpt_kh, content, content_kh,
   type, tags, sort_order, view_count, workflow_status, status, published_at, created_by)
VALUES
  (gen_random_uuid()::text, 'About Us', 'អំពីយើង', 'about-us',
   'We build point-of-sale and retail solutions that help businesses across Cambodia sell, manage, and grow.',
   'យើងបង្កើតដំណោះស្រាយ POS និងលក់រាយ ដើម្បីជួយអាជីវកម្មនៅកម្ពុជា លក់ គ្រប់គ្រង និងរីកចម្រើន។',
   '<p>Our team is dedicated to building simple, reliable tools for retailers, cafés, and growing businesses — from the first sale of the day to end-of-month reports.</p>',
   '<p>ក្រុមការងាររបស់យើងប្តេជ្ញាបង្កើតឧបករណ៍សាមញ្ញ និងអាចទុកចិត្តបាន សម្រាប់អាជីវកម្មលក់រាយ ហាងកាហ្វេ និងអាជីវកម្មកំពុងរីកចម្រើន។</p>',
   'ABOUT-US', 'about', 1, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS')
ON CONFLICT (slug) DO NOTHING;
