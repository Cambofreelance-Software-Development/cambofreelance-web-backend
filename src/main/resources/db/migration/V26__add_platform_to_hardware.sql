-- Platform compatibility for hardware (comma-separated: iOS, Android)
ALTER TABLE public.hardware ADD COLUMN IF NOT EXISTS platform VARCHAR(100);

-- Backfill dummy data
UPDATE public.hardware SET platform = 'iOS,Android' WHERE name = 'Thermal Receipt Printer T88';
UPDATE public.hardware SET platform = 'iOS,Android' WHERE name = 'Compact POS Printer mPOP';
UPDATE public.hardware SET platform = 'Android'     WHERE name = 'Handheld Barcode Scanner DS2208';
UPDATE public.hardware SET platform = 'iOS,Android' WHERE name = 'Wireless Scanner S740';
UPDATE public.hardware SET platform = 'iOS,Android' WHERE name = 'Heavy-Duty Cash Drawer CD4';
UPDATE public.hardware SET platform = 'iOS,Android' WHERE name = 'Contactless Card Reader';
