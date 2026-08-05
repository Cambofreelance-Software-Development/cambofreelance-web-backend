-- Help Center category tree: Article Type -> Category (unlimited nesting via parent_id).
CREATE TABLE IF NOT EXISTS public.help_center_categories (
    id               VARCHAR(36)   PRIMARY KEY,
    article_type_id  VARCHAR(36)   NOT NULL,
    parent_id        VARCHAR(36),
    name             VARCHAR(150)  NOT NULL,
    name_kh          VARCHAR(150),
    slug             VARCHAR(180)  NOT NULL,
    description      TEXT,
    description_kh   TEXT,
    icon             VARCHAR(100),
    display_order    INTEGER       NOT NULL DEFAULT 0,
    created_by       VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by       VARCHAR(255),
    updated_at       TIMESTAMP,
    status           VARCHAR(3)    NOT NULL DEFAULT 'ACT',
    CONSTRAINT uq_hc_categories_slug     UNIQUE (slug),
    CONSTRAINT fk_hc_categories_type     FOREIGN KEY (article_type_id) REFERENCES article_types (id),
    CONSTRAINT fk_hc_categories_parent   FOREIGN KEY (parent_id)       REFERENCES help_center_categories (id) ON DELETE CASCADE,
    CONSTRAINT chk_hc_categories_no_self CHECK (parent_id IS NULL OR parent_id <> id)
);

CREATE INDEX idx_hc_categories_type   ON public.help_center_categories (article_type_id);
CREATE INDEX idx_hc_categories_parent ON public.help_center_categories (parent_id);
CREATE INDEX idx_hc_categories_status ON public.help_center_categories (status);

-- Articles: self-nesting sub-articles ("Details" pages) + SEO fields.
-- summary/content/published_at/status already exist on articles.
ALTER TABLE public.articles
    ADD COLUMN parent_article_id VARCHAR(36),
    ADD COLUMN meta_title        VARCHAR(255),
    ADD COLUMN meta_description  VARCHAR(500),
    ADD COLUMN meta_keywords     VARCHAR(500),
    ADD COLUMN canonical_url     VARCHAR(500);

ALTER TABLE public.articles
    ADD CONSTRAINT fk_articles_parent_article FOREIGN KEY (parent_article_id)
        REFERENCES articles (id) ON DELETE CASCADE,
    ADD CONSTRAINT chk_articles_no_self_parent CHECK (parent_article_id IS NULL OR parent_article_id <> id);

CREATE INDEX idx_articles_parent_article ON public.articles (parent_article_id);

-- Article <-> Category (many-to-many): one article can belong to several topics.
CREATE TABLE IF NOT EXISTS public.article_categories (
    article_id     VARCHAR(36) NOT NULL,
    category_id    VARCHAR(36) NOT NULL,
    created_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (article_id, category_id),
    CONSTRAINT fk_article_categories_article  FOREIGN KEY (article_id)  REFERENCES articles (id) ON DELETE CASCADE,
    CONSTRAINT fk_article_categories_category FOREIGN KEY (category_id) REFERENCES help_center_categories (id) ON DELETE CASCADE
);

CREATE INDEX idx_article_categories_category ON public.article_categories (category_id);

-- Permissions
INSERT INTO public.permissions (id, code, name, group_name, sort_order) VALUES
    (gen_random_uuid()::text, 'help-center-categories.view',   'View Help Center Categories',   'ARTICLE_MANAGEMENT', 60),
    (gen_random_uuid()::text, 'help-center-categories.create', 'Create Help Center Categories', 'ARTICLE_MANAGEMENT', 61),
    (gen_random_uuid()::text, 'help-center-categories.update', 'Update Help Center Categories', 'ARTICLE_MANAGEMENT', 62),
    (gen_random_uuid()::text, 'help-center-categories.delete', 'Delete Help Center Categories', 'ARTICLE_MANAGEMENT', 63)
ON CONFLICT (code) DO NOTHING;

INSERT INTO public.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM public.roles r
CROSS JOIN public.permissions p
WHERE r.code IN ('ADMIN', 'SUPER_ADMIN')
  AND p.code IN ('help-center-categories.view', 'help-center-categories.create',
                 'help-center-categories.update', 'help-center-categories.delete')
ON CONFLICT DO NOTHING;
