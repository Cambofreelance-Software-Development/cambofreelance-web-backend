-- ── In-app admin notifications ────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS public.admin_notifications (
    id             VARCHAR(36)   PRIMARY KEY,
    type           VARCHAR(50)   NOT NULL,                    -- e.g. SUBSCRIPTION_EXPIRING
    title          VARCHAR(200)  NOT NULL,
    message        TEXT          NOT NULL,
    reference_id   VARCHAR(36),                                -- e.g. user_subscription.id
    reference_type VARCHAR(30),                                -- e.g. SUBSCRIPTION
    is_read        BOOLEAN       NOT NULL DEFAULT FALSE,
    created_by     VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by     VARCHAR(255),
    updated_at     TIMESTAMP,
    status         VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);
CREATE INDEX IF NOT EXISTS idx_admin_notifications_status     ON public.admin_notifications (status);
CREATE INDEX IF NOT EXISTS idx_admin_notifications_is_read    ON public.admin_notifications (is_read);
CREATE INDEX IF NOT EXISTS idx_admin_notifications_created_at ON public.admin_notifications (created_at);
CREATE INDEX IF NOT EXISTS idx_admin_notifications_reference  ON public.admin_notifications (reference_type, reference_id);

-- ── Subscription expiry-reminder threshold tracking ───────────────────────
-- One-shot flags so the daily reminder job never re-notifies the same subscription
-- at a threshold it already crossed (reset on renewal in SubscriptionServiceImpl#activateSubscription).
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS notice_7d_sent BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS notice_3d_sent BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS notice_1d_sent BOOLEAN NOT NULL DEFAULT FALSE;
CREATE INDEX IF NOT EXISTS idx_user_subscription_expiry_notice ON public.user_subscription (sub_status, expires_at);

-- ── Permissions ────────────────────────────────────────────────────────────
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'notifications.view',   'View Notifications',   'NOTIFICATIONS', 60),
  (gen_random_uuid()::text, 'notifications.update', 'Update Notifications', 'NOTIFICATIONS', 61),
  (gen_random_uuid()::text, 'notifications.delete', 'Delete Notifications', 'NOTIFICATIONS', 62)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('notifications.view', 'notifications.update', 'notifications.delete')
ON CONFLICT DO NOTHING;

-- ── Response codes ─────────────────────────────────────────────────────────
INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0026', 'Notification not found', '404', 'MESSAGE', 'Notification not found', 'Notification not found', 'រកមិនឃើញការជូនដំណឹងទេ', 'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
