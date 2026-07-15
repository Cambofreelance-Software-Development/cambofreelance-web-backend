-- Seed the Partner CTA section shown at the bottom of the /en and /km home page.
-- Defaults mirror the strings currently hardcoded in PartnerCtaSection.tsx so the
-- section renders identically until an admin edits them from the CMS.

INSERT INTO public.cms_settings (setting_id, setting_key, setting_value, setting_group) VALUES
    (gen_random_uuid()::text, 'partner_cta_title',       'Become a SOPPOS Partner', 'PARTNER_CTA'),
    (gen_random_uuid()::text, 'partner_cta_title_kh',    'ក្លាយជាដៃគូ SOPPOS',        'PARTNER_CTA'),
    (gen_random_uuid()::text, 'partner_cta_body',        'Let''s grow together and help other businesses grow. If you would like to serve local businesses and help them implement software and hardware retail solutions, join our partner program today.', 'PARTNER_CTA'),
    (gen_random_uuid()::text, 'partner_cta_body_kh',     'តោះរីកចម្រើនជាមួយគ្នា ហើយជួយអាជីវកម្មដទៃទៀតឲ្យរីកចម្រើន។ បើអ្នកចង់បម្រើអាជីវកម្មក្នុងស្រុក និងជួយពួកគេអនុវត្តដំណោះស្រាយសូហ្វវែរ និងហាតវែរ សូមចូលរួមកម្មវិធីភាពជាដៃគូរបស់យើងថ្ងៃនេះ។', 'PARTNER_CTA'),
    (gen_random_uuid()::text, 'partner_cta_label',       'Join the Partnership',    'PARTNER_CTA'),
    (gen_random_uuid()::text, 'partner_cta_label_kh',    'ចូលរួមភាពជាដៃគូ',           'PARTNER_CTA'),
    (gen_random_uuid()::text, 'partner_cta_link',        '/partner',                'PARTNER_CTA')
ON CONFLICT (setting_key) DO NOTHING;
