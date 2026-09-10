-- ═══ SOP POS System — client provisioning integration ══════════════════════
-- When a subscription activates, renews, or changes plan, the customer's tenant
-- in the SOP POS System is created (POST /api/registration) or updated
-- (PATCH /api/registration/{id}). Inert until soppos.enabled=true + base-url set.

-- Maps a pricing plan to the SOP POS plan tier code. Valid codes: 0,9,19,25,39,99,150,200,250.
ALTER TABLE public.pricing_plan ADD COLUMN IF NOT EXISTS pos_plan_code SMALLINT;

-- Seed the existing plans from their monthly price, which already keys the pricing
-- catalog (comparison rows / bullets join on price_monthly — see V28).
UPDATE public.pricing_plan
   SET pos_plan_code = price_monthly::int
 WHERE pos_plan_code IS NULL
   AND price_monthly::int IN (0, 9, 19, 25, 39, 99, 150, 200, 250);

-- Provisioning state, one POS tenant per subscription.
-- pos_registration_id = the UUID sent as `id` on the original POST; later PATCH calls
-- address /api/registration/{pos_registration_id}. Carried forward when a lapsed
-- subscriber re-subscribes so the same tenant is reactivated, not duplicated.
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS pos_registration_id VARCHAR(64);
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS pos_client_code     VARCHAR(32);
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS pos_backend_url     VARCHAR(255);
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS pos_emenu_url       VARCHAR(255);
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS pos_root_user       VARCHAR(128);
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS pos_root_password   VARCHAR(255);
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS pos_sync_status     VARCHAR(16);
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS pos_sync_error      VARCHAR(500);
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS pos_synced_at       TIMESTAMP;
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS pos_last_attempt_at TIMESTAMP;

CREATE INDEX IF NOT EXISTS idx_user_subscription_pos_sync
    ON public.user_subscription (pos_sync_status);

-- ═══ Response code ════════════════════════════════════════════════════════
INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0038', 'POS provisioning failed', '502', 'MESSAGE', 'Could not provision the POS system for this subscription, it will be retried automatically', 'Could not provision the POS system for this subscription, it will be retried automatically', 'មិនអាចបង្កើតប្រព័ន្ធ POS សម្រាប់ការជាវនេះបានទេ ប្រព័ន្ធនឹងព្យាយាមម្ដងទៀតដោយស្វ័យប្រវត្តិ', 'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
