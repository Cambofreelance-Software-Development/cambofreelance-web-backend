-- "How to Make Sales" now has a more specific home in the Sales & Refunds
-- sub-topic (V64); drop its earlier direct link to the top-level Sales
-- category so it doesn't show twice on the Sales topic page.
DELETE FROM public.article_categories
WHERE article_id = (SELECT id FROM public.articles WHERE slug = 'how-to-make-sales')
  AND category_id = (SELECT id FROM public.help_center_categories WHERE slug = 'sales');
