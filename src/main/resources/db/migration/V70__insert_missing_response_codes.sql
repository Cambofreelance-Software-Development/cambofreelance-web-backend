-- Insert response codes that exist in Java ErrorCode constants but were missing from V6.
-- All rows use ON CONFLICT (code) DO NOTHING so this migration is idempotent.

INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    -- ── Additional success codes ───────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS', 'SUC-00005', 'Change password successfully',  '200', 'MESSAGE', 'Password changed successfully',         'Password changed successfully',         'ការផ្លាស់ប្ដូរពាក្យសម្ងាត់បានជោគជ័យ',         'SUC', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'SUC-00006', 'Update profile successfully',   '200', 'MESSAGE', 'Profile updated successfully',           'Profile updated successfully',           'ព័ត៌មានផ្ទាល់ខ្លួនត្រូវបានធ្វើបច្ចុប្បន្នភាព',  'SUC', 'ALL', 'ACT'),

    -- ── User / account codes (ERR-000xx series) ────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00010', 'Username already exists',       '200', 'MESSAGE', 'Username already exists',               'Username already exists',               'ឈ្មោះអ្នកប្រើប្រាស់មានរួចហើយ',               'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00011', 'Email already exists',          '200', 'MESSAGE', 'Email already exists',                  'Email already exists',                  'អ៊ីមែលមានរួចហើយ',                             'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00012', 'Phone number already exists',   '200', 'MESSAGE', 'Phone number already exists',           'Phone number already exists',           'លេខទូរស័ព្ទមានរួចហើយ',                         'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00013', 'Incorrect username or password','401', 'MESSAGE', 'Incorrect username or password',        'Incorrect username or password',        'ឈ្មោះអ្នកប្រើ ឬពាក្យសម្ងាត់មិនត្រឹមត្រូវ',    'ERR', 'ALL', 'ACT'),

    -- ── Article error codes ────────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00014', 'Article not found',             '200', 'MESSAGE', 'Article not found',                     'Article not found',                     'រកមិនឃើញអត្ថបទ',                              'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00015', 'Invalid article type',          '200', 'MESSAGE', 'Invalid article type',                  'Invalid article type',                  'ប្រភេទអត្ថបទមិនត្រឹមត្រូវ',                    'ERR', 'ALL', 'ACT'),

    -- ── OTP codes ─────────────────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0011',  'Invalid OTP',                   '200', 'MESSAGE', 'OTP is invalid',                        'OTP is invalid',                        'លេខ OTP មិនត្រឹមត្រូវ',                        'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0012',  'OTP expired',                   '200', 'MESSAGE', 'OTP has expired',                       'OTP has expired',                       'លេខ OTP បានផុតកំណត់',                          'ERR', 'ALL', 'ACT'),

    -- ── HTTP / infrastructure error codes ─────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00400', 'Bad request',                   '400', 'MESSAGE', 'Bad request',                           'Bad request',                           'សំណើមិនត្រឹមត្រូវ',                            'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00408', 'Web client request error',      '408', 'MESSAGE', 'Request timeout or web client error',   'Request timeout or web client error',   'សំណើអស់ពេល ឬកំហុសសំណើរបស់ client',            'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00500', 'Internal server error',         '500', 'MESSAGE', 'Internal server error',                 'Internal server error',                 'កំហុសម៉ាស៊ីនបម្រើផ្ទៃក្នុង',                   'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00502', 'Database connection error',     '500', 'MESSAGE', 'Database connection error',             'Database connection error',             'កំហុសការតភ្ជាប់មូលដ្ឋានទិន្នន័យ',               'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00503', 'Service unavailable',           '503', 'MESSAGE', 'Service is currently unavailable',      'Service is currently unavailable',      'សេវាកម្មមិនអាចប្រើបាននាពេលនេះ',               'ERR', 'ALL', 'ACT'),

    -- ── Validation error code ─────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR_00033', 'Invalid field',                 '400', 'MESSAGE', 'One or more fields are invalid',        'One or more fields are invalid',        'វាលមួយឬច្រើនមិនត្រឹមត្រូវ',                   'ERR', 'ALL', 'ACT')

ON CONFLICT (code) DO NOTHING;
