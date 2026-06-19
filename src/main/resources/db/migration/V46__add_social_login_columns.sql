-- Make phone_number and password nullable for social login users
ALTER TABLE public.users
    ALTER COLUMN phone_number DROP NOT NULL,
ALTER COLUMN password DROP NOT NULL;

ALTER TABLE public.users
    ADD COLUMN IF NOT EXISTS social_provider VARCHAR(50),
    ADD COLUMN IF NOT EXISTS social_provider_id VARCHAR(255);