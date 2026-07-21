-- Register ARTICLE-HERO-SECTION as an article type and seed the home page hero
INSERT INTO public.article_types (id, code, label, label_kh, sort_order)
VALUES (gen_random_uuid(), 'ARTICLE-HERO-SECTION', 'Article Hero Section', 'ផ្នែកបឋមកថា', 17)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.articles
  (id, title, title_kh, slug, excerpt, excerpt_kh, content, content_kh,
   type, tags, sort_order, view_count, workflow_status, status, published_at, created_by)
VALUES
  (gen_random_uuid()::text,
   'Free Point of Sale and Inventory Management Software',
   'កម្មវិធីគ្រប់គ្រងការលក់ និងស្តុកទំនិញ ដោយឥតគិតថ្លៃ',
   'home-hero',
   'Turn your smartphone or tablet into a powerful POS. Manage sales, inventory and employees with ease; engage customers and increase your revenue.',
   'ប្រែក្លាយស្មាតហ្វូន ឬថេប្លេតរបស់អ្នកទៅជាប្រព័ន្ធ POS ដ៏មានឥទ្ធិភាព។ គ្រប់គ្រងការលក់ ស្តុកទំនិញ និងបុគ្គលិកយ៉ាងងាយស្រួល។',
   NULL, NULL,
   'ARTICLE-HERO-SECTION', 'hero', 1, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS')
ON CONFLICT (slug) DO NOTHING;
