-- ═══ Auto-renew (Card-on-File) scaffolding ══════════════════════════════════
-- Inert until payway.cof-enabled=true — ABA has not enabled Card-on-File for
-- the sopposstore merchant account yet, so no code path can reach these
-- columns with real data today.

ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS auto_renew BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS payment_token VARCHAR(255);
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS payment_token_captured_at TIMESTAMP;
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS auto_renew_failure_count INTEGER NOT NULL DEFAULT 0;
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS auto_renew_last_attempt_at TIMESTAMP;
CREATE INDEX IF NOT EXISTS idx_user_subscription_auto_renew ON public.user_subscription (auto_renew, expires_at);

ALTER TABLE public.payment_transaction ADD COLUMN IF NOT EXISTS initiated_by VARCHAR(12) NOT NULL DEFAULT 'USER';

-- ═══ Permissions ═════════════════════════════════════════════════════════
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
  (gen_random_uuid()::text, 'subscription.manage', 'Manage Subscriptions', 'CONTENT_MANAGEMENT', 56)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code = 'subscription.manage'
ON CONFLICT DO NOTHING;

-- ═══ Response codes ══════════════════════════════════════════════════════
INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0019', 'Auto-renew not available',      '501', 'MESSAGE', 'Auto-renewal is not available yet',              'Auto-renewal is not available yet',              'ការបន្តជាវដោយស្វ័យប្រវត្តិមិនទាន់អាចប្រើបានទេ',        'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0020', 'Auto-renew token missing',      '400', 'MESSAGE', 'No card on file yet for auto-renewal',           'No card on file yet for auto-renewal',           'មិនទាន់មានប័ណ្ណទូទាត់សម្រាប់ការបន្តជាវដោយស្វ័យប្រវត្តិទេ', 'ERR', 'ALL', 'ACT'),
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0021', 'Active subscription not found', '404', 'MESSAGE', 'No active subscription found',                   'No active subscription found',                   'រកមិនឃើញការជាវដែលកំពុងដំណើរការទេ',                   'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
