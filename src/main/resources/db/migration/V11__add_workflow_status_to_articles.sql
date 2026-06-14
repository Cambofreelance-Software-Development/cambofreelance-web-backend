ALTER TABLE public.articles
    ADD COLUMN IF NOT EXISTS workflow_status VARCHAR(20) NOT NULL DEFAULT 'DRAFT';

UPDATE public.articles
SET workflow_status = 'PUBLISHED'
WHERE status = 'ACT'
  AND published_at IS NOT NULL;

CREATE INDEX idx_articles_workflow_status ON public.articles (workflow_status);
