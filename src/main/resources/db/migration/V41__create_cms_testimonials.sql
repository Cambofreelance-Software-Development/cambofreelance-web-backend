CREATE TABLE IF NOT EXISTS public.cms_testimonials (
    id            VARCHAR(36)  PRIMARY KEY,
    quote         TEXT         NOT NULL,
    quote_kh      TEXT,
    author_name   VARCHAR(255) NOT NULL,
    author_name_kh VARCHAR(255),
    location      VARCHAR(255),
    location_kh   VARCHAR(255),
    avatar_url    VARCHAR(500),
    sort_order    INTEGER      NOT NULL DEFAULT 0,
    created_by    VARCHAR(255) NOT NULL DEFAULT 'SYS',
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by    VARCHAR(255),
    updated_at    TIMESTAMP,
    status        VARCHAR(3)   NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_cms_testimonials_status     ON public.cms_testimonials (status);
CREATE INDEX IF NOT EXISTS idx_cms_testimonials_sort_order ON public.cms_testimonials (sort_order);

-- Seed defaults mirroring the strings previously hardcoded in TestimonialsSection.tsx
INSERT INTO public.cms_testimonials (id, quote, quote_kh, author_name, author_name_kh, location, location_kh, avatar_url, sort_order) VALUES
    (gen_random_uuid()::text,
     '"SOPPOS really has transformed the way we look at taking, tracking, and managing transactions."',
     '"SOPPOS ពិតជាបានផ្លាស់ប្តូររបៀបដែលយើងទទួល តាមដាន និងគ្រប់គ្រងប្រតិបត្តិការ។"',
     'The Redshank Catering', 'The Redshank Catering',
     'Scotland', 'ស្កុតលេន',
     '/images/home/testimonial-1.jpg', 1),
    (gen_random_uuid()::text,
     '"SOPPOS system is easy to program and to use, besides it is so easy to add pictures to aid staff as well."',
     '"ប្រព័ន្ធ SOPPOS ងាយស្រួលកម្មវិធីនិងប្រើប្រាស់ ហើយងាយស្រួលក្នុងការបន្ថែមរូបភាពដើម្បីជួយបុគ្គលិកផងដែរ។"',
     'Just1swap', 'Just1swap',
     'UK', 'ចក្រភពអង់គ្លេស',
     '/images/home/testimonial-2.jpg', 2),
    (gen_random_uuid()::text,
     '"SOPPOS has helped us manage our business easily regardless of distance."',
     '"SOPPOS បានជួយយើងគ្រប់គ្រងអាជីវកម្មរបស់យើងយ៉ាងងាយស្រួល ដោយមិនគិតពីចម្ងាយ។"',
     'Perfumeria Benie', 'Perfumeria Benie',
     'Colombia', 'កូឡុំប៊ី',
     '/images/home/testimonial-3.jpg', 3);

-- Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'testimonials.view',   'View Testimonials',   'CONTENT_MANAGEMENT', 80),
  (gen_random_uuid()::text, 'testimonials.create', 'Create Testimonials', 'CONTENT_MANAGEMENT', 81),
  (gen_random_uuid()::text, 'testimonials.update', 'Update Testimonials', 'CONTENT_MANAGEMENT', 82),
  (gen_random_uuid()::text, 'testimonials.delete', 'Delete Testimonials', 'CONTENT_MANAGEMENT', 83)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('testimonials.view', 'testimonials.create', 'testimonials.update', 'testimonials.delete')
ON CONFLICT DO NOTHING;
