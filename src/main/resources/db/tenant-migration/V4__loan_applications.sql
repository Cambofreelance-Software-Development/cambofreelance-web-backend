-- Tenant-scoped loan application table.
-- Physical isolation is via Postgres schema (schema-per-tenant); no tenant_id column needed.

CREATE SEQUENCE IF NOT EXISTS loan_application_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS loan_applications (
    id                   VARCHAR(36)     PRIMARY KEY,
    loan_number          VARCHAR(20)     NOT NULL UNIQUE,
    customer_id          VARCHAR(36)     NOT NULL REFERENCES customers(id),
    customer_code        VARCHAR(20)     NOT NULL,
    loan_amount          NUMERIC(14,2)   NOT NULL,
    currency             VARCHAR(5)      NOT NULL,
    tenor_value          INTEGER         NOT NULL,
    tenor_unit           VARCHAR(10)     NOT NULL,
    payment_type         VARCHAR(10)     NOT NULL,
    interest_rate        NUMERIC(5,2)    NOT NULL DEFAULT 12.00,
    service_fee_rate     NUMERIC(5,2)    NOT NULL DEFAULT 5.00,
    service_fee_amount   NUMERIC(14,2),
    interest_amount      NUMERIC(14,2),
    net_disbursement     NUMERIC(14,2),
    application_date     DATE            NOT NULL,
    first_payment_date   DATE            NOT NULL,
    purpose              VARCHAR(20)     NOT NULL,
    loan_officer_id      VARCHAR(255)    NOT NULL,
    application_status   VARCHAR(20)     NOT NULL DEFAULT 'DRAFT',
    submitted_by         VARCHAR(255),
    submitted_at         TIMESTAMP,
    approved_by          VARCHAR(255),
    approved_at          TIMESTAMP,
    approval_note        VARCHAR(1000),
    rejected_by          VARCHAR(255),
    rejected_at          TIMESTAMP,
    rejection_reason     VARCHAR(1000),
    created_by           VARCHAR(255)    NOT NULL DEFAULT 'SYS',
    created_at           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by           VARCHAR(255),
    updated_at           TIMESTAMP,
    status               VARCHAR(3)      NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_loan_applications_customer_id        ON loan_applications (customer_id);
CREATE INDEX IF NOT EXISTS idx_loan_applications_application_status ON loan_applications (application_status);
CREATE INDEX IF NOT EXISTS idx_loan_applications_loan_officer_id    ON loan_applications (loan_officer_id);
CREATE INDEX IF NOT EXISTS idx_loan_applications_application_date   ON loan_applications (application_date);
CREATE INDEX IF NOT EXISTS idx_loan_applications_status             ON loan_applications (status);
CREATE INDEX IF NOT EXISTS idx_loan_applications_created_at         ON loan_applications (created_at);
