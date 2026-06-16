CREATE SEQUENCE IF NOT EXISTS public.invoice_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS public.invoices (
    id              VARCHAR(20)   PRIMARY KEY,
    tenant_id       VARCHAR(255)  NOT NULL REFERENCES public.tenants(id),
    subscription_id VARCHAR(20)   NOT NULL REFERENCES public.subscriptions(id),
    package_id      VARCHAR(255)  NOT NULL REFERENCES public.subscription_packages(id),
    billing_period  DATE          NOT NULL,
    amount          NUMERIC(12,2) NOT NULL,
    tax             NUMERIC(12,2) NOT NULL DEFAULT 0,
    total_amount    NUMERIC(12,2) NOT NULL,
    currency        VARCHAR(10)   NOT NULL DEFAULT 'USD',
    invoice_status  VARCHAR(20)   NOT NULL DEFAULT 'UNPAID',
    paid_at         TIMESTAMP,
    created_by      VARCHAR(255)  NOT NULL DEFAULT 'SYSTEM',
    created_at      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      VARCHAR(255),
    updated_at      TIMESTAMP,
    status          VARCHAR(3)    NOT NULL DEFAULT 'ACT',
    UNIQUE (tenant_id, billing_period)
);

CREATE INDEX IF NOT EXISTS idx_invoices_tenant_id ON public.invoices (tenant_id);
CREATE INDEX IF NOT EXISTS idx_invoices_invoice_status ON public.invoices (invoice_status);

INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'invoices.view',     'View Invoices',           'SUBSCRIPTION_MANAGEMENT', 98),
  (gen_random_uuid()::text, 'invoices.generate', 'Generate Invoices',       'SUBSCRIPTION_MANAGEMENT', 99),
  (gen_random_uuid()::text, 'invoices.pay',      'Record Invoice Payments', 'SUBSCRIPTION_MANAGEMENT', 100)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code = 'ADMIN'
  AND p.code IN ('invoices.view', 'invoices.generate', 'invoices.pay')
ON CONFLICT DO NOTHING;
