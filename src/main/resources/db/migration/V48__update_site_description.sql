-- Rebrand: replace the old freelance-team site description with the SOPPOS redesign message
INSERT INTO public.cms_settings (setting_id, setting_key, setting_value, setting_group) VALUES
    (gen_random_uuid()::text,
     'site_description',
     'Our digital space is undergoing a major redesign to serve you better. We''re preparing to launch advanced Point-of-Sale (POS) management platforms and retail solutions very soon.',
     'GENERAL')
ON CONFLICT (setting_key) DO UPDATE SET setting_value = EXCLUDED.setting_value;
