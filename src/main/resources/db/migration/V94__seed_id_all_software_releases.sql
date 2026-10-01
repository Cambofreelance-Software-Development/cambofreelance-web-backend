-- Sample ID-ALL software releases (Windows + macOS) so the admin page and the public
-- "Software for ID-ALL" section have data. Attach real installers from the admin panel
-- (Upload Software for ID-ALL -> edit -> choose file); no download file is linked yet.
INSERT INTO public.app_releases
    (id, product_key, app_name, platform, version_name, version_code, file_size, min_os_version,
     release_notes, release_notes_kh, force_update, release_date, created_by, status)
SELECT gen_random_uuid()::text, 'ID_ALL', v.app_name, v.platform, v.version_name, v.version_code, v.file_size,
       v.min_os_version, v.release_notes, v.release_notes_kh, FALSE, v.release_date::date, 'SYS', 'ACT'
FROM (VALUES
    ('ID-ALL', 'WINDOWS', '1.2.0', 12, '85 MB', 'Windows 10',
     E'- Faster card template loading\n- Improved printer detection\n- Bug fixes and stability improvements',
     E'- ផ្ទុកគំរូកាតកាន់តែលឿន\n- កែលម្អការរកឃើញម៉ាស៊ីនបោះពុម្ព\n- ដោះស្រាយបញ្ហា និងបង្កើនស្ថេរភាព',
     '2026-09-15'),
    ('ID-ALL', 'WINDOWS', '1.1.0', 11, '82 MB', 'Windows 10',
     E'- Added batch card printing\n- UI improvements',
     E'- បន្ថែមការបោះពុម្ពកាតជាបាច់\n- កែលម្អចំណុចប្រទាក់អ្នកប្រើ',
     '2026-07-01'),
    ('ID-ALL', 'MACOS', '1.2.0', 12, '92 MB', 'macOS 12',
     E'- Faster card template loading\n- Improved printer detection\n- Bug fixes and stability improvements',
     E'- ផ្ទុកគំរូកាតកាន់តែលឿន\n- កែលម្អការរកឃើញម៉ាស៊ីនបោះពុម្ព\n- ដោះស្រាយបញ្ហា និងបង្កើនស្ថេរភាព',
     '2026-09-15')
) AS v(app_name, platform, version_name, version_code, file_size, min_os_version, release_notes, release_notes_kh, release_date)
WHERE NOT EXISTS (
    SELECT 1 FROM public.app_releases r
    WHERE r.product_key = 'ID_ALL' AND r.platform = v.platform AND r.version_name = v.version_name
);
