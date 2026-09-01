-- New SMTP setting, editable from Admin Settings > Email, controlling whether the mail
-- client trusts the configured host's TLS cert even if it fails validation (expired/self-signed).
-- Seeded 'true' to match the temporary code-level workaround it replaces: mail.softpoint.com.kh's
-- cert has been expired since Dec 2024. Turn this off from the settings UI once it's renewed.
INSERT INTO public.cms_settings (setting_id, setting_key, setting_value, setting_group) VALUES
    (gen_random_uuid()::text, 'smtp_trust_invalid_cert', 'true', 'SMTP')
ON CONFLICT (setting_key) DO NOTHING;
