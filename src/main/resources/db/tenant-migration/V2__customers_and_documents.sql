-- Tenant-scoped business tables: physical isolation via schema, no tenant_id column needed.

CREATE SEQUENCE IF NOT EXISTS customer_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS customers (
    id                     VARCHAR(36)   PRIMARY KEY,
    customer_code          VARCHAR(20)   NOT NULL UNIQUE,
    first_name             VARCHAR(255)  NOT NULL,
    last_name              VARCHAR(255)  NOT NULL,
    gender                 VARCHAR(10)   NOT NULL,
    date_of_birth          DATE,
    phone_number           VARCHAR(30)   NOT NULL UNIQUE,
    alternate_phone        VARCHAR(30),
    identity_card_number   VARCHAR(50),
    address                VARCHAR(1000),
    occupation             VARCHAR(255),
    employer_name          VARCHAR(255),
    monthly_income         NUMERIC(14,2),
    guarantor_name         VARCHAR(255),
    guarantor_phone        VARCHAR(30),
    onboarding_status      VARCHAR(30)   NOT NULL DEFAULT 'DRAFT',
    submitted_by           VARCHAR(255),
    submitted_at           TIMESTAMP,
    verified_by            VARCHAR(255),
    verified_at            TIMESTAMP,
    verification_note      VARCHAR(1000),
    approved_by            VARCHAR(255),
    approved_at            TIMESTAMP,
    approval_note          VARCHAR(1000),
    rejected_by            VARCHAR(255),
    rejected_at            TIMESTAMP,
    rejection_reason       VARCHAR(1000),
    created_by             VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_customers_identity_card
    ON customers (identity_card_number) WHERE identity_card_number IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_customers_onboarding_status ON customers (onboarding_status);
CREATE INDEX IF NOT EXISTS idx_customers_status ON customers (status);
CREATE INDEX IF NOT EXISTS idx_customers_created_at ON customers (created_at);

CREATE TABLE IF NOT EXISTS customer_documents (
    id              VARCHAR(36)  PRIMARY KEY,
    customer_id     VARCHAR(36)  NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    media_id        VARCHAR(36)  NOT NULL UNIQUE REFERENCES public.media_files(id),
    document_type   VARCHAR(40)  NOT NULL,
    created_by      VARCHAR(255) NOT NULL DEFAULT 'SYS',
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      VARCHAR(255),
    updated_at      TIMESTAMP,
    status          VARCHAR(3)   NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_customer_documents_customer_id ON customer_documents (customer_id);
