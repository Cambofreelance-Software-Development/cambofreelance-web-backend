-- ── Prorated plan upgrades ────────────────────────────────────────────────
-- A checkout transaction now records which plan it will apply to activation
-- (equal to the subscription's current plan for a plain renewal, different
-- for an upgrade) and whether its amount was prorated for a mid-cycle switch.
ALTER TABLE public.payment_transaction ADD COLUMN IF NOT EXISTS target_plan_id VARCHAR(36) REFERENCES public.pricing_plan(id);
ALTER TABLE public.payment_transaction ADD COLUMN IF NOT EXISTS prorated BOOLEAN NOT NULL DEFAULT FALSE;

INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0018', 'Plan change is not an upgrade', '400', 'MESSAGE', 'The selected plan must cost more than your current plan to switch with prorated billing', 'The selected plan must cost more than your current plan to switch with prorated billing', 'គម្រោងដែលបានជ្រើសរើសត្រូវតែមានតម្លៃខ្ពស់ជាងគម្រោងបច្ចុប្បន្នរបស់អ្នក ដើម្បីប្ដូរដោយគិតថ្លៃតាមសមាមាត្រ', 'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
