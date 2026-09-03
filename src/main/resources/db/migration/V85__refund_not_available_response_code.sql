-- Dedicated response code for the PayWay refund stub.
-- PaywayClient.refund() threw ErrorCode.GENERAL_ERROR (ERR-00002) with ex.setHttpStatus(NOT_IMPLEMENTED),
-- but ERR-00002's catalog row (V6) hardcodes http_status=500 and a generic message, so
-- AppLoggerResponseEntityExceptionHandler always overrode the intended 501 with 500 and discarded
-- the specific "process the refund in ABA's merchant portal" message (same bug V84 fixed for checkout).
INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0030', 'Refund not available', '501', 'MESSAGE', 'Automatic refund via ABA PayWay is not available yet — process it in ABA''s merchant portal', 'Automatic refund via ABA PayWay is not available yet — process it in ABA''s merchant portal', 'ការសងប្រាក់វិញដោយស្វ័យប្រវត្តិតាមរយៈ ABA PayWay មិនទាន់អាចប្រើបានទេ សូមដំណើរការវានៅក្នុងផ្ទាំងគ្រប់គ្រងអាជីវកររបស់ ABA', 'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
