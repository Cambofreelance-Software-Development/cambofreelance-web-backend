INSERT INTO public.cms_settings (setting_id, setting_key, setting_value, setting_group) VALUES
    (gen_random_uuid()::text, 'ip_whitelist_enabled', 'false', 'IP_WHITELIST'),
    (gen_random_uuid()::text, 'ip_whitelist_rules',   '',      'IP_WHITELIST');
