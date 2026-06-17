-- Denormalise customer name columns into loan_applications (mirroring customer_code).
-- Backfill from the customers table for any rows already present.

ALTER TABLE loan_applications
    ADD COLUMN IF NOT EXISTS customer_first_name VARCHAR(100) NOT NULL DEFAULT '',
    ADD COLUMN IF NOT EXISTS customer_last_name  VARCHAR(100) NOT NULL DEFAULT '';

UPDATE loan_applications la
SET customer_first_name = c.first_name,
    customer_last_name  = c.last_name
FROM customers c
WHERE la.customer_id = c.id;
