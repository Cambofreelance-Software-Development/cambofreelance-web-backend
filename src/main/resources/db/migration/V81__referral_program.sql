-- ── Referral program ──────────────────────────────────────────────────────
-- Every user gets a shareable referral code; existing users are backfilled from their user_id.
ALTER TABLE public.users ADD COLUMN IF NOT EXISTS referral_code VARCHAR(20);
UPDATE public.users SET referral_code = UPPER(SUBSTRING(REPLACE(user_id, '-', ''), 1, 10))
WHERE referral_code IS NULL;
ALTER TABLE public.users ALTER COLUMN referral_code SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_referral_code ON public.users (referral_code);

-- Who referred this user in — set once, at registration, from the referral code they entered.
ALTER TABLE public.users ADD COLUMN IF NOT EXISTS referred_by VARCHAR(255) REFERENCES public.users(user_id);
CREATE INDEX IF NOT EXISTS idx_users_referred_by ON public.users (referred_by);

-- Referrer snapshot on the subscription — set once at first checkout from users.referred_by,
-- and stays fixed for the subscription's lifetime even if the user's referred_by changes later.
ALTER TABLE public.user_subscription ADD COLUMN IF NOT EXISTS referrer_id VARCHAR(255) REFERENCES public.users(user_id);
CREATE INDEX IF NOT EXISTS idx_user_subscription_referrer ON public.user_subscription (referrer_id);

-- Referrer copied onto every payment transaction (checkout, renewal, upgrade, auto-renew) —
-- denormalized so a future commission job can query payments directly without joining back
-- through the subscription/user chain.
ALTER TABLE public.payment_transaction ADD COLUMN IF NOT EXISTS referrer_id VARCHAR(255) REFERENCES public.users(user_id);
CREATE INDEX IF NOT EXISTS idx_payment_transaction_referrer ON public.payment_transaction (referrer_id);

-- ── Response codes ─────────────────────────────────────────────────────────
INSERT INTO public.response_codes (created_at, created_by, updated_at, updated_by, code, description, http_status, key, message_en, message_cn, message_km, type, service_type, status)
VALUES
    (NOW(), 'SYS', NOW(), 'SYS', 'ERR-0028', 'Invalid referral code', '400', 'MESSAGE', 'Invalid referral code', 'Invalid referral code', 'កូដណែនាំមិនត្រឹមត្រូវទេ', 'ERR', 'ALL', 'ACT')
ON CONFLICT (code) DO NOTHING;
