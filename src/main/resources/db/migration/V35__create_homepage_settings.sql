-- Homepage CMS: hero, "empowering businesses" metrics band + 3 lifestyle photos,
-- "get the help you need" 3-card block, and "become a SOPPOS Partner" CTA band.
-- Structured lists (tabbed modules, products, business types, testimonials,
-- FAQs, footer menus) are introduced in later migrations.
INSERT INTO public.cms_settings (setting_id, setting_key, setting_value, setting_group) VALUES
    -- Hero
    (gen_random_uuid()::text, 'home_hero_title',              'Free Point of Sale and Inventory Management Software', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_hero_title_kh',           '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_hero_subtitle',           'Turn your smartphone or tablet into a powerful POS. Manage sales, inventory and employees with ease; engage customers and increase your revenue.', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_hero_subtitle_kh',        '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_hero_cta_label',          'GET STARTED', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_hero_cta_label_kh',       '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_hero_cta_link',           '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_hero_video_label',        'Watch video', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_hero_video_label_kh',     '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_hero_video_url',          '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_hero_image_url',          '', 'HOMEPAGE'),

    -- "Empowering businesses worldwide" band
    (gen_random_uuid()::text, 'home_metrics_title',           'Empowering businesses worldwide', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_metrics_title_kh',        '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_metrics_subtitle',        'Over 1,000,000 businesses in 170 countries have registered with SOPPOS POS. Our apps are available in 30+ languages.', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_metrics_subtitle_kh',     '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_lifestyle_image_1_url',   '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_lifestyle_image_2_url',   '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_lifestyle_image_3_url',   '', 'HOMEPAGE'),

    -- "Get the help you need" block heading
    (gen_random_uuid()::text, 'home_help_title',              'Get the help you need', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_title_kh',           '', 'HOMEPAGE'),

    -- Help card 1: Live chat
    (gen_random_uuid()::text, 'home_help_livechat_title',           'Live chat', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_livechat_title_kh',        '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_livechat_description',     'Get lightning fast assistance 24/7 from our friendly customer support team.', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_livechat_description_kh',  '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_livechat_cta_label',       'Start Chatting', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_livechat_cta_label_kh',    '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_livechat_cta_link',        '', 'HOMEPAGE'),

    -- Help card 2: Help center
    (gen_random_uuid()::text, 'home_help_center_title',           'Help center', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_center_title_kh',        '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_center_description',     'A comprehensive set of step-by-step guides in 17 languages with video tutorials.', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_center_description_kh',  '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_center_cta_label',       'Go to Help Center', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_center_cta_label_kh',    '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_center_cta_link',        '', 'HOMEPAGE'),

    -- Help card 3: Community
    (gen_random_uuid()::text, 'home_help_community_title',           'Community', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_community_title_kh',        '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_community_description',     'Learn from other sellers how to use SOPPOS and grow your business.', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_community_description_kh',  '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_community_cta_label',       'Join Community', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_community_cta_label_kh',    '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_help_community_cta_link',        '', 'HOMEPAGE'),

    -- "Become a SOPPOS Partner" CTA band
    (gen_random_uuid()::text, 'home_partner_title',           'Become a SOPPOS Partner', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_partner_title_kh',        '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_partner_description',     'Let''s grow together and help other businesses grow. If you would like to serve local businesses and help them implement software and hardware retail solutions, join our partner program today.', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_partner_description_kh',  '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_partner_cta_label',       'Join the Partnership', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_partner_cta_label_kh',    '', 'HOMEPAGE'),
    (gen_random_uuid()::text, 'home_partner_cta_link',        '', 'HOMEPAGE')
ON CONFLICT (setting_key) DO NOTHING;
