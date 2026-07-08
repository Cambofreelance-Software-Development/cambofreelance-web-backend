-- Reset-password errors (invalid/expired OTP, password mismatch, account not
-- found) were seeded with http_status '200', so a failed reset was returned as
-- HTTP 200 and clients showed the success screen. Return proper error statuses
-- so the message actually reaches the user.
UPDATE public.response_codes
SET http_status = '400',
    updated_at  = NOW(),
    updated_by  = 'SYS'
WHERE code IN ('ERR-0003', 'ERR-0011', 'ERR-0012')
  AND http_status = '200';

UPDATE public.response_codes
SET http_status = '404',
    updated_at  = NOW(),
    updated_by  = 'SYS'
WHERE code = 'ERR-0002'
  AND http_status = '200';
