-- Link app releases to an uploaded binary (APK/IPA/installer) in the media library
ALTER TABLE public.app_releases
    ADD COLUMN IF NOT EXISTS file_id VARCHAR(36) REFERENCES public.media_files(id);

CREATE INDEX IF NOT EXISTS idx_app_releases_file_id ON public.app_releases (file_id);
