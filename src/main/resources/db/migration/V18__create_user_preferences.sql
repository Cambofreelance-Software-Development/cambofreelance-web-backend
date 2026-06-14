CREATE TABLE IF NOT EXISTS public.user_preferences (
    user_id     VARCHAR(255) PRIMARY KEY,
    prefs_json  TEXT         NOT NULL DEFAULT '{}',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
