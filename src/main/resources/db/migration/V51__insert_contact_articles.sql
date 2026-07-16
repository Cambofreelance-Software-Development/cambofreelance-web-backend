-- Seed CONTACT-type articles (Address / Phone / Email shown on /contact)
INSERT INTO public.articles
  (id, title, title_kh, slug, excerpt, excerpt_kh, content, content_kh,
   type, tags, sort_order, view_count, workflow_status, status, published_at, created_by)
VALUES
  (gen_random_uuid()::text, 'Address', 'អាសយដ្ឋាន', 'contact-address',
   NULL, NULL,
   '<p>Street 123, BKK1, Phnom Penh, Cambodia</p>',
   '<p>ផ្លូវ ១២៣, បឹងកេងកង១, ភ្នំពេញ, កម្ពុជា</p>',
   'CONTACT', 'contact', 1, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'Phone', 'ទូរស័ព្ទ', 'contact-phone',
   NULL, NULL,
   '<p><a href="tel:+855012345678">+855 (0) 12 345 678</a></p>',
   '<p><a href="tel:+855012345678">+855 (0) 12 345 678</a></p>',
   'CONTACT', 'contact', 2, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS'),

  (gen_random_uuid()::text, 'Email', 'អ៊ីមែល', 'contact-email',
   NULL, NULL,
   '<p><a href="mailto:hello@soppossytem.com">hello@soppossytem.com</a></p>',
   '<p><a href="mailto:hello@soppossytem.com">hello@soppossytem.com</a></p>',
   'CONTACT', 'contact', 3, 0, 'PUBLISHED', 'ACT', CURRENT_TIMESTAMP, 'SYS')
ON CONFLICT (slug) DO NOTHING;
