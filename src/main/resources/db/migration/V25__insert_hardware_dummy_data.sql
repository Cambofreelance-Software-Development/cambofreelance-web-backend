-- Seed hardware categories
INSERT INTO public.category_hardware (id, name, name_kh, sort_order) VALUES
    (gen_random_uuid()::text, 'POS Printers',     'ម៉ាស៊ីនព្រីនវិក្កយបត្រ', 1),
    (gen_random_uuid()::text, 'Barcode Scanners', 'ម៉ាស៊ីនស្កេនកូដ',       2),
    (gen_random_uuid()::text, 'Cash Drawers',     'ថតដាក់លុយ',             3),
    (gen_random_uuid()::text, 'Card Readers',     'ម៉ាស៊ីនអានកាត',          4)
ON CONFLICT (name) DO NOTHING;

-- Seed hardware products, linked to categories by name
INSERT INTO public.hardware (id, name, name_kh, brand, description, description_kh, connectivity, category_id, icon, link, sort_order)
SELECT gen_random_uuid()::text, v.name, v.name_kh, v.brand, v.description, v.description_kh, v.connectivity, c.id, v.icon, v.link, v.sort_order
FROM (VALUES
    -- POS Printers
    ('Thermal Receipt Printer T88', 'ម៉ាស៊ីនព្រីនកម្ដៅ T88', 'Epson',
     'Fast, reliable thermal receipt printer for high-volume counters.',
     'ម៉ាស៊ីនព្រីនកម្ដៅ លឿន និងអាចទុកចិត្តបាន សម្រាប់បញ្ជរដែលមានការលក់ច្រើន។',
     'USB,Ethernet', 'POS Printers', 'receipt', NULL, 1),
    ('Compact POS Printer mPOP', 'ម៉ាស៊ីនព្រីន mPOP', 'Star Micronics',
     'All-in-one printer and cash drawer combo with a small footprint.',
     'ម៉ាស៊ីនព្រីន និងថតលុយរួមបញ្ចូលគ្នា ក្នុងទំហំតូច។',
     'Bluetooth,USB', 'POS Printers', 'receipt', NULL, 2),
    -- Barcode Scanners
    ('Handheld Barcode Scanner DS2208', 'ម៉ាស៊ីនស្កេនកូដ DS2208', 'Zebra',
     'Durable 2D imager that scans printed and on-screen barcodes.',
     'ម៉ាស៊ីនស្កេន 2D ដ៏រឹងមាំ ស្កេនកូដទាំងលើក្រដាស និងលើអេក្រង់។',
     'USB', 'Barcode Scanners', 'shopping-cart', NULL, 1),
    ('Wireless Scanner S740', 'ម៉ាស៊ីនស្កេនឥតខ្សែ S740', 'Socket Mobile',
     'Pocket-sized Bluetooth scanner for mobile and tablet checkout.',
     'ម៉ាស៊ីនស្កេន Bluetooth ទំហំហោប៉ៅ សម្រាប់ការគិតលុយលើទូរស័ព្ទ និងថេប្លេត។',
     'Bluetooth', 'Barcode Scanners', 'wifi', NULL, 2),
    -- Cash Drawers
    ('Heavy-Duty Cash Drawer CD4', 'ថតដាក់លុយ CD4', 'Star Micronics',
     'Sturdy metal cash drawer with removable coin and note tray.',
     'ថតដាក់លុយដែកដ៏រឹងមាំ ជាមួយថាសកាក់ និងក្រដាសប្រាក់ដកចេញបាន។',
     'Ethernet', 'Cash Drawers', 'package', NULL, 1),
    -- Card Readers
    ('Contactless Card Reader', 'ម៉ាស៊ីនអានកាតឥតប៉ះ', 'SumUp',
     'Accept chip, contactless, and mobile-wallet payments anywhere.',
     'ទទួលការទូទាត់តាមឈីប ឥតប៉ះ និងកាបូបចល័ត នៅគ្រប់ទីកន្លែង។',
     'Bluetooth,Wi-Fi', 'Card Readers', 'credit-card', NULL, 1)
) AS v(name, name_kh, brand, description, description_kh, connectivity, category_name, icon, link, sort_order)
JOIN public.category_hardware c ON c.name = v.category_name;
