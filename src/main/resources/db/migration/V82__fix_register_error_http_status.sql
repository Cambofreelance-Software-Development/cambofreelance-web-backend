-- Register conflict errors (username/email/phone already exists) were seeded
-- with http_status '200', so failed registrations were returned as HTTP 200 and
-- clients treated them as success. Return 409 Conflict so the error message
-- actually reaches the user.
UPDATE public.response_codes
SET http_status = '409',
    updated_at  = NOW(),
    updated_by  = 'SYS'
WHERE code IN ('ERR-0006', 'ERR-0007', 'ERR-0008')
  AND http_status = '200';
