-- ═══ Client Management ═══════════════════════════════════════════════════
-- One business/client profile per registered account (owner user).
CREATE TABLE IF NOT EXISTS public.clients (
    id               VARCHAR(36)   PRIMARY KEY,
    user_id          VARCHAR(255)  NOT NULL UNIQUE REFERENCES public.users(user_id),
    company_name     VARCHAR(255),
    company_email    VARCHAR(255),
    company_phone    VARCHAR(50),
    address          VARCHAR(500),
    city             VARCHAR(100),
    country          VARCHAR(100),
    business_type    VARCHAR(100),
    logo_url         VARCHAR(500),
    email_verified   BOOLEAN       NOT NULL DEFAULT FALSE,
    client_status    VARCHAR(10)   NOT NULL DEFAULT 'PENDING',      -- PENDING / ACTIVE / SUSPENDED / CLOSED
    onboarding_step  VARCHAR(30)   NOT NULL DEFAULT 'VERIFY_EMAIL', -- VERIFY_EMAIL / COMPANY_INFO / SELECT_PACKAGE / PAYMENT / DONE
    created_by       VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by       VARCHAR(255),
    updated_at       TIMESTAMP,
    status           VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);
CREATE INDEX IF NOT EXISTS idx_clients_status ON public.clients (client_status);

-- Existing self-registered accounts are grandfathered as fully onboarded clients
INSERT INTO public.clients (id, user_id, company_email, email_verified, client_status, onboarding_step)
SELECT gen_random_uuid()::text, u.user_id, u.email, TRUE,
       CASE WHEN u.approval_status = 'APR' THEN 'ACTIVE' ELSE 'PENDING' END, 'DONE'
FROM public.users u
WHERE u.user_type = 'USER' AND u.status <> 'DEL'
ON CONFLICT (user_id) DO NOTHING;

-- ═══ Billing & Invoice ═══════════════════════════════════════════════════
CREATE TABLE IF NOT EXISTS public.invoices (
    id              VARCHAR(36)   PRIMARY KEY,
    invoice_no      VARCHAR(30)   NOT NULL UNIQUE,
    user_id         VARCHAR(255)  NOT NULL REFERENCES public.users(user_id),
    subscription_id VARCHAR(36)   REFERENCES public.user_subscription(id),
    transaction_id  VARCHAR(36)   REFERENCES public.payment_transaction(id),
    subtotal        NUMERIC(10,2) NOT NULL,
    tax_rate        NUMERIC(5,2)  NOT NULL DEFAULT 0,
    tax_amount      NUMERIC(10,2) NOT NULL DEFAULT 0,
    total           NUMERIC(10,2) NOT NULL,
    currency        VARCHAR(8)    NOT NULL DEFAULT 'USD',
    invoice_status  VARCHAR(15)   NOT NULL DEFAULT 'ISSUED',       -- ISSUED / PAID / REFUNDED / CANCELLED
    issued_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    paid_at         TIMESTAMP,
    created_by      VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      VARCHAR(255),
    updated_at      TIMESTAMP,
    status          VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);
CREATE INDEX IF NOT EXISTS idx_invoices_user   ON public.invoices (user_id);
CREATE INDEX IF NOT EXISTS idx_invoices_status ON public.invoices (invoice_status);

-- Single-row billing configuration (tax + invoice numbering)
CREATE TABLE IF NOT EXISTS public.billing_settings (
    id             VARCHAR(36)  PRIMARY KEY,
    tax_rate       NUMERIC(5,2) NOT NULL DEFAULT 0,
    tax_label      VARCHAR(50)  NOT NULL DEFAULT 'VAT',
    invoice_prefix VARCHAR(10)  NOT NULL DEFAULT 'INV',
    updated_by     VARCHAR(255),
    updated_at     TIMESTAMP
);
INSERT INTO public.billing_settings (id, tax_rate, tax_label, invoice_prefix)
SELECT gen_random_uuid()::text, 0, 'VAT', 'INV'
WHERE NOT EXISTS (SELECT 1 FROM public.billing_settings);

-- ═══ Payment Workflow ════════════════════════════════════════════════════
-- Immutable log of every payment status transition (callback, poll, job, manual)
CREATE TABLE IF NOT EXISTS public.payment_event_log (
    id             VARCHAR(36)  PRIMARY KEY,
    transaction_id VARCHAR(36)  NOT NULL REFERENCES public.payment_transaction(id),
    from_status    VARCHAR(20),
    to_status      VARCHAR(20)  NOT NULL,
    source         VARCHAR(20)  NOT NULL,   -- CHECKOUT / CALLBACK / POLL / JOB / MANUAL / RECONCILE
    note           VARCHAR(500),
    actor          VARCHAR(255),
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_payment_event_log_tx ON public.payment_event_log (transaction_id);

-- Manual verification metadata on the transaction itself
ALTER TABLE public.payment_transaction ADD COLUMN IF NOT EXISTS verified_by   VARCHAR(255);
ALTER TABLE public.payment_transaction ADD COLUMN IF NOT EXISTS verify_method VARCHAR(10);   -- AUTO / MANUAL
ALTER TABLE public.payment_transaction ADD COLUMN IF NOT EXISTS verify_note   VARCHAR(500);

-- ═══ Permissions ═════════════════════════════════════════════════════════
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'client.view',      'View Clients',        'CLIENT_MANAGEMENT', 60),
  (gen_random_uuid()::text, 'client.update',    'Update Clients',      'CLIENT_MANAGEMENT', 61),
  (gen_random_uuid()::text, 'client.status',    'Change Client Status','CLIENT_MANAGEMENT', 62),
  (gen_random_uuid()::text, 'invoice.view',     'View Invoices',       'BILLING',           63),
  (gen_random_uuid()::text, 'billing.settings', 'Billing Settings',    'BILLING',           64),
  (gen_random_uuid()::text, 'payment.verify',   'Verify Payments',     'BILLING',           65)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('client.view','client.update','client.status','invoice.view','billing.settings','payment.verify')
ON CONFLICT DO NOTHING;
