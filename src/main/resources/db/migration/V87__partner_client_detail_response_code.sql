-- Response code for GET /partner/portal/clients/{userId} — the requested user must actually
-- be one of the calling partner's referrals (checked via users.referred_by).
INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0037', 'Referred client not found', '404', 'MESSAGE', 'Referred client not found', 'Referred client not found', 'រកមិនឃើញអតិថិជនដែលបានណែនាំទេ', 'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
