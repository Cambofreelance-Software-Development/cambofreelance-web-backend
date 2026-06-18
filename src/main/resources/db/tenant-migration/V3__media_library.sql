-- Tenant-scoped media library: each tenant manages its own collection of uploaded files.
-- Physical isolation via schema; media_id is a cross-schema FK to public.media_files.

CREATE TABLE IF NOT EXISTS media_library (
    id          VARCHAR(36)   PRIMARY KEY,
    media_id    VARCHAR(36)   NOT NULL UNIQUE REFERENCES public.media_files(id),
    folder      VARCHAR(255),
    title       VARCHAR(500),
    alt_text    VARCHAR(500),
    description TEXT,
    tags        VARCHAR(1000),
    uploaded_by VARCHAR(255),
    created_by  VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  VARCHAR(255),
    updated_at  TIMESTAMP,
    status      VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_media_library_status     ON media_library (status);
CREATE INDEX IF NOT EXISTS idx_media_library_folder     ON media_library (folder);
CREATE INDEX IF NOT EXISTS idx_media_library_created_at ON media_library (created_at);
