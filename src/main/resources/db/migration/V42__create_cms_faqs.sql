CREATE TABLE IF NOT EXISTS public.cms_faqs (
    id            VARCHAR(36)  PRIMARY KEY,
    question      TEXT         NOT NULL,
    question_kh   TEXT,
    answer        TEXT         NOT NULL,
    answer_kh     TEXT,
    sort_order    INTEGER      NOT NULL DEFAULT 0,
    created_by    VARCHAR(255) NOT NULL DEFAULT 'SYS',
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by    VARCHAR(255),
    updated_at    TIMESTAMP,
    status        VARCHAR(3)   NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_cms_faqs_status     ON public.cms_faqs (status);
CREATE INDEX IF NOT EXISTS idx_cms_faqs_sort_order ON public.cms_faqs (sort_order);

-- Seed defaults mirroring the strings previously hardcoded in FaqSection.tsx
INSERT INTO public.cms_faqs (id, question, question_kh, answer, answer_kh, sort_order) VALUES
    (gen_random_uuid()::text,
     'Is SOPPOS POS really free?',
     'តើ SOPPOS POS ឥតគិតថ្លៃពិតមែនទេ?',
     'Yes. The SOPPOS POS app is free to download and use for one register — no hidden fees, no credit card required. Optional paid add-ons (multi-store, advanced analytics, employee management) are available whenever you''re ready to scale.',
     'ត្រូវហើយ។ កម្មវិធី SOPPOS POS ឥតគិតថ្លៃក្នុងការទាញយក និងប្រើសម្រាប់ Register មួយ — គ្មានថ្លៃលាក់លៀម និងមិនត្រូវការកាតឥណទាន។ មានកញ្ចប់ជម្រើសបន្ថែម (ហាងច្រើន, វិភាគកម្រិតខ្ពស់, គ្រប់គ្រងបុគ្គលិក) នៅពេលអ្នករួចខ្លួនពង្រីក។',
     1),
    (gen_random_uuid()::text,
     'How to use SOPPOS POS system?',
     'តើប្រើប្រព័ន្ធ SOPPOS POS យ៉ាងម៉េច?',
     'Install the app on your smartphone or tablet, create your account, add your product catalog and prices, then start selling. Optional receipt printers, barcode scanners, and cash drawers can be connected via Bluetooth, USB, or Wi-Fi.',
     'ដំឡើងកម្មវិធីនៅលើស្មាតហ្វូន ឬថេប្លេតរបស់អ្នក បង្កើតគណនី បញ្ចូលកាតាឡុកផលិតផល និងតម្លៃ រួចហើយចាប់ផ្តើមលក់។ ម៉ាស៊ីនព្រីនវិក្កយបត្រ ស្កេនឡើកកូដ និងថតដាក់លុយអាចភ្ជាប់តាម Bluetooth, USB ឬ Wi-Fi ។',
     2),
    (gen_random_uuid()::text,
     'Does SOPPOS POS work offline?',
     'តើ SOPPOS POS ដំណើរការពេលអុហ្វឡាញទេ?',
     'Yes. Sales, refunds, and receipts continue to work without an internet connection. When your device reconnects, all transactions sync back to the cloud automatically.',
     'ត្រូវហើយ។ ការលក់ ការសង និងវិក្កយបត្រនៅតែដំណើរការទោះបីជាគ្មានអ៊ីនធឺណិត។ ពេលឧបករណ៍ភ្ជាប់អ៊ីនធឺណិតវិញ ប្រតិបត្តិការទាំងអស់នឹងធ្វើសមកាលកម្មទៅ Cloud ដោយស្វ័យប្រវត្តិ។',
     3),
    (gen_random_uuid()::text,
     'What kind of hardware works with SOPPOS?',
     'តើហាតវែរប្រភេទណាដំណើរការជាមួយ SOPPOS?',
     'SOPPOS is compatible with a wide range of POS printers, barcode scanners, cash drawers, and card readers — including Star, Epson, Zebra, and SumUp. See the full compatible hardware list on the Hardware page.',
     'SOPPOS ឆបគ្នាជាមួយម៉ាស៊ីនព្រីន POS, ស្កេនឡើកកូដ, ថតដាក់លុយ, និងម៉ាស៊ីនអានកាតច្រើនប្រភេទ — រួមមាន Star, Epson, Zebra, និង SumUp ។ សូមមើលបញ្ជីហាតវែរឆបគ្នាពេញលេញនៅទំព័រ Hardware ។',
     4);

-- Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'faqs.view',   'View FAQs',   'CONTENT_MANAGEMENT', 84),
  (gen_random_uuid()::text, 'faqs.create', 'Create FAQs', 'CONTENT_MANAGEMENT', 85),
  (gen_random_uuid()::text, 'faqs.update', 'Update FAQs', 'CONTENT_MANAGEMENT', 86),
  (gen_random_uuid()::text, 'faqs.delete', 'Delete FAQs', 'CONTENT_MANAGEMENT', 87)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('faqs.view', 'faqs.create', 'faqs.update', 'faqs.delete')
ON CONFLICT DO NOTHING;
