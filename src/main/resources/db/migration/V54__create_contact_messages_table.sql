CREATE TABLE IF NOT EXISTS public.contact_messages (
    id          VARCHAR(36)   PRIMARY KEY,
    full_name   VARCHAR(100)  NOT NULL,
    email       VARCHAR(255)  NOT NULL,
    subject     VARCHAR(200)  NOT NULL,
    message     TEXT          NOT NULL,
    ip_address  VARCHAR(64),
    is_read     BOOLEAN       NOT NULL DEFAULT FALSE,
    created_by  VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(255),
    updated_at  TIMESTAMP,
    status      VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_contact_messages_status     ON public.contact_messages (status);
CREATE INDEX IF NOT EXISTS idx_contact_messages_is_read    ON public.contact_messages (is_read);
CREATE INDEX IF NOT EXISTS idx_contact_messages_created_at ON public.contact_messages (created_at);

-- Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'contact-messages.view',   'View Contact Messages',   'SETTINGS', 90),
  (gen_random_uuid()::text, 'contact-messages.update', 'Update Contact Messages', 'SETTINGS', 91),
  (gen_random_uuid()::text, 'contact-messages.delete', 'Delete Contact Messages', 'SETTINGS', 92)
ON CONFLICT (code) DO NOTHING;

-- Auto-assign to ADMIN role
INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('contact-messages.view', 'contact-messages.update', 'contact-messages.delete')
ON CONFLICT DO NOTHING;
