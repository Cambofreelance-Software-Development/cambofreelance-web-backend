-- Response codes for authentication-service (PostgreSQL)
-- Uses ON CONFLICT (code) DO NOTHING to be idempotent

INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    -- ── Success codes ──────────────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS', 'SUC-00001', 'Get data successfully',          '200', 'MESSAGE', 'Get data successfully',             'Get data successfully',             'ទទួលបានទិន្នន័យដោយជោគជ័យ',             'SUC', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'SUC-00002', 'Create record successfully',     '200', 'MESSAGE', 'Create record successfully',        'Create record successfully',        'បង្កើតទិន្នន័យដោយជោគជ័យ',             'SUC', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'SUC-00003', 'Update record successfully',     '200', 'MESSAGE', 'Update record successfully',        'Update record successfully',        'កែប្រែទិន្នន័យដោយជោគជ័យ',             'SUC', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'SUC-00004', 'Delete record successfully',     '200', 'MESSAGE', 'Delete record successfully',        'Delete record successfully',        'លុបទិន្នន័យដោយជោគជ័យ',               'SUC', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'SUC-001',   'Create account successfully',    '200', 'MESSAGE', 'Create account successfully',       'Create account successfully',       'បង្កើតគណនីដោយជោគជ័យ',                'SUC', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'SUC-002',   'Update account successfully',    '200', 'MESSAGE', 'Update account successfully',       'Update account successfully',       'កែប្រែគណនីដោយជោគជ័យ',                'SUC', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'SUC-003',   'Change password successfully',   '200', 'MESSAGE', 'Change password successfully',      'Change password successfully',      'ផ្លាស់ប្ដូរពាក្យសម្ងាត់ដោយជោគជ័យ',   'SUC', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'SUC-004',   'Update data successfully',       '200', 'MESSAGE', 'Update data successfully',          'Update data successfully',          'កែប្រែទិន្នន័យដោយជោគជ័យ',             'SUC', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'SUC-005',   'Delete data successfully',       '200', 'MESSAGE', 'Delete data successfully',          'Delete data successfully',          'លុបទិន្នន័យដោយជោគជ័យ',               'SUC', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'SUC-006',   'Update password successfully',   '200', 'MESSAGE', 'Update password successfully',      'Update password successfully',      'ធ្វើបច្ចុប្បន្នភាពពាក្យសម្ងាត់ដោយជោគជ័យ', 'SUC', 'ALL', 'ACT'),

    -- ── Auth / login codes ─────────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00007', 'Login successfully',             '200', 'MESSAGE', 'Login successfully',                'Login successfully',                'ចូលប្រើប្រាស់ដោយជោគជ័យ',               'SUC', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00003', 'Unauthorized',                  '401', 'MESSAGE', 'Username or password is incorrect', 'Username or password is incorrect', 'ឈ្មោះអ្នកប្រើ ឬពាក្យសម្ងាត់មិនត្រឹមត្រូវ', 'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-401',   'User unauthorized',             '401', 'MESSAGE', 'You are not authorized',            'You are not authorized',            'អ្នកមិនត្រូវបានអនុញ្ញាត',               'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-004',   'User not permission',           '403', 'MESSAGE', 'You do not have permission',        'You do not have permission',        'អ្នកមិនមានសិទ្ធិ',                       'ERR', 'ALL', 'ACT'),

    -- ── User / account codes ───────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00004', 'Account not required change password', '200', 'MESSAGE', 'Your account does not require a password change', 'Your account does not require a password change', 'គណនីរបស់អ្នកមិនតម្រូវឱ្យផ្លាស់ប្ដូរពាក្យសម្ងាត់', 'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00005', 'User already exists',           '200', 'MESSAGE', 'User already exists',               'User already exists',               'អ្នកប្រើប្រាស់មានរួចហើយ',               'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00006', 'User not found',                '200', 'MESSAGE', 'User not found',                    'User not found',                    'រកមិនឃើញអ្នកប្រើប្រាស់',               'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00008', 'User not active',               '200', 'MESSAGE', 'User account is not active',        'User account is not active',        'គណនីអ្នកប្រើប្រាស់មិនសកម្ម',            'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0001',  'Login ID already exists',       '200', 'MESSAGE', 'Login ID already exists',           'Login ID already exists',           'លេខសម្គាល់ចូលប្រើមានរួចហើយ',            'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0002',  'Account not found',             '200', 'MESSAGE', 'Account not found',                 'Account not found',                 'រកមិនឃើញគណនី',                        'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0003',  'Confirm password not match',    '200', 'MESSAGE', 'Confirm password does not match',   'Confirm password does not match',   'ការបញ្ជាក់ពាក្យសម្ងាត់មិនត្រូវគ្នា',     'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0004',  'Current password not match',    '200', 'MESSAGE', 'Current password does not match',   'Current password does not match',   'ពាក្យសម្ងាត់បច្ចុប្បន្នមិនត្រូវគ្នា',     'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0005',  'Account not enough balance',    '200', 'MESSAGE', 'Account has insufficient balance',  'Account has insufficient balance',  'គណនីមានសមតុល្យមិនគ្រប់គ្រាន់',          'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0006',  'Username already exists',       '200', 'MESSAGE', 'Username already exists',           'Username already exists',           'ឈ្មោះអ្នកប្រើប្រាស់មានរួចហើយ',           'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0007',  'Email already exists',          '200', 'MESSAGE', 'Email already exists',              'Email already exists',              'អ៊ីមែលមានរួចហើយ',                       'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0008',  'Phone number already exists',   '200', 'MESSAGE', 'Phone number already exists',       'Phone number already exists',       'លេខទូរស័ព្ទមានរួចហើយ',                   'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-003',   'Name already in use',           '200', 'MESSAGE', 'Name is already in use',            'Name is already in use',            'ឈ្មោះត្រូវបានប្រើប្រាស់រួចហើយ',           'ERR', 'ALL', 'ACT'),

    -- ── Request / server error codes ───────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00001', 'Invalid request',               '400', 'MESSAGE', 'Invalid request',                   'Invalid request',                   'សំណើមិនត្រឹមត្រូវ',                      'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00002', 'General error',                 '500', 'MESSAGE', 'An error occurred, please try again', 'An error occurred, please try again', 'មានបញ្ហាកើតឡើង សូមព្យាយាមម្តងទៀត',      'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-001',   'Invalid request error',         '400', 'MESSAGE', 'Invalid request parameters',        'Invalid request parameters',        'ប៉ារ៉ាម៉ែត្រសំណើមិនត្រឹមត្រូវ',          'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-500',   'Internal server error',         '500', 'MESSAGE', 'Internal server error',             'Internal server error',             'កំហុសម៉ាស៊ីនបម្រើផ្ទៃក្នុង',              'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-00009', 'Access denied',                 '403', 'MESSAGE', 'Access denied',                     'Access denied',                     'ការចូលប្រើប្រាស់ត្រូវបានបដិសេធ',          'ERR', 'ALL', 'ACT')

ON CONFLICT (code) DO NOTHING;
