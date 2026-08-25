-- ═══ Email is now an alternative register-OTP channel alongside phone ═════
ALTER TABLE public.users ADD COLUMN IF NOT EXISTS email_verified boolean NOT NULL DEFAULT false;

-- Grandfather existing accounts so the login gate doesn't lock anyone out.
UPDATE public.users SET email_verified = true WHERE email_verified = false;

-- The login gate now accepts either channel, so the message is no longer phone-specific.
UPDATE public.response_codes
SET message_en = 'Please verify your phone number or email before logging in',
    message_cn = 'Please verify your phone number or email before logging in',
    message_km = 'សូមផ្ទៀងផ្ទាត់លេខទូរស័ព្ទ ឬអ៊ីមែលរបស់អ្នកមុននឹងចូលប្រើ',
    description = 'Account not verified'
WHERE code = 'ERR-0023';
