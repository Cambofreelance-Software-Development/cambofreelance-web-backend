-- "Join the Partnership" button now routes to the contact page
UPDATE public.cms_settings
SET setting_value = '/contact'
WHERE setting_key = 'partner_cta_link';
