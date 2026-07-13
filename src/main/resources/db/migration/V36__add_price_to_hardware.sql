-- Display price shown on hardware cards on the /en/hardware page (kept as VARCHAR
-- because scraped values include currency symbols and phrases like "Starting from $99").
ALTER TABLE public.hardware
    ADD COLUMN IF NOT EXISTS price VARCHAR(50);


