-- Seed default SMTP settings
INSERT INTO public.cms_settings (setting_id, setting_key, setting_value, setting_group) VALUES
    (gen_random_uuid()::text, 'smtp_host',        'mail.softpoint.com.kh', 'SMTP'),
    (gen_random_uuid()::text, 'smtp_port',        '587',                   'SMTP'),
    (gen_random_uuid()::text, 'smtp_username',    'soppos@softpoint.com.kh', 'SMTP'),
    (gen_random_uuid()::text, 'smtp_password',    'g0%cJ]~6p-Z7',          'SMTP'),
    (gen_random_uuid()::text, 'smtp_from_email',  'soppos@softpoint.com.kh', 'SMTP'),
    (gen_random_uuid()::text, 'smtp_from_name',   'SOPPOS System',         'SMTP'),
    (gen_random_uuid()::text, 'smtp_encryption',  'STARTTLS',              'SMTP'),
    (gen_random_uuid()::text, 'smtp_auth',        'true',                  'SMTP')
ON CONFLICT (setting_key) DO NOTHING;
