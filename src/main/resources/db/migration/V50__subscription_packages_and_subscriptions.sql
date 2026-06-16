-- ── Subscription packages (plan catalog) ────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.subscription_packages (
    id                VARCHAR(255)  PRIMARY KEY,
    code              VARCHAR(50)   NOT NULL UNIQUE,
    name              VARCHAR(255)  NOT NULL,
    monthly_price     NUMERIC(12,2),
    is_custom_pricing BOOLEAN       NOT NULL DEFAULT FALSE,
    max_customers     INTEGER,
    max_loans         INTEGER,
    max_users         INTEGER,
    description       VARCHAR(1000),
    sort_order        INTEGER       NOT NULL DEFAULT 0,
    created_by        VARCHAR(255)  NOT NULL DEFAULT 'SYSTEM',
    created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by        VARCHAR(255),
    updated_at        TIMESTAMP,
    status            VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);

-- ── Per-package feature toggles ──────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.subscription_package_features (
    package_id   VARCHAR(255) NOT NULL REFERENCES public.subscription_packages(id) ON DELETE CASCADE,
    feature_code VARCHAR(50)  NOT NULL,
    enabled      BOOLEAN      NOT NULL DEFAULT FALSE,
    PRIMARY KEY (package_id, feature_code)
);

-- ── Tenant subscriptions (human-readable sequential ID, e.g. SUB-00000001) ──
CREATE SEQUENCE IF NOT EXISTS public.subscription_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS public.subscriptions (
    id                  VARCHAR(20)   PRIMARY KEY,
    tenant_id           VARCHAR(255)  NOT NULL REFERENCES public.tenants(id),
    package_id          VARCHAR(255)  NOT NULL REFERENCES public.subscription_packages(id),
    billing_cycle       VARCHAR(20)   NOT NULL,
    start_date          DATE          NOT NULL,
    end_date            DATE          NOT NULL,
    amount              NUMERIC(12,2) NOT NULL,
    currency            VARCHAR(10)   NOT NULL DEFAULT 'USD',
    payment_status      VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    auto_renewal        BOOLEAN       NOT NULL DEFAULT TRUE,
    created_by          VARCHAR(255)  NOT NULL DEFAULT 'SYSTEM',
    created_at          TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by          VARCHAR(255),
    updated_at          TIMESTAMP,
    status              VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_subscriptions_tenant_id ON public.subscriptions (tenant_id);
CREATE INDEX IF NOT EXISTS idx_subscriptions_status ON public.subscriptions (status);

-- ── Seed the 4 default packages ──────────────────────────────────────────────
INSERT INTO public.subscription_packages
  (id, code, name, monthly_price, is_custom_pricing, max_customers, max_loans, max_users, description, sort_order)
VALUES
  (gen_random_uuid()::text, 'STARTER',      'Starter',      10.00, FALSE, 500,   200,   3,  'Basic LMS',            1),
  (gen_random_uuid()::text, 'STANDARD',     'Standard',     25.00, FALSE, 2000,  1000,  10, 'LMS + Reports',        2),
  (gen_random_uuid()::text, 'PROFESSIONAL', 'Professional', 50.00, FALSE, 10000, 5000,  30, 'LMS + Reports + API',  3),
  (gen_random_uuid()::text, 'ENTERPRISE',   'Enterprise',   NULL,  TRUE,  NULL,  NULL,  NULL, 'All Features',       4)
ON CONFLICT (code) DO NOTHING;

-- ── Seed the feature matrix ──────────────────────────────────────────────────
INSERT INTO public.subscription_package_features (package_id, feature_code, enabled)
SELECT p.id, v.feature_code, v.enabled
FROM public.subscription_packages p
JOIN (VALUES
  ('STARTER',      'CUSTOMER_MANAGEMENT',   TRUE),
  ('STARTER',      'LOAN_MANAGEMENT',       TRUE),
  ('STARTER',      'PAYMENT_COLLECTION',    TRUE),
  ('STARTER',      'DASHBOARD_REPORTS',     FALSE),
  ('STARTER',      'DOCUMENT_MANAGEMENT',   FALSE),
  ('STARTER',      'MOBILE_APP_ACCESS',     FALSE),
  ('STARTER',      'API_ACCESS',            FALSE),
  ('STARTER',      'CUSTOM_BRANDING',       FALSE),
  ('STARTER',      'MULTI_BRANCH_SUPPORT',  FALSE),
  ('STARTER',      'WHITE_LABEL',           FALSE),

  ('STANDARD',     'CUSTOMER_MANAGEMENT',   TRUE),
  ('STANDARD',     'LOAN_MANAGEMENT',       TRUE),
  ('STANDARD',     'PAYMENT_COLLECTION',    TRUE),
  ('STANDARD',     'DASHBOARD_REPORTS',     TRUE),
  ('STANDARD',     'DOCUMENT_MANAGEMENT',   TRUE),
  ('STANDARD',     'MOBILE_APP_ACCESS',     TRUE),
  ('STANDARD',     'API_ACCESS',            FALSE),
  ('STANDARD',     'CUSTOM_BRANDING',       FALSE),
  ('STANDARD',     'MULTI_BRANCH_SUPPORT',  FALSE),
  ('STANDARD',     'WHITE_LABEL',           FALSE),

  ('PROFESSIONAL', 'CUSTOMER_MANAGEMENT',   TRUE),
  ('PROFESSIONAL', 'LOAN_MANAGEMENT',       TRUE),
  ('PROFESSIONAL', 'PAYMENT_COLLECTION',    TRUE),
  ('PROFESSIONAL', 'DASHBOARD_REPORTS',     TRUE),
  ('PROFESSIONAL', 'DOCUMENT_MANAGEMENT',   TRUE),
  ('PROFESSIONAL', 'MOBILE_APP_ACCESS',     TRUE),
  ('PROFESSIONAL', 'API_ACCESS',            TRUE),
  ('PROFESSIONAL', 'CUSTOM_BRANDING',       TRUE),
  ('PROFESSIONAL', 'MULTI_BRANCH_SUPPORT',  TRUE),
  ('PROFESSIONAL', 'WHITE_LABEL',           FALSE),

  ('ENTERPRISE',   'CUSTOMER_MANAGEMENT',   TRUE),
  ('ENTERPRISE',   'LOAN_MANAGEMENT',       TRUE),
  ('ENTERPRISE',   'PAYMENT_COLLECTION',    TRUE),
  ('ENTERPRISE',   'DASHBOARD_REPORTS',     TRUE),
  ('ENTERPRISE',   'DOCUMENT_MANAGEMENT',   TRUE),
  ('ENTERPRISE',   'MOBILE_APP_ACCESS',     TRUE),
  ('ENTERPRISE',   'API_ACCESS',            TRUE),
  ('ENTERPRISE',   'CUSTOM_BRANDING',       TRUE),
  ('ENTERPRISE',   'MULTI_BRANCH_SUPPORT',  TRUE),
  ('ENTERPRISE',   'WHITE_LABEL',           TRUE)
) AS v(pkg_code, feature_code, enabled) ON v.pkg_code = p.code
ON CONFLICT DO NOTHING;

-- ── Permissions ───────────────────────────────────────────────────────────────
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'subscription-packages.view',   'View Subscription Packages',   'SUBSCRIPTION_MANAGEMENT', 90),
  (gen_random_uuid()::text, 'subscription-packages.create', 'Create Subscription Packages', 'SUBSCRIPTION_MANAGEMENT', 91),
  (gen_random_uuid()::text, 'subscription-packages.update', 'Update Subscription Packages', 'SUBSCRIPTION_MANAGEMENT', 92),
  (gen_random_uuid()::text, 'subscription-packages.delete', 'Delete Subscription Packages', 'SUBSCRIPTION_MANAGEMENT', 93),
  (gen_random_uuid()::text, 'subscriptions.view',           'View Subscriptions',           'SUBSCRIPTION_MANAGEMENT', 94),
  (gen_random_uuid()::text, 'subscriptions.create',         'Create/Change Subscriptions',  'SUBSCRIPTION_MANAGEMENT', 95),
  (gen_random_uuid()::text, 'subscriptions.update',         'Update Subscriptions',         'SUBSCRIPTION_MANAGEMENT', 96),
  (gen_random_uuid()::text, 'subscriptions.cancel',         'Cancel Subscriptions',         'SUBSCRIPTION_MANAGEMENT', 97)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code = 'ADMIN'
  AND p.code IN (
    'subscription-packages.view', 'subscription-packages.create', 'subscription-packages.update', 'subscription-packages.delete',
    'subscriptions.view', 'subscriptions.create', 'subscriptions.update', 'subscriptions.cancel'
  )
ON CONFLICT DO NOTHING;
