CREATE TABLE IF NOT EXISTS public.pricing_faq (
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

CREATE INDEX IF NOT EXISTS idx_pricing_faq_status     ON public.pricing_faq (status);
CREATE INDEX IF NOT EXISTS idx_pricing_faq_sort_order ON public.pricing_faq (sort_order);

-- Seed defaults for the pricing page's "Frequently asked questions" section
INSERT INTO public.pricing_faq (id, question, question_kh, answer, answer_kh, sort_order) VALUES
    (gen_random_uuid()::text,
     'How do I subscribe to a paid plan?',
     'តើខ្ញុំចុះឈ្មោះគម្រោងបង់ប្រាក់យ៉ាងដូចម្តេច?',
     'Open the Subscription page from your dashboard, pick the plan that fits your business, and choose monthly or yearly billing. Your new plan activates immediately after payment is confirmed.',
     'បើកទំព័រ Subscription ពីផ្ទាំងគ្រប់គ្រងរបស់អ្នក ជ្រើសរើសគម្រោងដែលសាកសមនឹងអាជីវកម្មរបស់អ្នក រួចជ្រើសរើសបង់ប្រាក់ប្រចាំខែ ឬប្រចាំឆ្នាំ។ គម្រោងថ្មីរបស់អ្នកនឹងចាប់ផ្តើមដំណើរការភ្លាមៗបន្ទាប់ពីការទូទាត់ត្រូវបានបញ្ជាក់។',
     1),
    (gen_random_uuid()::text,
     'How long is the free trial and how does it work?',
     'តើការសាកល្បងឥតគិតថ្លៃមានរយៈពេលប៉ុន្មាន និងដំណើរការយ៉ាងដូចម្តេច?',
     'Every new account gets a 14-day free trial with full access to your chosen plan''s features — no credit card required to start. You can upgrade, downgrade, or cancel anytime before the trial ends.',
     'គណនីថ្មីនីមួយៗទទួលបានការសាកល្បងឥតគិតថ្លៃរយៈពេល ១៤ថ្ងៃ ជាមួយសិទ្ធិចូលប្រើពេញលេញចំពោះមុខងារនៃគម្រោងដែលបានជ្រើសរើស — មិនត្រូវការកាតឥណទានដើម្បីចាប់ផ្តើមទេ។ អ្នកអាចដំឡើង ដាក់ចុះ ឬលុបចោលបានគ្រប់ពេលមុនពេលការសាកល្បងបញ្ចប់។',
     2),
    (gen_random_uuid()::text,
     'What payment methods are accepted?',
     'តើមានវិធីទូទាត់ប្រាក់អ្វីខ្លះដែលទទួលយក?',
     'We accept major credit and debit cards, plus local payment options such as ABA PAY. All payments are processed securely and your card details are never stored on our servers.',
     'យើងទទួលយកកាតឥណទាន និងកាតឥណពន្ធធំៗ ព្រមទាំងជម្រើសទូទាត់ក្នុងស្រុកដូចជា ABA PAY ។ ការទូទាត់ទាំងអស់ត្រូវបានដំណើរការដោយសុវត្ថិភាព ហើយព័ត៌មានកាតរបស់អ្នកមិនត្រូវបានរក្សាទុកនៅលើម៉ាស៊ីនមេរបស់យើងឡើយ។',
     3),
    (gen_random_uuid()::text,
     'Can I cancel my subscription anytime?',
     'តើខ្ញុំអាចលុបចោលការជាវបានគ្រប់ពេលឬទេ?',
     'Yes. You can cancel from the Subscription page at any time — there''s no lock-in contract. Your plan stays active until the end of the current billing period, and you won''t be charged again after that.',
     'បាទ/ចាស។ អ្នកអាចលុបចោលពីទំព័រ Subscription បានគ្រប់ពេល — គ្មានកិច្ចសន្យាចងសង្កត់ទេ។ គម្រោងរបស់អ្នកនៅតែសកម្មរហូតដល់ចុងបញ្ចប់នៃវដ្តទូទាត់បច្ចុប្បន្ន ហើយអ្នកនឹងមិនត្រូវបានគិតលុយម្តងទៀតឡើយបន្ទាប់ពីនោះ។',
     4),
    (gen_random_uuid()::text,
     'What happens to my data if my account is suspended?',
     'តើទិន្នន័យរបស់ខ្ញុំនឹងទៅជាយ៉ាងណា ប្រសិនបើគណនីរបស់ខ្ញុំត្រូវបានផ្អាក?',
     'Your sales and business data is kept safe for 90 days after suspension, giving you time to reactivate your subscription and pick up right where you left off. After that period, inactive data may be permanently removed.',
     'ទិន្នន័យលក់ និងអាជីវកម្មរបស់អ្នកត្រូវបានរក្សាទុកឱ្យមានសុវត្ថិភាពរយៈពេល ៩០ថ្ងៃ បន្ទាប់ពីការផ្អាក ដើម្បីឱ្យអ្នកមានពេលវេលាបើកដំណើរការការជាវឡើងវិញ និងបន្តការងារពីកន្លែងដែលបានឈប់។ បន្ទាប់ពីរយៈពេលនោះ ទិន្នន័យដែលមិនសកម្មអាចនឹងត្រូវលុបចោលជាអចិន្ត្រៃយ៍។',
     5)
ON CONFLICT (id) DO NOTHING;
