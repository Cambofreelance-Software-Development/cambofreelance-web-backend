CREATE TABLE IF NOT EXISTS public.articles (
    id                VARCHAR(36)   PRIMARY KEY,
    title             VARCHAR(500)  NOT NULL,
    slug              VARCHAR(500)  NOT NULL,
    content           TEXT,
    excerpt           VARCHAR(1000),
    type              VARCHAR(20)   NOT NULL,
    featured_image_id VARCHAR(36),
    author_id         VARCHAR(36),
    author_name       VARCHAR(255),
    tags              VARCHAR(500),
    published_at      TIMESTAMP,
    sort_order        INTEGER       NOT NULL DEFAULT 0,
    view_count        INTEGER       NOT NULL DEFAULT 0,
    created_by        VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by        VARCHAR(255),
    updated_at        TIMESTAMP,
    status            VARCHAR(3)    NOT NULL DEFAULT 'ACT',
    CONSTRAINT uq_articles_slug          UNIQUE (slug),
    CONSTRAINT fk_articles_featured_img  FOREIGN KEY (featured_image_id) REFERENCES media_files (id)
);

CREATE INDEX idx_articles_type   ON public.articles (type);
CREATE INDEX idx_articles_status ON public.articles (status);
CREATE INDEX idx_articles_slug   ON public.articles (slug);

CREATE TABLE IF NOT EXISTS public.article_attachments (
    article_id    VARCHAR(36) NOT NULL,
    media_file_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (article_id, media_file_id),
    CONSTRAINT fk_art_attach_article FOREIGN KEY (article_id)    REFERENCES articles    (id),
    CONSTRAINT fk_art_attach_media   FOREIGN KEY (media_file_id) REFERENCES media_files (id)
);
