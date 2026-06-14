-- Add Tutorial article type for step-by-step educational series (e.g., C++ Tutorial)
INSERT INTO public.article_types (id, code, label, label_kh, sort_order)
VALUES (gen_random_uuid(), 'TUTORIAL', 'Tutorial', 'ការបង្រៀន', 9)
ON CONFLICT (code) DO NOTHING;
