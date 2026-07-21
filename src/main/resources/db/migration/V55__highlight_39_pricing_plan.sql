-- Feature the $39 plan on the pricing page instead of the previously highlighted one
UPDATE public.pricing_plan SET highlighted = FALSE WHERE highlighted = TRUE;
UPDATE public.pricing_plan SET highlighted = TRUE  WHERE price_monthly = 39;
