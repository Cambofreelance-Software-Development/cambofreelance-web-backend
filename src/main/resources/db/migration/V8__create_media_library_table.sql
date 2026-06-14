CREATE TABLE IF NOT EXISTS public.media_files (
    id            VARCHAR(36)  PRIMARY KEY,
    file_name     VARCHAR(255) NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    media_type    VARCHAR(20)  NOT NULL,
    mime_type     VARCHAR(100),
    file_size     BIGINT,
    file_path     VARCHAR(500) NOT NULL,
    file_url      VARCHAR(500) NOT NULL,
    uploaded_by   VARCHAR(36),
    created_by    VARCHAR(255) NOT NULL DEFAULT 'SYS',
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by    VARCHAR(255),
    updated_at    TIMESTAMP,
    status        VARCHAR(3)   NOT NULL DEFAULT 'ACT'
);

CREATE INDEX idx_media_files_type   ON public.media_files (media_type);
CREATE INDEX idx_media_files_status ON public.media_files (status);
