-- Permissions and response codes for Inventory Products & Attributes.

-- 1. Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'inventory.products.view',     'View Products & Catalog',         'INVENTORY_MANAGEMENT', 200),
  (gen_random_uuid()::text, 'inventory.products.create',   'Create Product & Variants',       'INVENTORY_MANAGEMENT', 201),
  (gen_random_uuid()::text, 'inventory.products.update',   'Update Product & Pricing',        'INVENTORY_MANAGEMENT', 202),
  (gen_random_uuid()::text, 'inventory.products.delete',   'Deactivate/Delete Product',       'INVENTORY_MANAGEMENT', 203),
  (gen_random_uuid()::text, 'inventory.attributes.view',   'View Catalog Attributes',         'INVENTORY_MANAGEMENT', 204),
  (gen_random_uuid()::text, 'inventory.attributes.manage', 'Manage Attributes & Presets',     'INVENTORY_MANAGEMENT', 205)
ON CONFLICT (code) DO NOTHING;

-- Grant to SUPER_ADMIN and TENANT_ADMIN
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('SUPER_ADMIN', 'TENANT_ADMIN')
  AND p.code IN (
    'inventory.products.view',
    'inventory.products.create',
    'inventory.products.update',
    'inventory.products.delete',
    'inventory.attributes.view',
    'inventory.attributes.manage'
  )
ON CONFLICT DO NOTHING;

-- 2. Domain Response Codes for Inventory Product Management
INSERT INTO public.response_codes
    (created_at, created_by, updated_at, updated_by,
     code, description, http_status, key,
     message_en, message_cn, message_km,
     type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS',
     'PRODUCT_NOT_FOUND',         'Product not found',                       '200', 'MESSAGE',
     'Product not found',                    'Product not found',                    'រកមិនឃើញទំនិញ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'DUPLICATE_PRODUCT_SKU',     'Product SKU already exists',              '400', 'MESSAGE',
     'Product SKU is already in use',        'Product SKU is already in use',        'កូដ SKU ទំនិញនេះមានរួចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'DUPLICATE_PRODUCT_BARCODE', 'Product barcode already exists',          '400', 'MESSAGE',
     'Product barcode is already in use',    'Product barcode is already in use',    'បារកូដទំនិញនេះមានរួចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'INVALID_PRODUCT_STATUS',    'Invalid product status transition',       '400', 'MESSAGE',
     'Invalid product status transition',    'Invalid product status transition',    'ស្ថានភាពទំនិញមិនត្រឹមត្រូវ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'VARIANT_NOT_FOUND',         'Product variant not found',               '200', 'MESSAGE',
     'Product variant not found',            'Product variant not found',            'រកមិនឃើញ variant នៃទំនិញ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'DUPLICATE_VARIANT_SKU',     'Variant SKU already exists',              '400', 'MESSAGE',
     'Variant SKU is already in use',        'Variant SKU is already in use',        'កូដ SKU នៃ variant នេះមានរួចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'DUPLICATE_VARIANT_BARCODE', 'Variant barcode already exists',          '400', 'MESSAGE',
     'Variant barcode is already in use',    'Variant barcode is already in use',    'បារកូដនៃ variant នេះមានរួចហើយ',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'ATTRIBUTE_NOT_FOUND',       'Attribute not found',                     '200', 'MESSAGE',
     'Attribute not found',                  'Attribute not found',                  'រកមិនឃើញ attribute',
     'ERR', 'ALL', 'ACT'),

    (NOW(), 'SYS', NOW(), 'SYS',
     'DUPLICATE_ATTRIBUTE_CODE',  'Attribute code already exists',           '400', 'MESSAGE',
     'Attribute code is already in use',     'Attribute code is already in use',     'កូដ attribute នេះមានរួចហើយ',
     'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
