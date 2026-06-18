-- Normalize taxonomy / taxonomy_item rows created with wrong status value "Active".
-- The correct short-code for active is "ACT"; "Active" was written by mistake.
UPDATE taxonomy
SET status = 'ACT'
WHERE status = 'Active';

UPDATE taxonomy_item
SET status = 'ACT'
WHERE status = 'Active';
