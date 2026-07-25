-- Overview spec-table content for hardware products (separate from Full details).

ALTER TABLE public.hardware ADD COLUMN IF NOT EXISTS overview    TEXT;
ALTER TABLE public.hardware ADD COLUMN IF NOT EXISTS overview_kh TEXT;
