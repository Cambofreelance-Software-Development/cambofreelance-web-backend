-- ═══ Phone OTP verification on register ═════════════════════════════════
ALTER TABLE public.users ADD COLUMN IF NOT EXISTS phone_verified boolean NOT NULL DEFAULT false;

-- Grandfather existing accounts so the new login gate doesn't lock anyone out;
-- only newly self-registered accounts start out unverified.
UPDATE public.users SET phone_verified = true WHERE phone_verified = false;

-- ═══ Response codes ══════════════════════════════════════════════════════
INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0023', 'Phone number not verified', '403', 'MESSAGE', 'Please verify your phone number before logging in', 'Please verify your phone number before logging in', 'សូមផ្ទៀងផ្ទាត់លេខទូរស័ព្ទរបស់អ្នកមុននឹងចូលប្រើ', 'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0024', 'Too many OTP requests', '429', 'MESSAGE', 'Too many OTP requests, please try again later', 'Too many OTP requests, please try again later', 'សំណើសុំកូដផ្ទៀងផ្ទាត់ច្រើនពេក សូមព្យាយាមម្តងទៀតនៅពេលក្រោយ', 'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
