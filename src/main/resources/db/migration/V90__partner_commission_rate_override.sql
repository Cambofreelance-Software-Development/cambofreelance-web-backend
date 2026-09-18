-- Lets an admin pin a partner's commission rate to a specific value (e.g. a negotiated
-- rate outside the standard BRONZE/SILVER/GOLD ladder). NULL = keep using the tier-derived
-- rate from PartnerTier.rateOf(tier), as before.
ALTER TABLE public.partner_applications
    ADD COLUMN IF NOT EXISTS commission_rate_override NUMERIC(5,4);

INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0039', 'Invalid commission rate', '400', 'MESSAGE', 'Commission rate must be between 0% and 100%', 'Commission rate must be between 0% and 100%', 'អត្រាកម្រៃជើងសារត្រូវតែចន្លោះពី 0% ដល់ 100%', 'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
