INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'article-types.view',   'View Article Types',   'ARTICLE_MANAGEMENT', 35),
  (gen_random_uuid()::text, 'article-types.create', 'Create Article Types', 'ARTICLE_MANAGEMENT', 36),
  (gen_random_uuid()::text, 'article-types.update', 'Update Article Types', 'ARTICLE_MANAGEMENT', 37),
  (gen_random_uuid()::text, 'article-types.delete', 'Delete Article Types', 'ARTICLE_MANAGEMENT', 38),
  (gen_random_uuid()::text, 'media.confirm',        'Confirm Media Upload', 'MEDIA',              43),
  (gen_random_uuid()::text, 'sessions.view',        'View User Sessions',   'USER_MANAGEMENT',    15),
  (gen_random_uuid()::text, 'sessions.revoke',      'Revoke User Sessions', 'USER_MANAGEMENT',    16)
ON CONFLICT (code) DO NOTHING;
