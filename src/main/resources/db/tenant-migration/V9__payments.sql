CREATE SEQUENCE IF NOT EXISTS payment_receipt_seq START 1 INCREMENT 1;

CREATE TABLE IF NOT EXISTS payments (
    id                   VARCHAR(36)     PRIMARY KEY,
    receipt_number       VARCHAR(30)     NOT NULL UNIQUE,
    loan_application_id  VARCHAR(36)     NOT NULL REFERENCES loan_applications(id),
    loan_number          VARCHAR(50),
    currency             VARCHAR(10),
    customer_id          VARCHAR(36),
    customer_code        VARCHAR(50),
    customer_first_name  VARCHAR(100),
    customer_last_name   VARCHAR(100),
    payment_date         DATE            NOT NULL,
    principal_paid       NUMERIC(14,2)   NOT NULL DEFAULT 0,
    interest_paid        NUMERIC(14,2)   NOT NULL DEFAULT 0,
    penalty_fee          NUMERIC(14,2)   NOT NULL DEFAULT 0,
    total_paid           NUMERIC(14,2)   NOT NULL,
    payment_method       VARCHAR(20)     NOT NULL,
    payment_type         VARCHAR(20)     NOT NULL,
    received_by_id       VARCHAR(255),
    notes                VARCHAR(1000),
    payment_status       VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE',
    reversed_at          TIMESTAMP,
    reversed_by_id       VARCHAR(255),
    reversal_reason      VARCHAR(500),
    created_by           VARCHAR(255)    NOT NULL DEFAULT 'SYS',
    created_at           TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by           VARCHAR(255),
    updated_at           TIMESTAMP,
    status               VARCHAR(3)      NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_payments_loan_application_id ON payments (loan_application_id);
CREATE INDEX IF NOT EXISTS idx_payments_payment_date        ON payments (payment_date);
CREATE INDEX IF NOT EXISTS idx_payments_payment_status      ON payments (payment_status);
