-- UserServiceImpl.registerUser throws ERR-0006/0007/0008 (username/email/phone already exists),
-- but their catalog rows (V6) hardcode http_status=200, so AppLoggerResponseEntityExceptionHandler
-- returned HTTP 200 with success=false for a duplicate signup. The frontend only treats non-2xx as
-- an error, so it silently advanced to the OTP step instead of showing the validation message.
UPDATE public.response_codes
SET http_status = '400', updated_at = NOW(), updated_by = 'SYS'
WHERE code IN ('ERR-0006', 'ERR-0007', 'ERR-0008')
  AND http_status = '200';
