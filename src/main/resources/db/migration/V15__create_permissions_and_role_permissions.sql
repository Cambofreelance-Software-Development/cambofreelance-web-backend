-- Add audit columns to roles (safe: IF NOT EXISTS)
ALTER TABLE public.roles ADD COLUMN IF NOT EXISTS created_at  TIMESTAMP DEFAULT NOW();
ALTER TABLE public.roles ADD COLUMN IF NOT EXISTS created_by  VARCHAR(100) DEFAULT 'SYS';
ALTER TABLE public.roles ADD COLUMN IF NOT EXISTS updated_at  TIMESTAMP;
ALTER TABLE public.roles ADD COLUMN IF NOT EXISTS updated_by  VARCHAR(100);

-- Permissions catalogue
CREATE TABLE IF NOT EXISTS public.permissions (
    id          VARCHAR(36)  PRIMARY KEY,
    code        VARCHAR(100) NOT NULL UNIQUE,
    name        VARCHAR(200) NOT NULL,
    group_name  VARCHAR(100),
    description TEXT,
    sort_order  INTEGER      DEFAULT 0,
    created_at  TIMESTAMP    DEFAULT NOW()
);

-- Role ↔ Permission join
CREATE TABLE IF NOT EXISTS public.role_permissions (
    role_id       VARCHAR(36) NOT NULL REFERENCES public.roles(id),
    permission_id VARCHAR(36) NOT NULL REFERENCES public.permissions(id),
    PRIMARY KEY (role_id, permission_id)
);

-- Seed default permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'users.view',        'View Users',             'USER_MANAGEMENT',    10),
  (gen_random_uuid()::text, 'users.create',       'Create Users',           'USER_MANAGEMENT',    11),
  (gen_random_uuid()::text, 'users.update',       'Update Users',           'USER_MANAGEMENT',    12),
  (gen_random_uuid()::text, 'users.delete',       'Delete Users',           'USER_MANAGEMENT',    13),
  (gen_random_uuid()::text, 'users.status',       'Toggle User Status',     'USER_MANAGEMENT',    14),
  (gen_random_uuid()::text, 'roles.view',         'View Roles',             'ROLE_MANAGEMENT',    20),
  (gen_random_uuid()::text, 'roles.create',       'Create Roles',           'ROLE_MANAGEMENT',    21),
  (gen_random_uuid()::text, 'roles.update',       'Update Roles',           'ROLE_MANAGEMENT',    22),
  (gen_random_uuid()::text, 'roles.delete',       'Delete Roles',           'ROLE_MANAGEMENT',    23),
  (gen_random_uuid()::text, 'roles.status',       'Toggle Role Status',     'ROLE_MANAGEMENT',    24),
  (gen_random_uuid()::text, 'roles.clone',        'Clone Roles',            'ROLE_MANAGEMENT',    25),
  (gen_random_uuid()::text, 'roles.permissions',  'Manage Role Permissions','ROLE_MANAGEMENT',    26),
  (gen_random_uuid()::text, 'articles.view',      'View Articles',          'ARTICLE_MANAGEMENT', 30),
  (gen_random_uuid()::text, 'articles.create',    'Create Articles',        'ARTICLE_MANAGEMENT', 31),
  (gen_random_uuid()::text, 'articles.update',    'Update Articles',        'ARTICLE_MANAGEMENT', 32),
  (gen_random_uuid()::text, 'articles.delete',    'Delete Articles',        'ARTICLE_MANAGEMENT', 33),
  (gen_random_uuid()::text, 'articles.publish',   'Publish Articles',       'ARTICLE_MANAGEMENT', 34),
  (gen_random_uuid()::text, 'media.view',         'View Media Library',     'MEDIA',              40),
  (gen_random_uuid()::text, 'media.upload',       'Upload Media',           'MEDIA',              41),
  (gen_random_uuid()::text, 'media.delete',       'Delete Media',           'MEDIA',              42),
  (gen_random_uuid()::text, 'settings.view',      'View Settings',          'SETTINGS',           50),
  (gen_random_uuid()::text, 'settings.update',    'Update Settings',        'SETTINGS',           51)
ON CONFLICT (code) DO NOTHING;
