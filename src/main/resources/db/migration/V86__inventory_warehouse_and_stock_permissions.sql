-- Permissions and response codes for Warehouses, Inventory Items, Batches, and Stock Movements.

-- 1. Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'inventory.warehouses.view',   'View Warehouses & Locations',     'INVENTORY_MANAGEMENT', 210),
  (gen_random_uuid()::text, 'inventory.warehouses.manage', 'Manage Warehouses & Locations',   'INVENTORY_MANAGEMENT', 211),
  (gen_random_uuid()::text, 'inventory.items.view',        'View Serialized Inventory Items', 'INVENTORY_MANAGEMENT', 212),
  (gen_random_uuid()::text, 'inventory.items.create',      'Receive Serialized Inventory',    'INVENTORY_MANAGEMENT', 213),
  (gen_random_uuid()::text, 'inventory.items.update',      'Update Item Status / Details',    'INVENTORY_MANAGEMENT', 214),
  (gen_random_uuid()::text, 'inventory.batches.view',      'View Inventory Batches',          'INVENTORY_MANAGEMENT', 215),
  (gen_random_uuid()::text, 'inventory.batches.manage',    'Manage Inventory Batches',        'INVENTORY_MANAGEMENT', 216),
  (gen_random_uuid()::text, 'inventory.movements.view',    'View Stock Movements & Ledger',   'INVENTORY_MANAGEMENT', 217),
  (gen_random_uuid()::text, 'inventory.stock.adjust',      'Adjust Stock & Transfers',        'INVENTORY_MANAGEMENT', 218)
ON CONFLICT (code) DO NOTHING;

-- Grant to SUPER_ADMIN and TENANT_ADMIN
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('SUPER_ADMIN', 'TENANT_ADMIN')
  AND p.code IN (
    'inventory.warehouses.view',
    'inventory.warehouses.manage',
    'inventory.items.view',
    'inventory.items.create',
    'inventory.items.update',
    'inventory.batches.view',
    'inventory.batches.manage',
    'inventory.movements.view',
    'inventory.stock.adjust'
  )
ON CONFLICT DO NOTHING;

-- 2. Domain Response Codes for Inventory Operations
INSERT INTO public.response_codes
    (created_at, created_by, updated_at, updated_by,
     code, description, http_status, key,
     message_en, message_cn, message_km,
     type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS',
     'WAREHOUSE_NOT_FOUND',       'Warehouse not found',                     '200', 'MESSAGE',
     'Warehouse not found',                  'Warehouse not found',                  'រកមិនឃើញឃ្លាំង',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'DUPLICATE_WAREHOUSE_CODE',  'Warehouse code already exists',           '400', 'MESSAGE',
     'Warehouse code is already in use',     'Warehouse code is already in use',     'កូដឃ្លាំងនេះមានរួចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'INVENTORY_ITEM_NOT_FOUND',  'Inventory item not found',                '200', 'MESSAGE',
     'Inventory item not found',             'Inventory item not found',             'រកមិនឃើញទំនិញ serialized',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'DUPLICATE_VIN',             'Vehicle VIN already exists in stock',     '400', 'MESSAGE',
     'A vehicle with this VIN is already in stock', 'A vehicle with this VIN is already in stock', 'លេខតួ (VIN) នេះមានក្នុងស្តុកស្រេចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'DUPLICATE_SERIAL_NUMBER',   'Serial number already exists in stock',   '400', 'MESSAGE',
     'This serial number is already in stock', 'This serial number is already in stock', 'លេខស៊េរីនេះមានក្នុងស្តុកស្រេចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'DUPLICATE_ENGINE_NUMBER',   'Engine number already exists in stock',   '400', 'MESSAGE',
     'This engine number is already in stock', 'This engine number is already in stock', 'លេខម៉ាស៊ីននេះមានក្នុងស្តុកស្រេចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'INVALID_ITEM_STATUS',       'Invalid inventory item status transition','400', 'MESSAGE',
     'Invalid item status transition',       'Invalid item status transition',       'ការផ្លាស់ប្ដូរស្ថានភាពទំនិញមិនត្រឹមត្រូវ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'ITEM_NOT_AVAILABLE',        'Inventory item is not available',         '400', 'MESSAGE',
     'Inventory item is not available for this operation', 'Inventory item is not available for this operation', 'ទំនិញនេះមិនទំនេរសម្រាប់ប្រតិបត្តិការនេះទេ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'BATCH_NOT_FOUND',           'Inventory batch not found',               '200', 'MESSAGE',
     'Inventory batch not found',            'Inventory batch not found',            'រកមិនឃើញ batch នៃទំនិញ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'DUPLICATE_BATCH_NUMBER',    'Batch number already exists for variant', '400', 'MESSAGE',
     'Batch number already exists in this warehouse', 'Batch number already exists in this warehouse', 'លេខ batch នេះមានរួចហើយក្នុងឃ្លាំងនេះ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'INSUFFICIENT_STOCK',        'Insufficient stock quantity',             '400', 'MESSAGE',
     'Insufficient stock quantity',          'Insufficient stock quantity',          'ចំនួនស្តុកមិនគ្រប់គ្រាន់',
     'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
