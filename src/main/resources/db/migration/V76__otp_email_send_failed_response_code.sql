-- ═══ Surface OTP email send failures instead of a silent false "success" ═══
INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0025', 'Failed to send OTP email', '502', 'MESSAGE', 'Failed to send the verification email. Please try again later.', 'Failed to send the verification email. Please try again later.', 'មិនអាចផ្ញើអ៊ីមែលកូដផ្ទៀងផ្ទាត់បានទេ។ សូមព្យាយាមម្តងទៀតនៅពេលក្រោយ។', 'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
