-- Make phone_number and password nullable for social login users
ALTER TABLE users
    ALTER COLUMN phone_number DROP NOT NULL,
    ALTER COLUMN password DROP NOT NULL;

-- Add social provider tracking columns
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS social_provider VARCHAR(50),
    ADD COLUMN IF NOT EXISTS social_provider_id VARCHAR(255);

CREATE INDEX IF NOT EXISTS idx_users_social ON users (social_provider, social_provider_id);