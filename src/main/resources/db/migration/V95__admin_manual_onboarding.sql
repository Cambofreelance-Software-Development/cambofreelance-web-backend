-- ═══ Admin manual onboarding of existing clients & partners ═══════════════
-- Clients who already use SOP POS / paid offline, and offline resellers, are
-- onboarded directly from the admin portal — no self-signup, KHQR checkout or
-- partner application. No payment_transaction is recorded for imported
-- subscriptions, so commission only accrues on future platform payments.

-- How a subscription row came to exist: CHECKOUT (self-serve) / ADMIN_IMPORT (admin grant).
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS source        VARCHAR(20)  NOT NULL DEFAULT 'CHECKOUT';
-- PLATFORM = the POS tenant is provisioned/updated by this platform's sync;
-- MANUAL   = tenant managed outside the platform, never auto-synced;
-- NULL     = not provisioned yet (the next sync will POST a new tenant).
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS pos_link_mode VARCHAR(16);
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS import_note   VARCHAR(500);

UPDATE public.user_subscription
   SET pos_link_mode = 'PLATFORM'
 WHERE pos_link_mode IS NULL
   AND pos_registration_id IS NOT NULL;

UPDATE public.user_subscription
   SET pos_link_mode = 'MANUAL'
 WHERE pos_link_mode IS NULL
   AND pos_registration_id IS NULL
   AND (pos_client_code IS NOT NULL OR pos_backend_url IS NOT NULL);

CREATE INDEX IF NOT EXISTS idx_user_subscription_pos_registration
    ON public.user_subscription (pos_registration_id);

-- APPLICATION (self-applied) / ADMIN (created directly by an admin).
ALTER TABLE public.partner_applications ADD COLUMN IF NOT EXISTS source VARCHAR(20) NOT NULL DEFAULT 'APPLICATION';

-- ═══ Permissions ══════════════════════════════════════════════════════════
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'client.import',  'Import Existing Client',   'CLIENT_MANAGEMENT',  63),
  (gen_random_uuid()::text, 'partner.create', 'Create Partner Directly',  'PARTNER_MANAGEMENT', 73),
  (gen_random_uuid()::text, 'partner.link',   'Link Clients To Partner',  'PARTNER_MANAGEMENT', 74)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('client.import', 'partner.create', 'partner.link')
ON CONFLICT DO NOTHING;

-- ═══ Response codes ═══════════════════════════════════════════════════════
INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0040', 'Invalid import period',             '400', 'MESSAGE', 'Subscription end date must be after start date',                              'Subscription end date must be after start date',                              'កាលបរិច្ឆេទបញ្ចប់ការជាវត្រូវតែនៅក្រោយកាលបរិច្ឆេទចាប់ផ្តើម',                     'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0041', 'POS tenant managed manually',       '409', 'MESSAGE', 'This POS tenant is managed manually; update POS access instead of syncing',  'This POS tenant is managed manually; update POS access instead of syncing',  'ប្រព័ន្ធ POS នេះត្រូវបានគ្រប់គ្រងដោយដៃ សូមកែប្រែព័ត៌មានចូលប្រើ POS ជំនួសឱ្យការធ្វើសមកាលកម្ម', 'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0042', 'Referral code taken',               '409', 'MESSAGE', 'Partner/referral code is already in use',                                     'Partner/referral code is already in use',                                     'លេខកូដដៃគូ/លេខកូដណែនាំនេះត្រូវបានប្រើប្រាស់រួចហើយ',                              'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0043', 'POS registration already linked',   '409', 'MESSAGE', 'This POS registration is already linked to another client',                  'This POS registration is already linked to another client',                  'ការចុះឈ្មោះ POS នេះត្រូវបានភ្ជាប់ទៅអតិថិជនផ្សេងរួចហើយ',                         'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0044', 'Client already referred',           '409', 'MESSAGE', 'Client is already attributed to another referrer',                            'Client is already attributed to another referrer',                            'អតិថិជននេះត្រូវបានកត់ត្រាជាអ្នកណែនាំផ្សេងរួចហើយ',                                'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0045', 'Invalid referrer',                  '400', 'MESSAGE', 'Invalid referrer',                                                            'Invalid referrer',                                                            'អ្នកណែនាំមិនត្រឹមត្រូវ',                                                          'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0046', 'POS access details required',       '400', 'MESSAGE', 'POS access details are required to link an existing tenant',                 'POS access details are required to link an existing tenant',                 'ត្រូវការព័ត៌មានចូលប្រើ POS ដើម្បីភ្ជាប់ប្រព័ន្ធដែលមានស្រាប់',                       'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
