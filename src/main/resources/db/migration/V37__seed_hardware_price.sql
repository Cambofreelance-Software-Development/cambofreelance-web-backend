-- Populate the display price shown on hardware cards on the /en/hardware page.
-- price was introduced (as VARCHAR) in V36 but left NULL, so HardwareResponse
-- (@JsonInclude(NON_NULL)) omitted it and the page had nothing to show.
-- Rows are matched by their seeded name (see V25). The IS NULL guard keeps this
-- from overwriting any price later edited through the CMS.
UPDATE public.hardware SET price = '$189' WHERE name = 'Thermal Receipt Printer T88'     AND price IS NULL;
UPDATE public.hardware SET price = '$329' WHERE name = 'Compact POS Printer mPOP'          AND price IS NULL;
UPDATE public.hardware SET price = '$99'  WHERE name = 'Handheld Barcode Scanner DS2208'   AND price IS NULL;
UPDATE public.hardware SET price = '$249' WHERE name = 'Wireless Scanner S740'             AND price IS NULL;
UPDATE public.hardware SET price = '$129' WHERE name = 'Heavy-Duty Cash Drawer CD4'        AND price IS NULL;
UPDATE public.hardware SET price = '$39'  WHERE name = 'Contactless Card Reader'           AND price IS NULL;