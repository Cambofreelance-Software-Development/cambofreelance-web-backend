-- Dedicated response code for PayWay checkout rejections.
-- Previously SubscriptionServiceImpl.createCheckout threw ErrorCode.GENERAL_ERROR (ERR-00002)
-- with ex.setHttpStatus(BAD_GATEWAY), but ERR-00002's catalog row (V6) hardcodes http_status=500
-- and a generic message, so AppLoggerResponseEntityExceptionHandler always overrode the intended
-- 502 with 500 and discarded the specific "Payment gateway rejected the request" message.
INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0029', 'Payment gateway error', '502', 'MESSAGE', 'The payment gateway could not process your request, please try again', 'The payment gateway could not process your request, please try again', 'ផ្នែកទូទាត់មិនអាចដំណើរការសំណើរបស់អ្នកបានទេ សូមព្យាយាមម្តងទៀត', 'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
