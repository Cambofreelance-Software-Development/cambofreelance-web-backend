-- Release/launch date shown on hardware cards on the /en/hardware page
ALTER TABLE public.hardware
    ADD COLUMN IF NOT EXISTS release_date DATE;
