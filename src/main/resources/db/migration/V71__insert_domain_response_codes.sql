-- All domain-specific error codes thrown via new AppException("CODE", ...) throughout
-- the service layer.  Codes are idempotent — ON CONFLICT (code) DO NOTHING.

INSERT INTO public.response_codes
    (created_at, created_by, updated_at, updated_by,
     code, description, http_status, key,
     message_en, message_cn, message_km,
     type, service_type, status)
VALUES

    -- ── Article ───────────────────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS',
     'ARTICLE_NOT_FOUND',         'Article not found',                      '200', 'MESSAGE',
     'Article not found',                    'Article not found',                    'រកមិនឃើញអត្ថបទ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'INVALID_WORKFLOW_STATUS',   'Invalid article workflow status',         '400', 'MESSAGE',
     'Invalid workflow status',              'Invalid workflow status',              'ស្ថានភាព workflow មិនត្រឹមត្រូវ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'INVALID_WORKFLOW_TRANSITION','Invalid article workflow transition',    '400', 'MESSAGE',
     'Invalid workflow transition',          'Invalid workflow transition',          'ការផ្លាស់ប្ដូរ workflow មិនត្រឹមត្រូវ',
     'ERR', 'ALL', 'ACT'),

    -- ── Media ─────────────────────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS',
     'MEDIA_NOT_FOUND',           'Media file not found',                   '200', 'MESSAGE',
     'Media file not found',                 'Media file not found',                 'រកមិនឃើញឯកសារ media',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'UNSUPPORTED_MEDIA_TYPE',    'Unsupported media type',                  '400', 'MESSAGE',
     'Unsupported media type',               'Unsupported media type',               'ប្រភេទ media មិនត្រូវបានគាំទ្រ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'STORAGE_NOT_CONFIGURED',    'Storage is not configured',               '500', 'MESSAGE',
     'Storage is not configured',            'Storage is not configured',            'ឧបករណ៍ផ្ទុកទិន្នន័យមិនទាន់បានកំណត់',
     'ERR', 'ALL', 'ACT'),

    -- ── Invoice ───────────────────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS',
     'INVOICE_NOT_FOUND',         'Invoice not found',                       '200', 'MESSAGE',
     'Invoice not found',                    'Invoice not found',                    'រកមិនឃើញវិក្កយបត្រ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'INVOICE_ALREADY_EXISTS',    'Invoice already exists for this period',  '200', 'MESSAGE',
     'Invoice already exists for this billing period', 'Invoice already exists for this billing period', 'វិក្កយបត្រនេះមានរួចហើយសម្រាប់រយៈពេលនេះ',
     'ERR', 'ALL', 'ACT'),

    -- ── Subscription ─────────────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS',
     'SUBSCRIPTION_NOT_FOUND',    'Subscription not found',                  '200', 'MESSAGE',
     'Subscription not found',               'Subscription not found',               'រកមិនឃើញការជាវ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'SUBSCRIPTION_NOT_ACTIVE',   'Subscription is not active',              '200', 'MESSAGE',
     'Only an active subscription can perform this action', 'Only an active subscription can perform this action', 'ត្រូវការការជាវសកម្មដើម្បីធ្វើសកម្មភាពនេះ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'SUBSCRIPTION_NOT_PENDING',  'Subscription is not pending',             '200', 'MESSAGE',
     'Only a pending subscription can be activated', 'Only a pending subscription can be activated', 'មានតែការជាវដែលកំពុងរង់ចាំប៉ុណ្ណោះអាចធ្វើឱ្យសកម្ម',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'SUBSCRIPTION_NOT_SUSPENDED','Subscription is not suspended',           '200', 'MESSAGE',
     'Only a suspended subscription can be resumed', 'Only a suspended subscription can be resumed', 'មានតែការជាវដែលត្រូវបានផ្អាកអាចបន្តបាន',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'NO_ACTIVE_SUBSCRIPTION',    'No active subscription found',            '200', 'MESSAGE',
     'Tenant has no active subscription',    'Tenant has no active subscription',    'អ្នកជួលមិនមានការជាវដែលសកម្ម',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'NO_PENDING_SUBSCRIPTION',   'No pending subscription found',           '200', 'MESSAGE',
     'No pending subscription found for this tenant', 'No pending subscription found for this tenant', 'រកមិនឃើញការជាវដែលកំពុងរង់ចាំសម្រាប់អ្នកជួលនេះ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'INVALID_BILLING_CYCLE',     'Invalid billing cycle',                   '400', 'MESSAGE',
     'Billing cycle must be one of MONTHLY, QUARTERLY, YEARLY', 'Billing cycle must be one of MONTHLY, QUARTERLY, YEARLY', 'វដ្តវិក្កយបត្រត្រូវតែជា MONTHLY, QUARTERLY ឬ YEARLY',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'INVALID_PAYMENT_STATUS',    'Invalid payment status',                  '400', 'MESSAGE',
     'Payment status must be one of PENDING, PAID, OVERDUE', 'Payment status must be one of PENDING, PAID, OVERDUE', 'ស្ថានភាពការទូទាត់ត្រូវតែជា PENDING, PAID ឬ OVERDUE',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'AMOUNT_REQUIRED',           'Amount is required',                      '400', 'MESSAGE',
     'Amount is required for custom-priced packages', 'Amount is required for custom-priced packages', 'ចំនួនទឹកប្រាក់ត្រូវការសម្រាប់កញ្ចប់តម្លៃផ្ទាល់ខ្លួន',
     'ERR', 'ALL', 'ACT'),

    -- ── Package ───────────────────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS',
     'PACKAGE_NOT_FOUND',         'Subscription package not found',          '200', 'MESSAGE',
     'Subscription package not found',       'Subscription package not found',       'រកមិនឃើញកញ្ចប់ការជាវ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'PACKAGE_CODE_EXISTS',       'Package code already exists',             '200', 'MESSAGE',
     'Package code already exists',          'Package code already exists',          'លេខកូដកញ្ចប់មានរួចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'INVALID_FEATURE_CODE',      'Invalid package feature code',            '400', 'MESSAGE',
     'Unknown feature code',                 'Unknown feature code',                 'លេខកូដលក្ខណៈពិសេសសំណុំមិនស្គាល់',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'NOT_AN_UPGRADE',            'Selected package is not an upgrade',      '400', 'MESSAGE',
     'Selected package is not a higher tier than the current plan', 'Selected package is not a higher tier than the current plan', 'កញ្ចប់ដែលបានជ្រើសរើសមិនមែនជាកម្រិតខ្ពស់ជាងផែនការបច្ចុប្បន្ន',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'NOT_A_DOWNGRADE',           'Selected package is not a downgrade',     '400', 'MESSAGE',
     'Selected package is not a lower tier than the current plan', 'Selected package is not a lower tier than the current plan', 'កញ្ចប់ដែលបានជ្រើសរើសមិនមែនជាកម្រិតទាបជាងផែនការបច្ចុប្បន្ន',
     'ERR', 'ALL', 'ACT'),

    -- ── Tenant ────────────────────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS',
     'TENANT_NOT_FOUND',          'Tenant not found',                        '200', 'MESSAGE',
     'Tenant not found',                     'Tenant not found',                     'រកមិនឃើញអ្នកជួល',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'TENANT_CODE_EXISTS',        'Tenant code already exists',              '200', 'MESSAGE',
     'Tenant code already exists',           'Tenant code already exists',           'លេខកូដអ្នកជួលមានរួចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'TENANT_SUSPENDED',          'Tenant account is suspended',             '200', 'MESSAGE',
     'Tenant account is suspended. Please contact support.', 'Tenant account is suspended. Please contact support.', 'គណនីអ្នកជួលត្រូវបានផ្អាក សូមទាក់ទងផ្នែកជំនួយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'TENANT_EXPIRED',            'Tenant subscription has expired',         '200', 'MESSAGE',
     'Tenant subscription has expired. Please renew your plan.', 'Tenant subscription has expired. Please renew your plan.', 'ការជាវអ្នកជួលបានផុតកំណត់ សូមបន្តការជាវ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'TENANT_INVALID_STATUS',     'Invalid tenant status value',             '400', 'MESSAGE',
     'Status must be one of ACT, SUS, EXP',  'Status must be one of ACT, SUS, EXP',  'ស្ថានភាពត្រូវតែជា ACT, SUS ឬ EXP',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'TENANT_NOT_PENDING',        'Tenant registration is not pending',      '200', 'MESSAGE',
     'Only a pending tenant registration can be processed', 'Only a pending tenant registration can be processed', 'មានតែការចុះឈ្មោះអ្នកជួលដែលរង់ចាំប៉ុណ្ណោះអាចដំណើរការ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'NO_TENANT_ASSIGNED',        'No tenant assigned to this account',      '200', 'MESSAGE',
     'Your account is not assigned to a tenant', 'Your account is not assigned to a tenant', 'គណនីរបស់អ្នកមិនត្រូវបានចាត់តាំងទៅអ្នកជួល',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'SCHEMA_PROVISION_FAILED',   'Failed to provision tenant schema',       '500', 'MESSAGE',
     'Failed to create schema for tenant',   'Failed to create schema for tenant',   'បរាជ័យក្នុងការបង្កើត schema សម្រាប់អ្នកជួល',
     'ERR', 'ALL', 'ACT'),

    -- ── User ──────────────────────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS',
     'USER_NOT_FOUND',            'User not found',                          '200', 'MESSAGE',
     'User not found',                       'User not found',                       'រកមិនឃើញអ្នកប្រើប្រាស់',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'USERNAME_ALREADY_EXIST',    'Username already exists',                 '200', 'MESSAGE',
     'Username already exists',              'Username already exists',              'ឈ្មោះអ្នកប្រើប្រាស់មានរួចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'EMAIL_ALREADY_EXIST',       'Email already exists',                    '200', 'MESSAGE',
     'Email already in use',                 'Email already in use',                 'អ៊ីមែលនេះមានរួចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'PHONE_ALREADY_EXIST',       'Phone number already exists',             '200', 'MESSAGE',
     'Phone number already in use',          'Phone number already in use',          'លេខទូរស័ព្ទនេះមានរួចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'USER_NOT_IN_TENANT',        'User does not belong to this tenant',     '200', 'MESSAGE',
     'User does not belong to this tenant',  'User does not belong to this tenant',  'អ្នកប្រើប្រាស់មិនស្ថិតនៅក្រោមអ្នកជួលនេះ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'USER_LIMIT_REACHED',        'Tenant user limit reached',               '200', 'MESSAGE',
     'User limit for this tenant has been reached', 'User limit for this tenant has been reached', 'ចំនួនអ្នកប្រើប្រាស់អតិបរមាសម្រាប់អ្នកជួលនេះបានដល់ដែន',
     'ERR', 'ALL', 'ACT'),

    -- ── Role ──────────────────────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS',
     'ROLE_NOT_FOUND',            'Role not found',                          '200', 'MESSAGE',
     'Role not found',                       'Role not found',                       'រកមិនឃើញតួនាទី',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'PERMISSION_NOT_ALLOWED',    'Permission is not allowed for this role', '403', 'MESSAGE',
     'Permission is not allowed for this tenant role', 'Permission is not allowed for this tenant role', 'សិទ្ធិនេះមិនត្រូវបានអនុញ្ញាតសម្រាប់តួនាទីនេះ',
     'ERR', 'ALL', 'ACT'),

    -- ── Generic / shared ─────────────────────────────────────────────────────
    (NOW(), 'SYS', NOW(), 'SYS',
     'NOT_FOUND',                 'Record not found',                        '200', 'MESSAGE',
     'Record not found',                     'Record not found',                     'រកមិនឃើញទិន្នន័យ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'DUPLICATE_CODE',            'Code already exists',                     '200', 'MESSAGE',
     'Code already exists',                  'Code already exists',                  'លេខកូដនេះមានរួចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'INVALID_STATUS',            'Invalid status value',                    '400', 'MESSAGE',
     'Invalid status value',                 'Invalid status value',                 'តម្លៃស្ថានភាពមិនត្រឹមត្រូវ',
     'ERR', 'ALL', 'ACT')

ON CONFLICT (code) DO NOTHING;
