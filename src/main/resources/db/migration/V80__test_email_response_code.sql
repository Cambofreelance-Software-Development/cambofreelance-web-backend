-- ═══ Surface admin test-email failures instead of a silent success ═══
INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0027', 'Failed to send test email', '502', 'MESSAGE', 'Failed to send the test email. Check the SMTP configuration.', 'Failed to send the test email. Check the SMTP configuration.', 'មិនអាចផ្ញើអ៊ីមែលសាកល្បងបានទេ។ សូមពិនិត្យការកំណត់ SMTP។', 'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
