-- Taxonomy management permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'taxonomy.view',   'View Taxonomy',          'TAXONOMY_MANAGEMENT', 80),
  (gen_random_uuid()::text, 'taxonomy.create', 'Create Taxonomy',        'TAXONOMY_MANAGEMENT', 81),
  (gen_random_uuid()::text, 'taxonomy.update', 'Update Taxonomy',        'TAXONOMY_MANAGEMENT', 82),
  (gen_random_uuid()::text, 'taxonomy.delete', 'Delete Taxonomy',        'TAXONOMY_MANAGEMENT', 83),
  (gen_random_uuid()::text, 'taxonomy-item.view',   'View Taxonomy Items',   'TAXONOMY_MANAGEMENT', 84),
  (gen_random_uuid()::text, 'taxonomy-item.create', 'Create Taxonomy Items', 'TAXONOMY_MANAGEMENT', 85),
  (gen_random_uuid()::text, 'taxonomy-item.update', 'Update Taxonomy Items', 'TAXONOMY_MANAGEMENT', 86),
  (gen_random_uuid()::text, 'taxonomy-item.delete', 'Delete Taxonomy Items', 'TAXONOMY_MANAGEMENT', 87)
ON CONFLICT (code) DO NOTHING;

-- Auto-assign all taxonomy permissions to the ADMIN role
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code = 'ADMIN'
  AND p.code IN (
    'taxonomy.view',
    'taxonomy.create',
    'taxonomy.update',
    'taxonomy.delete',
    'taxonomy-item.view',
    'taxonomy-item.create',
    'taxonomy-item.update',
    'taxonomy-item.delete'
  )
ON CONFLICT DO NOTHING;
