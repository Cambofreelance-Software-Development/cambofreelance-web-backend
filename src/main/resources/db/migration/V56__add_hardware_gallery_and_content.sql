-- Rich detail content + multi-image gallery for hardware products.

ALTER TABLE public.hardware ADD COLUMN IF NOT EXISTS content    TEXT;
ALTER TABLE public.hardware ADD COLUMN IF NOT EXISTS content_kh TEXT;

-- Ordered gallery images (position preserves the order chosen in the admin).
CREATE TABLE IF NOT EXISTS public.hardware_images (
    hardware_id   VARCHAR(36) NOT NULL,
    media_file_id VARCHAR(36) NOT NULL,
    position      INTEGER     NOT NULL,
    PRIMARY KEY (hardware_id, position),
    CONSTRAINT fk_hw_img_hardware FOREIGN KEY (hardware_id)   REFERENCES public.hardware    (id),
    CONSTRAINT fk_hw_img_media    FOREIGN KEY (media_file_id) REFERENCES public.media_files (id)
);

CREATE INDEX IF NOT EXISTS idx_hardware_images_hardware ON public.hardware_images (hardware_id);
