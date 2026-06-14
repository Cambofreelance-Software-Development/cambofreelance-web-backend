ALTER TABLE public.user_refresh_tokens
    ADD COLUMN IF NOT EXISTS ip_address     VARCHAR(64),
    ADD COLUMN IF NOT EXISTS user_agent     TEXT,
    ADD COLUMN IF NOT EXISTS device_name    VARCHAR(200),
    ADD COLUMN IF NOT EXISTS device_type    VARCHAR(20)  DEFAULT 'UNKNOWN',
    ADD COLUMN IF NOT EXISTS browser        VARCHAR(100),
    ADD COLUMN IF NOT EXISTS os_name        VARCHAR(100),
    ADD COLUMN IF NOT EXISTS last_active_at TIMESTAMP,
    ADD COLUMN IF NOT EXISTS is_trusted     BOOLEAN      DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS is_new_device  BOOLEAN      DEFAULT FALSE;
CREATE INDEX idx_refresh_device ON public.user_refresh_tokens(user_id, device_id);
