-- ═══ Google login/register ═══════════════════════════════════════════════
-- Google-provisioned accounts have no phone number to store.
ALTER TABLE public.users ALTER COLUMN phone_number DROP NOT NULL;

-- ═══ Response codes ══════════════════════════════════════════════════════
INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0022', 'Google authentication failed', '401', 'MESSAGE', 'Google authentication failed', 'Google authentication failed', 'ការផ្ទៀងផ្ទាត់តាមរយៈ Google បានបរាជ័យ', 'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
