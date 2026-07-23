-- A "Feature Tab" (the tab-strip button) can now hold multiple stacked items,
-- each with its own title/subtitle/image/CTA/bullets (e.g. one "POS System" tab
-- listing Point of sale, Payments, Inventory management, ... one after another).
-- Previously a cms_feature_tabs row *was* a single item; this migration moves
-- that content into a new child table and repoints the existing bullets to it.

CREATE TABLE IF NOT EXISTS public.cms_feature_tab_items (
    id            VARCHAR(36)  PRIMARY KEY,
    tab_id        VARCHAR(36)  NOT NULL REFERENCES public.cms_feature_tabs(id) ON DELETE CASCADE,
    title         VARCHAR(255) NOT NULL,
    title_kh      VARCHAR(255),
    subtitle      TEXT,
    subtitle_kh   TEXT,
    cta_label     VARCHAR(255),
    cta_label_kh  VARCHAR(255),
    cta_href      VARCHAR(500),
    cta_button    BOOLEAN      NOT NULL DEFAULT FALSE,
    image_url     VARCHAR(500),
    image_side    VARCHAR(10)  NOT NULL DEFAULT 'right',
    sort_order    INTEGER      NOT NULL DEFAULT 0,
    created_by    VARCHAR(255) NOT NULL DEFAULT 'SYS',
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by    VARCHAR(255),
    updated_at    TIMESTAMP,
    status        VARCHAR(3)   NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_feature_tab_items_tab_id     ON public.cms_feature_tab_items (tab_id);
CREATE INDEX IF NOT EXISTS idx_feature_tab_items_status     ON public.cms_feature_tab_items (status);
CREATE INDEX IF NOT EXISTS idx_feature_tab_items_sort_order ON public.cms_feature_tab_items (sort_order);

-- Carry each existing tab's content into a single item (sort_order 1) so
-- current data keeps rendering exactly as before.
INSERT INTO public.cms_feature_tab_items (id, tab_id, title, title_kh, subtitle, subtitle_kh,
    cta_label, cta_label_kh, cta_href, cta_button, image_url, image_side, sort_order,
    created_by, created_at, updated_by, updated_at, status)
SELECT gen_random_uuid()::text, id, title, title_kh, subtitle, subtitle_kh,
       cta_label, cta_label_kh, cta_href, cta_button, image_url, image_side, 1,
       created_by, created_at, updated_by, updated_at, status
FROM public.cms_feature_tabs;

-- Repoint bullets from the tab to the new item that carries its old content.
ALTER TABLE public.cms_feature_tab_bullets ADD COLUMN item_id VARCHAR(36);

UPDATE public.cms_feature_tab_bullets b
SET item_id = i.id
FROM public.cms_feature_tab_items i
WHERE i.tab_id = b.tab_id;

ALTER TABLE public.cms_feature_tab_bullets DROP CONSTRAINT IF EXISTS cms_feature_tab_bullets_tab_id_fkey;
ALTER TABLE public.cms_feature_tab_bullets ALTER COLUMN item_id SET NOT NULL;
ALTER TABLE public.cms_feature_tab_bullets ADD CONSTRAINT fk_feature_tab_bullets_item
    FOREIGN KEY (item_id) REFERENCES public.cms_feature_tab_items(id) ON DELETE CASCADE;

DROP INDEX IF EXISTS idx_feature_tab_bullets_tab_id;
CREATE INDEX IF NOT EXISTS idx_feature_tab_bullets_item_id ON public.cms_feature_tab_bullets (item_id);

ALTER TABLE public.cms_feature_tab_bullets DROP COLUMN tab_id;

-- Content now lives on cms_feature_tab_items; the tab row only drives the tab strip.
ALTER TABLE public.cms_feature_tabs
    DROP COLUMN title,
    DROP COLUMN title_kh,
    DROP COLUMN subtitle,
    DROP COLUMN subtitle_kh,
    DROP COLUMN cta_label,
    DROP COLUMN cta_label_kh,
    DROP COLUMN cta_href,
    DROP COLUMN cta_button,
    DROP COLUMN image_url,
    DROP COLUMN image_side;
