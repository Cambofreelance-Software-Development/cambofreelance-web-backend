-- Extend hardware for the Loyverse import.
-- image_url stores an externally-hosted product image (media_files is for uploads
-- managed by the CMS; scraped vendor imagery lives outside that flow).
-- The four new categories map to Loyverse's section headings that V25 did not seed.

ALTER TABLE public.hardware
    ADD COLUMN IF NOT EXISTS image_url VARCHAR(500);

INSERT INTO public.category_hardware (id, name, name_kh, sort_order) VALUES
    (gen_random_uuid()::text, 'Mobile Printers',       'ម៉ាស៊ីនព្រីនចល័ត',        5),
    (gen_random_uuid()::text, 'Label Printers',        'ម៉ាស៊ីនព្រីនស្លាកសម្គាល់', 6),
    (gen_random_uuid()::text, 'Android POS Terminals', 'ស្ថានីយ POS Android',     7),
    (gen_random_uuid()::text, 'Tablet Stands',         'ជើងទ្រថេប្លេត',           8)
ON CONFLICT (name) DO NOTHING;
