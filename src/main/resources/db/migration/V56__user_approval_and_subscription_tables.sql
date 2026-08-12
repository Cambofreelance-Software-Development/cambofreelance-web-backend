-- ── Account approval workflow ─────────────────────────────────────────────
-- New self-registered users start as PEN and must be approved by an admin
-- before they can subscribe/pay. Existing accounts are grandfathered as APR.
ALTER TABLE public.users ADD COLUMN IF NOT EXISTS approval_status VARCHAR(3) NOT NULL DEFAULT 'PEN';
ALTER TABLE public.users ADD COLUMN IF NOT EXISTS approved_by VARCHAR(255);
ALTER TABLE public.users ADD COLUMN IF NOT EXISTS approved_at TIMESTAMP;

UPDATE public.users SET approval_status = 'APR' WHERE approval_status = 'PEN';

-- ── User subscriptions ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.user_subscription (
    id             VARCHAR(36)    PRIMARY KEY,
    user_id        VARCHAR(255)   NOT NULL REFERENCES public.users(user_id),
    plan_id        VARCHAR(36)    NOT NULL REFERENCES public.pricing_plan(id),
    billing_cycle  VARCHAR(10)    NOT NULL,                          -- MONTHLY / YEARLY
    price          NUMERIC(10,2)  NOT NULL,                          -- snapshot at purchase time
    currency       VARCHAR(8)     NOT NULL DEFAULT 'USD',
    sub_status     VARCHAR(20)    NOT NULL DEFAULT 'PENDING_PAYMENT',-- PENDING_PAYMENT / ACTIVE / EXPIRED / CANCELLED
    start_at       TIMESTAMP,
    expires_at     TIMESTAMP,
    created_by     VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by     VARCHAR(255),
    updated_at     TIMESTAMP,
    status         VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);
CREATE INDEX IF NOT EXISTS idx_user_subscription_user   ON public.user_subscription (user_id);
CREATE INDEX IF NOT EXISTS idx_user_subscription_status ON public.user_subscription (sub_status);

-- ── ABA PayWay payment transactions ──────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.payment_transaction (
    id              VARCHAR(36)    PRIMARY KEY,
    tran_id         VARCHAR(20)    NOT NULL UNIQUE,                  -- key sent to / returned by PayWay (max 20 chars)
    subscription_id VARCHAR(36)    NOT NULL REFERENCES public.user_subscription(id),
    user_id         VARCHAR(255)   NOT NULL REFERENCES public.users(user_id),
    amount          NUMERIC(10,2)  NOT NULL,
    currency        VARCHAR(8)     NOT NULL DEFAULT 'USD',
    payment_option  VARCHAR(30),
    payment_status  VARCHAR(20)    NOT NULL DEFAULT 'PENDING',       -- PENDING / APPROVED / DECLINED / CANCELLED / REFUNDED
    apv             VARCHAR(50),
    bank_ref        VARCHAR(100),
    raw_callback    TEXT,
    verified_at     TIMESTAMP,
    created_by      VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at      TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      VARCHAR(255),
    updated_at      TIMESTAMP,
    status          VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);
CREATE INDEX IF NOT EXISTS idx_payment_transaction_sub    ON public.payment_transaction (subscription_id);
CREATE INDEX IF NOT EXISTS idx_payment_transaction_user   ON public.payment_transaction (user_id);
CREATE INDEX IF NOT EXISTS idx_payment_transaction_status ON public.payment_transaction (payment_status);

-- ── Permissions ──────────────────────────────────────────────────────────
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'users.approve',     'Approve Users',      'USER_MANAGEMENT',    15),
  (gen_random_uuid()::text, 'subscription.view', 'View Subscriptions', 'CONTENT_MANAGEMENT', 55)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('users.approve', 'subscription.view')
ON CONFLICT DO NOTHING;

-- ── Response codes for subscription/payment errors ───────────────────────
INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0013', 'Account not approved',        '403', 'MESSAGE', 'Your account is pending admin approval',      'Your account is pending admin approval',      'គណនីរបស់អ្នកកំពុងរង់ចាំការអនុម័តពីអ្នកគ្រប់គ្រង', 'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0014', 'Subscription already active', '400', 'MESSAGE', 'You already have an active subscription',      'You already have an active subscription',      'អ្នកមានការជាវដែលកំពុងដំណើរការរួចហើយ',           'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0015', 'Payment not found',           '404', 'MESSAGE', 'Payment transaction not found',                'Payment transaction not found',                'រកមិនឃើញប្រតិបត្តិការទូទាត់ទេ',                  'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0016', 'Payment not completed',       '400', 'MESSAGE', 'Payment has not been completed',               'Payment has not been completed',               'ការទូទាត់មិនទាន់បានបញ្ចប់ទេ',                    'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0017', 'Plan not available',          '404', 'MESSAGE', 'Pricing plan is not available',                'Pricing plan is not available',                'គម្រោងតម្លៃនេះមិនមានទេ',                        'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
