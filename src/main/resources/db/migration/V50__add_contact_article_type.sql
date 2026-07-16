-- Register CONTACT as an article type so contact-page content can be managed as articles
INSERT INTO public.article_types (id, code, label, label_kh, sort_order)
VALUES (gen_random_uuid(), 'CONTACT', 'Contact', 'ទំនាក់ទំនង', 15)
ON CONFLICT (code) DO NOTHING;
