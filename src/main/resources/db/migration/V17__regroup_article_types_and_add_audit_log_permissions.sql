-- Move article-type permissions into their own group so they appear as a
-- separate section in the role permission picker.
UPDATE public.permissions
SET group_name = 'ARTICLE_TYPE_MANAGEMENT'
WHERE code IN (
    'article-types.view',
    'article-types.create',
    'article-types.update',
    'article-types.delete'
);

-- Audit log permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'audit-log.view',   'View Audit Logs',   'AUDIT_LOG', 60),
  (gen_random_uuid()::text, 'audit-log.export', 'Export Audit Logs', 'AUDIT_LOG', 61),
  (gen_random_uuid()::text, 'audit-log.purge',  'Purge Audit Logs',  'AUDIT_LOG', 62)
ON CONFLICT (code) DO NOTHING;
