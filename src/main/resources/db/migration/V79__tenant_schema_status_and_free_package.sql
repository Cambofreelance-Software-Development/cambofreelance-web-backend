-- ── Track async schema provisioning state on the tenant row ─────────────────
-- PENDING = schema/Flyway provisioning running in the background
-- READY   = schema provisioned and usable (default for pre-existing tenants)
-- FAILED  = background provisioning threw; needs admin attention/retry
ALTER TABLE public.tenants ADD COLUMN IF NOT EXISTS schema_status VARCHAR(10) NOT NULL DEFAULT 'READY';

-- ── Seed a FREE package used for the 1-month trial granted on admin-created tenants ──
INSERT INTO public.subscription_packages
  (id, code, name, monthly_price, is_custom_pricing, max_customers, max_loans, max_users, description, sort_order)
VALUES
  (gen_random_uuid()::text, 'FREE', 'Free Trial', 0.00, FALSE, 50, 20, 1, '1-month free trial', 0)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.subscription_package_features (package_id, feature_code, enabled)
SELECT p.id, v.feature_code, v.enabled
FROM public.subscription_packages p
JOIN (VALUES
  ('FREE', 'CUSTOMER_MANAGEMENT',   TRUE),
  ('FREE', 'LOAN_MANAGEMENT',       TRUE),
  ('FREE', 'PAYMENT_COLLECTION',    TRUE),
  ('FREE', 'DASHBOARD_REPORTS',     FALSE),
  ('FREE', 'DOCUMENT_MANAGEMENT',   FALSE),
  ('FREE', 'MOBILE_APP_ACCESS',     FALSE),
  ('FREE', 'API_ACCESS',            FALSE),
  ('FREE', 'CUSTOM_BRANDING',       FALSE),
  ('FREE', 'MULTI_BRANCH_SUPPORT',  FALSE),
  ('FREE', 'WHITE_LABEL',           FALSE)
) AS v(pkg_code, feature_code, enabled) ON v.pkg_code = p.code
ON CONFLICT DO NOTHING;
