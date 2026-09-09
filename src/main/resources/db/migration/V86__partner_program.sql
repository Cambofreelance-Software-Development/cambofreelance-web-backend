-- ── Partner program (referral program — phase 2) ─────────────────────────
-- A user applies once to become a referral partner. An admin reviews the
-- application; on approval the user's users.referral_code is switched to the
-- assigned partner ref (PTR-YYYY-NNNN) so the existing referral-attribution
-- pipeline (registration -> user_subscription.referrer_id ->
-- payment_transaction.referrer_id) credits every downstream payment to the
-- partner with no further wiring. Commission tier/rate are computed live from
-- the count of active referred subscriptions — not stored here.

CREATE TABLE IF NOT EXISTS public.partner_applications (
    id                        VARCHAR(36)   PRIMARY KEY,
    user_id                   VARCHAR(255)  NOT NULL UNIQUE REFERENCES public.users(user_id),
    partner_ref               VARCHAR(20)   UNIQUE,                  -- PTR-YYYY-NNNN, assigned on first submit
    app_status                VARCHAR(20)   NOT NULL DEFAULT 'SUBMITTED', -- SUBMITTED / UNDER_REVIEW / APPROVED / REJECTED / WITHDRAWN

    -- Section 1: partner & company
    partner_name              VARCHAR(200)  NOT NULL,
    company_name              VARCHAR(200)  NOT NULL,
    partner_type              VARCHAR(40)   NOT NULL,                -- RESELLER / SYSTEM_INTEGRATOR / AGENCY / INDIVIDUAL / OTHER
    business_type             VARCHAR(80),
    employee_count            VARCHAR(20),
    business_registration_no  VARCHAR(80),
    website                   VARCHAR(200),

    -- Section 2: location & address
    country                   VARCHAR(80)   NOT NULL,
    city                      VARCHAR(80)   NOT NULL,
    business_address          VARCHAR(500)  NOT NULL,

    -- Section 3: assets & notes
    logo_url                  VARCHAR(500),
    notes                     VARCHAR(1000),
    agreement_accepted        BOOLEAN       NOT NULL DEFAULT FALSE,

    payout_channel            VARCHAR(20)   NOT NULL DEFAULT 'ABA',

    -- review + activation
    submitted_at              TIMESTAMP,
    reviewed_by               VARCHAR(255),
    reviewed_at               TIMESTAMP,
    review_note               VARCHAR(1000),
    activated_at              TIMESTAMP,

    created_by                VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at                TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by                VARCHAR(255),
    updated_at                TIMESTAMP,
    status                    VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);
CREATE INDEX IF NOT EXISTS idx_partner_applications_status ON public.partner_applications (app_status);
CREATE INDEX IF NOT EXISTS idx_partner_applications_user   ON public.partner_applications (user_id);

-- Recorded partner commission payouts — "pending payout" = earned commission − SUM(paid).
CREATE TABLE IF NOT EXISTS public.partner_payouts (
    id             VARCHAR(36)   PRIMARY KEY,
    application_id VARCHAR(36)   NOT NULL REFERENCES public.partner_applications(id),
    amount         NUMERIC(12,2) NOT NULL,
    channel        VARCHAR(20)   NOT NULL DEFAULT 'ABA',
    reference      VARCHAR(120),
    note           VARCHAR(500),
    paid_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by     VARCHAR(255),
    updated_at     TIMESTAMP,
    status         VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);
CREATE INDEX IF NOT EXISTS idx_partner_payouts_application ON public.partner_payouts (application_id);

-- ── Permissions ──────────────────────────────────────────────────────────
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'partner.view',   'View Partner Applications',   'PARTNER_MANAGEMENT', 70),
  (gen_random_uuid()::text, 'partner.review', 'Review Partner Applications', 'PARTNER_MANAGEMENT', 71),
  (gen_random_uuid()::text, 'partner.payout', 'Record Partner Payouts',      'PARTNER_MANAGEMENT', 72)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('partner.view', 'partner.review', 'partner.payout')
ON CONFLICT DO NOTHING;

-- ── Response codes ───────────────────────────────────────────────────────
INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0031', 'Partner application not found',      '404', 'MESSAGE', 'Partner application not found',                  'Partner application not found',                  'រកមិនឃើញពាក្យសុំធ្វើជាដៃគូទេ',                    'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0032', 'Partner application already exists', '400', 'MESSAGE', 'You already have a partner application in progress', 'You already have a partner application in progress', 'អ្នកមានពាក្យសុំធ្វើជាដៃគូកំពុងដំណើរការរួចហើយ',   'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0033', 'Partner application not editable',   '400', 'MESSAGE', 'This application can no longer be edited',        'This application can no longer be edited',        'ពាក្យសុំនេះមិនអាចកែប្រែបានទៀតទេ',                'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0034', 'Partner agreement required',         '400', 'MESSAGE', 'You must accept the partner agreement',          'You must accept the partner agreement',          'អ្នកត្រូវតែយល់ព្រមលើកិច្ចព្រមព្រៀងដៃគូ',          'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0035', 'Not an active partner',              '403', 'MESSAGE', 'Your partner account is not active yet',          'Your partner account is not active yet',          'គណនីដៃគូរបស់អ្នកមិនទាន់សកម្មនៅឡើយទេ',            'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0036', 'Invalid partner application state',  '400', 'MESSAGE', 'This action is not allowed in the current state', 'This action is not allowed in the current state', 'សកម្មភាពនេះមិនត្រូវបានអនុញ្ញាតក្នុងស្ថានភាពបច្ចុប្បន្ន', 'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
