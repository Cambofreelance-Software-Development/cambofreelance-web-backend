-- The change-password error "Current password does not match" (ERR-0004) was
-- seeded with http_status '200', so a failed change was returned as HTTP 200
-- and clients showed "password changed" while nothing was changed. Return 400
-- so the message actually reaches the user.
UPDATE public.response_codes
SET http_status = '400',
    updated_at  = NOW(),
    updated_by  = 'SYS'
WHERE code = 'ERR-0004'
  AND http_status = '200';
