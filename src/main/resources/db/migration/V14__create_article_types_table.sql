CREATE TABLE IF NOT EXISTS public.article_types (
    id          VARCHAR(36)  PRIMARY KEY,
    code        VARCHAR(50)  NOT NULL,
    label       VARCHAR(100) NOT NULL,
    label_kh    VARCHAR(100),
    sort_order  INTEGER      NOT NULL DEFAULT 0,
    created_by  VARCHAR(255) NOT NULL DEFAULT 'SYS',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(255),
    updated_at  TIMESTAMP,
    status      VARCHAR(3)   NOT NULL DEFAULT 'ACT',
    CONSTRAINT uq_article_types_code UNIQUE (code)
);

CREATE INDEX idx_article_types_status ON public.article_types (status);

INSERT INTO public.article_types (id, code, label, sort_order) VALUES
    (gen_random_uuid(), 'NEWS',          'News',          1),
    (gen_random_uuid(), 'PROMOTIONS',    'Promotions',    2),
    (gen_random_uuid(), 'BLOGS',         'Blogs',         3),
    (gen_random_uuid(), 'ANNOUNCEMENTS', 'Announcements', 4),
    (gen_random_uuid(), 'SERVICE',       'Service',       5),
    (gen_random_uuid(), 'TEAM',          'Team',          6),
    (gen_random_uuid(), 'COURSE',        'Course',        7),
    (gen_random_uuid(), 'PRODUCTS',      'Products',      8);
