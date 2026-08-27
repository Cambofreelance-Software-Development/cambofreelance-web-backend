-- Admin-initiated refund metadata on the transaction itself
ALTER TABLE public.payment_transaction ADD COLUMN IF NOT EXISTS refunded_by   VARCHAR(255);
ALTER TABLE public.payment_transaction ADD COLUMN IF NOT EXISTS refund_reason VARCHAR(500);
ALTER TABLE public.payment_transaction ADD COLUMN IF NOT EXISTS refunded_at   TIMESTAMP;

INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'payment.refund', 'Refund Payments', 'BILLING', 66)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code = 'payment.refund'
ON CONFLICT DO NOTHING;
