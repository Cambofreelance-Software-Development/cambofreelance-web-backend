CREATE TABLE IF NOT EXISTS public.cms_feature_tabs (
    id            VARCHAR(36)  PRIMARY KEY,
    tab_label     VARCHAR(255) NOT NULL,
    tab_label_kh  VARCHAR(255),
    title         VARCHAR(255) NOT NULL,
    title_kh      VARCHAR(255),
    subtitle      TEXT,
    subtitle_kh   TEXT,
    cta_label     VARCHAR(255),
    cta_label_kh  VARCHAR(255),
    cta_href      VARCHAR(500),
    cta_button    BOOLEAN      NOT NULL DEFAULT FALSE,
    image_url     VARCHAR(500),
    image_side    VARCHAR(10)  NOT NULL DEFAULT 'right',
    sort_order    INTEGER      NOT NULL DEFAULT 0,
    created_by    VARCHAR(255) NOT NULL DEFAULT 'SYS',
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by    VARCHAR(255),
    updated_at    TIMESTAMP,
    status        VARCHAR(3)   NOT NULL DEFAULT 'ACT'
);

CREATE TABLE IF NOT EXISTS public.cms_feature_tab_bullets (
    id            VARCHAR(36)  PRIMARY KEY,
    tab_id        VARCHAR(36)  NOT NULL REFERENCES public.cms_feature_tabs(id) ON DELETE CASCADE,
    icon          VARCHAR(20)  NOT NULL DEFAULT 'check',
    label         VARCHAR(500) NOT NULL,
    label_kh      VARCHAR(500),
    sub_label     TEXT,
    sub_label_kh  TEXT,
    sort_order    INTEGER      NOT NULL DEFAULT 0,
    created_by    VARCHAR(255) NOT NULL DEFAULT 'SYS',
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by    VARCHAR(255),
    updated_at    TIMESTAMP,
    status        VARCHAR(3)   NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_feature_tabs_status     ON public.cms_feature_tabs (status);
CREATE INDEX IF NOT EXISTS idx_feature_tabs_sort_order ON public.cms_feature_tabs (sort_order);
CREATE INDEX IF NOT EXISTS idx_feature_tab_bullets_tab_id     ON public.cms_feature_tab_bullets (tab_id);
CREATE INDEX IF NOT EXISTS idx_feature_tab_bullets_sort_order ON public.cms_feature_tab_bullets (sort_order);

-- Seed defaults mirroring the 8 tabs previously hardcoded in FeaturesTabsSection.tsx.
DO $$
DECLARE
    pos_id          VARCHAR(36) := gen_random_uuid()::text;
    payments_id     VARCHAR(36) := gen_random_uuid()::text;
    inventory_id    VARCHAR(36) := gen_random_uuid()::text;
    analytics_id    VARCHAR(36) := gen_random_uuid()::text;
    employee_id     VARCHAR(36) := gen_random_uuid()::text;
    crm_id          VARCHAR(36) := gen_random_uuid()::text;
    multistore_id   VARCHAR(36) := gen_random_uuid()::text;
    integrations_id VARCHAR(36) := gen_random_uuid()::text;
BEGIN
    INSERT INTO public.cms_feature_tabs (id, tab_label, tab_label_kh, title, title_kh, subtitle, subtitle_kh,
                                          cta_label, cta_label_kh, cta_href, cta_button, image_url, image_side, sort_order) VALUES
        (pos_id, 'Point of sale', 'ចំណុចលក់', 'Point of sale', 'ចំណុចលក់',
         'Transform your smartphone or tablet into an easy-to-use point of sale.',
         'ប្រែក្លាយស្មាតហ្វូន ឬថេប្លេតរបស់អ្នកទៅជាចំណុចលក់ដ៏ងាយស្រួលប្រើ។',
         'Explore SOPPOS Point of Sale', 'រុករក SOPPOS Point of Sale', '/features', FALSE,
         '/images/home/tab-point-of-sale.png', 'right', 1),

        (payments_id, 'Payments', 'ការទូទាត់', 'POS payment solutions', 'ដំណោះស្រាយការទូទាត់ POS',
         'Seamlessly accept any method of payment your customers want with SOPPOS integrated solutions.',
         'ទទួលយកគ្រប់វិធីនៃការទូទាត់ដែលអតិថិជនរបស់អ្នកចង់បាន ជាមួយដំណោះស្រាយរួមបញ្ចូល SOPPOS ។',
         'Explore integrated payment providers', 'រុករកអ្នកផ្ដល់សេវាទូទាត់រួមបញ្ចូល', '/features', TRUE,
         '/images/home/tab-payments.png', 'left', 2),

        (inventory_id, 'Inventory management', 'គ្រប់គ្រងស្តុក', 'Inventory management', 'គ្រប់គ្រងស្តុក',
         'Never run out of stock with real-time tracking and automated alerts.',
         'កុំឲ្យអស់ស្តុក ជាមួយការតាមដានពេលវេលាពិត និងការជូនដំណឹងស្វ័យប្រវត្តិ។',
         'Explore SOPPOS Point of Sale', 'រុករក SOPPOS Point of Sale', '/features', FALSE,
         '/images/home/tab-inventory.png', 'right', 3),

        (analytics_id, 'Sales analytics', 'វិភាគការលក់', 'Sales analytics', 'វិភាគការលក់',
         'Access your reports from a smartphone, tablet or computer anytime, anywhere.',
         'ចូលមើលរបាយការណ៍របស់អ្នកពីស្មាតហ្វូន ថេប្លេត ឬកុំព្យូទ័រនៅគ្រប់ពេល គ្រប់ទីកន្លែង។',
         'Explore SOPPOS Back Office', 'រុករក SOPPOS Back Office', '/features', FALSE,
         '/images/home/tab-sales-analytics.png', 'left', 4),

        (employee_id, 'Employee management', 'គ្រប់គ្រងបុគ្គលិក', 'Employee management', 'គ្រប់គ្រងបុគ្គលិក',
         'Easily manage your staff and make balanced decisions',
         'គ្រប់គ្រងបុគ្គលិករបស់អ្នកយ៉ាងងាយស្រួល និងធ្វើការសម្រេចចិត្តបានយ៉ាងសមស្រប',
         'Explore employee management', 'រុករកការគ្រប់គ្រងបុគ្គលិក', '/features', FALSE,
         '/images/home/tab-employee.png', 'right', 5),

        (crm_id, 'CRM and customer loyalty', 'CRM និងភាពស្មោះត្រង់អតិថិជន',
         'CRM and customer loyalty', 'CRM និងភាពស្មោះត្រង់អតិថិជន',
         'Turn once-in-a-while shoppers into regulars',
         'ប្រែក្លាយអតិថិជនម្តងម្កាលឲ្យទៅជាអតិថិជនប្រចាំ',
         'Explore loyalty program', 'រុករកកម្មវិធីភាពស្មោះត្រង់', '/features', FALSE,
         '/images/home/tab-crm.png', 'left', 6),

        (multistore_id, 'Multi-store management', 'គ្រប់គ្រងហាងច្រើន',
         'Multi-store management', 'គ្រប់គ្រងហាងច្រើន',
         'Grow your business from one to hundreds of stores',
         'ពង្រីកអាជីវកម្មរបស់អ្នកពីមួយទៅរាប់រយហាង',
         'Explore multi-store management', 'រុករកការគ្រប់គ្រងហាងច្រើន', '/features', FALSE,
         '/images/home/tab-multistore.png', 'right', 7),

        (integrations_id, 'Integrations', 'ការរួមបញ្ចូល', 'Integration with SOPPOS POS', 'ការរួមបញ្ចូលជាមួយ SOPPOS POS',
         'Connect third-party apps to SOPPOS to keep your business running smoothly.',
         'ភ្ជាប់កម្មវិធីភាគីទីបីទៅ SOPPOS ដើម្បីរក្សាអាជីវកម្មរបស់អ្នកដំណើរការបានរលូន។',
         'Visit App Marketplace', 'ចូលមើល App Marketplace', '/features', TRUE,
         '/images/home/tab-integrations.png', 'right', 8);

    INSERT INTO public.cms_feature_tab_bullets (id, tab_id, icon, label, label_kh, sub_label, sub_label_kh, sort_order) VALUES
        -- POS
        (gen_random_uuid()::text, pos_id, 'check', 'Issue printed or electronic receipts', 'ចេញវិក្កយបត្របោះពុម្ព ឬអេឡិចត្រូនិក', NULL, NULL, 1),
        (gen_random_uuid()::text, pos_id, 'check', 'Apply discounts and issue refunds', 'អនុវត្តការបញ្ចុះតម្លៃ និងសងត្រឡប់វិញ', NULL, NULL, 2),
        (gen_random_uuid()::text, pos_id, 'check', 'Keep recording sales even when offline', 'បន្តកត់ត្រាការលក់សូម្បីតែពេលអុហ្វឡាញ', NULL, NULL, 3),
        (gen_random_uuid()::text, pos_id, 'check', 'Connect a receipt printer and cash drawer', 'ភ្ជាប់ម៉ាស៊ីនព្រីនវិក្កយបត្រ និងថតដាក់លុយ', NULL, NULL, 4),
        -- Payments
        (gen_random_uuid()::text, payments_id, 'check', 'Integrated payments', 'ការទូទាត់រួមបញ្ចូល',
         'Direct integration with your POS for faster, error-free checkouts.',
         'ការរួមបញ្ចូលដោយផ្ទាល់ជាមួយ POS របស់អ្នក ដើម្បីគិតលុយលឿន និងគ្មានកំហុស។', 1),
        (gen_random_uuid()::text, payments_id, 'check', 'Local & Global support', 'ការគាំទ្រក្នុងស្រុក និងសកល',
         'Full support for KHQR, MasterCard, VISA, and other major providers.',
         'ការគាំទ្រពេញលេញសម្រាប់ KHQR, MasterCard, VISA និងអ្នកផ្ដល់សំខាន់ៗផ្សេងទៀត។', 2),
        (gen_random_uuid()::text, payments_id, 'check', 'Improved accuracy', 'ភាពត្រឹមត្រូវប្រសើរឡើង',
         'Eliminate manual entry errors and simplify your daily reconciliation.',
         'លុបបំបាត់កំហុសបញ្ចូលដោយដៃ និងធ្វើឲ្យការផ្ទៀងផ្ទាត់ប្រចាំថ្ងៃងាយស្រួល។', 3),
        -- Inventory
        (gen_random_uuid()::text, inventory_id, 'bar', 'Track stock levels', 'តាមដានកម្រិតស្តុក',
         'Real-time monitoring of your entire inventory across all stores.',
         'តាមដានពេលវេលាពិតនៃស្តុកទាំងអស់នៅគ្រប់ហាង។', 1),
        (gen_random_uuid()::text, inventory_id, 'bell', 'Low stock alerts', 'ការជូនដំណឹងស្តុកទាប',
         'Receive automatic notifications before items run out.',
         'ទទួលការជូនដំណឹងស្វ័យប្រវត្តិមុនពេលទំនិញអស់។', 2),
        (gen_random_uuid()::text, inventory_id, 'scan', 'Print barcode labels', 'បោះពុម្ពស្លាកបាកូដ',
         'Easily create and print professional labels for your products.',
         'បង្កើត និងបោះពុម្ពស្លាកសម្គាល់វិជ្ជាជីវៈសម្រាប់ផលិតផលរបស់អ្នកយ៉ាងងាយស្រួល។', 3),
        -- Sales Analytics
        (gen_random_uuid()::text, analytics_id, 'check', 'View revenue, average sale and profit', 'មើលចំណូល ការលក់ជាមធ្យម និងប្រាក់ចំណេញ', NULL, NULL, 1),
        (gen_random_uuid()::text, analytics_id, 'check', 'Track sales trends and react to changes promptly', 'តាមដាននិន្នាការលក់ និងឆ្លើយតបទៅនឹងការផ្លាស់ប្តូរឆាប់រហ័ស', NULL, NULL, 2),
        (gen_random_uuid()::text, analytics_id, 'check', 'Determine best-selling items and categories', 'កំណត់ទំនិញ និងប្រភេទដែលលក់ដាច់បំផុត', NULL, NULL, 3),
        (gen_random_uuid()::text, analytics_id, 'check', 'View complete sales history', 'មើលប្រវត្តិលក់ពេញលេញ', NULL, NULL, 4),
        (gen_random_uuid()::text, analytics_id, 'check', 'Export sales data to spreadsheets', 'នាំចេញទិន្នន័យលក់ទៅ Spreadsheet', NULL, NULL, 5),
        -- Employee
        (gen_random_uuid()::text, employee_id, 'check', 'Track sales by employee and determine best performers', 'តាមដានការលក់តាមបុគ្គលិក និងកំណត់អ្នកបំពេញការងារល្អបំផុត', NULL, NULL, 1),
        (gen_random_uuid()::text, employee_id, 'check', 'Employees clock in and out and their total work hours are calculated automatically', 'បុគ្គលិកចុះចូល/ចេញ ហើយម៉ោងធ្វើការសរុបត្រូវបានគណនាដោយស្វ័យប្រវត្តិ', NULL, NULL, 2),
        (gen_random_uuid()::text, employee_id, 'check', 'Grant different levels of access to protect sensitive information', 'ផ្តល់កម្រិតការចូលប្រើផ្សេងៗ ដើម្បីការពារព័ត៌មានរសើប', NULL, NULL, 3),
        -- CRM
        (gen_random_uuid()::text, crm_id, 'check', 'Build your customer base', 'បង្កើតមូលដ្ឋានអតិថិជនរបស់អ្នក', NULL, NULL, 1),
        (gen_random_uuid()::text, crm_id, 'check', 'View customer purchase history to provide personalized service', 'មើលប្រវត្តិទិញអតិថិជន ដើម្បីផ្តល់សេវាកម្មផ្ទាល់ខ្លួន', NULL, NULL, 2),
        (gen_random_uuid()::text, crm_id, 'check', 'Run a loyalty program to reward customers for their recurring purchases', 'ដំណើរការកម្មវិធីភាពស្មោះត្រង់ ដើម្បីផ្តល់រង្វាន់ដល់អតិថិជនសម្រាប់ការទិញម្ដងហើយម្ដងទៀត', NULL, NULL, 3),
        (gen_random_uuid()::text, crm_id, 'check', 'Keep notes on valuable customer preferences', 'រក្សាកំណត់ចំណាំអំពីចំណូលចិត្តអតិថិជនដ៏មានតម្លៃ', NULL, NULL, 4),
        -- Multi-store
        (gen_random_uuid()::text, multistore_id, 'check', 'Compare performance of your stores', 'ប្រៀបធៀបប្រតិបត្តិការហាងរបស់អ្នក', NULL, NULL, 1),
        (gen_random_uuid()::text, multistore_id, 'check', 'Manage items, employees and customers across multiple locations with a single account', 'គ្រប់គ្រងទំនិញ បុគ្គលិក និងអតិថិជននៅទីតាំងច្រើន ជាមួយគណនីតែមួយ', NULL, NULL, 2),
        -- Integrations
        (gen_random_uuid()::text, integrations_id, 'check', 'Sync with accounting, ecommerce, inventory management, marketing and other apps', 'ធ្វើសមកាលកម្មជាមួយកម្មវិធីគណនេយ្យ ពាណិជ្ជកម្មអេឡិចត្រូនិក គ្រប់គ្រងស្តុក ទីផ្សារ និងផ្សេងទៀត', NULL, NULL, 1),
        (gen_random_uuid()::text, integrations_id, 'check', 'Use SOPPOS API to develop custom integrations', 'ប្រើ SOPPOS API ដើម្បីអភិវឌ្ឍការរួមបញ្ចូលផ្ទាល់ខ្លួន', NULL, NULL, 2);
END $$;

-- Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'feature_tabs.view',   'View Feature Tabs',   'CONTENT_MANAGEMENT', 96),
  (gen_random_uuid()::text, 'feature_tabs.create', 'Create Feature Tabs', 'CONTENT_MANAGEMENT', 97),
  (gen_random_uuid()::text, 'feature_tabs.update', 'Update Feature Tabs', 'CONTENT_MANAGEMENT', 98),
  (gen_random_uuid()::text, 'feature_tabs.delete', 'Delete Feature Tabs', 'CONTENT_MANAGEMENT', 99)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('feature_tabs.view', 'feature_tabs.create', 'feature_tabs.update', 'feature_tabs.delete')
ON CONFLICT DO NOTHING;
