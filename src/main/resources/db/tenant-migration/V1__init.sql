-- Tenant-scoped business tables (e.g. customers, loans, payments, documents) will land
-- here as Flyway migrations once those modules are built. This file just gives Flyway a
-- valid migration set so TenantSchemaProvisioningService can initialize
-- flyway_schema_history in every newly provisioned tenant schema.
SELECT 1;
