-- Permissions and response codes for Sales, Reservations, Financing Applications, and Documents.

-- 1. Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'inventory.sales.view',       'View Sales & Invoices',           'SALES_MANAGEMENT',     220),
  (gen_random_uuid()::text, 'inventory.sales.create',     'Create Quotation & Sales',        'SALES_MANAGEMENT',     221),
  (gen_random_uuid()::text, 'inventory.sales.confirm',    'Confirm Sale (Deduct Stock)',     'SALES_MANAGEMENT',     222),
  (gen_random_uuid()::text, 'inventory.sales.cancel',     'Cancel Sale (Release Stock)',     'SALES_MANAGEMENT',     223),
  (gen_random_uuid()::text, 'inventory.sales.deliver',    'Mark Unit Delivered',             'SALES_MANAGEMENT',     224),

  (gen_random_uuid()::text, 'inventory.reserve.create',   'Reserve Serialized Unit (VIN)',   'SALES_MANAGEMENT',     225),
  (gen_random_uuid()::text, 'inventory.reserve.release',  'Release Serialized Unit Hold',    'SALES_MANAGEMENT',     226),

  (gen_random_uuid()::text, 'inventory.financing.view',   'View Financing Applications',     'FINANCING_INTEGRATION', 230),
  (gen_random_uuid()::text, 'inventory.financing.submit', 'Submit Financing to Bank',        'FINANCING_INTEGRATION', 231),
  (gen_random_uuid()::text, 'inventory.financing.approve','Approve Financing (Confirm Sale)','FINANCING_INTEGRATION', 232),
  (gen_random_uuid()::text, 'inventory.financing.reject', 'Reject Financing (Release Hold)', 'FINANCING_INTEGRATION', 233),

  (gen_random_uuid()::text, 'inventory.docs.view',        'View Inventory Documents',        'INVENTORY_DOCUMENTS',  240),
  (gen_random_uuid()::text, 'inventory.docs.upload',      'Upload Unit/Sale Documents',      'INVENTORY_DOCUMENTS',  241),
  (gen_random_uuid()::text, 'inventory.docs.verify',      'Verify Uploaded Documents',       'INVENTORY_DOCUMENTS',  242),
  (gen_random_uuid()::text, 'inventory.docs.delete',      'Delete Documents',                'INVENTORY_DOCUMENTS',  243)
ON CONFLICT (code) DO NOTHING;

-- Grant to SUPER_ADMIN and TENANT_ADMIN
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('SUPER_ADMIN', 'TENANT_ADMIN')
  AND p.code IN (
    'inventory.sales.view', 'inventory.sales.create', 'inventory.sales.confirm', 'inventory.sales.cancel', 'inventory.sales.deliver',
    'inventory.reserve.create', 'inventory.reserve.release',
    'inventory.financing.view', 'inventory.financing.submit', 'inventory.financing.approve', 'inventory.financing.reject',
    'inventory.docs.view', 'inventory.docs.upload', 'inventory.docs.verify', 'inventory.docs.delete'
  )
ON CONFLICT DO NOTHING;

-- 2. Domain Response Codes
INSERT INTO public.response_codes
    (created_at, created_by, updated_at, updated_by,
     code, description, http_status, key,
     message_en, message_cn, message_km,
     type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS',
     'SALE_NOT_FOUND',            'Sale record not found',                   '200', 'MESSAGE',
     'Sale record not found',                'Sale record not found',                'រកមិនឃើញការលក់',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'INVALID_SALE_STATUS',       'Invalid sale status transition',          '400', 'MESSAGE',
     'Invalid sale status transition',       'Invalid sale status transition',       'ការផ្លាស់ប្ដូរស្ថានភាពការលក់មិនត្រឹមត្រូវ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'ITEM_ALREADY_RESERVED',     'Unit is already reserved by another salesperson', '400', 'MESSAGE',
     'This serialized unit is already reserved by another salesperson', 'This serialized unit is already reserved by another salesperson', 'ទំនិញនេះត្រូវបានកក់ទុកដោយអ្នកលក់ផ្សេងរួចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'RESERVATION_NOT_FOUND',     'Active reservation not found',            '200', 'MESSAGE',
     'Active reservation not found',         'Active reservation not found',         'រកមិនឃើញការកក់ទំនិញ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'FINANCING_NOT_FOUND',       'Financing application not found',         '200', 'MESSAGE',
     'Financing application not found',      'Financing application not found',      'រកមិនឃើញពាក្យស្នើសុំបង់រំលស់/កម្ចី',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'INVALID_FINANCING_STATUS',  'Invalid financing application status transition', '400', 'MESSAGE',
     'Invalid financing status transition',  'Invalid financing status transition',  'ស្ថានភាពពាក្យស្នើសុំកម្ចីមិនត្រឹមត្រូវ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'DOCUMENT_NOT_FOUND',        'Inventory document not found',            '200', 'MESSAGE',
     'Document not found',                   'Document not found',                   'រកមិនឃើញឯកសារ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'BANK_NOT_FOUND',            'Financing bank partner not found',        '200', 'MESSAGE',
     'Bank not found',                       'Bank not found',                       'រកមិនឃើញធនាគារដៃគូ',
     'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
