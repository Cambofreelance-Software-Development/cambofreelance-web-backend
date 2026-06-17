CREATE TABLE IF NOT EXISTS loan_disbursements (
    id                      VARCHAR(36)     PRIMARY KEY,
    loan_application_id     VARCHAR(36)     NOT NULL UNIQUE REFERENCES loan_applications(id),
    loan_number             VARCHAR(20)     NOT NULL,
    customer_id             VARCHAR(36)     NOT NULL,
    customer_code           VARCHAR(20)     NOT NULL,
    customer_first_name     VARCHAR(100)    NOT NULL,
    customer_last_name      VARCHAR(100)    NOT NULL,
    disbursement_amount     NUMERIC(14,2)   NOT NULL,
    currency                VARCHAR(5)      NOT NULL,
    contract_media_id       VARCHAR(36)     REFERENCES public.media_files(id),
    disbursement_date       DATE,
    disbursed_by_id         VARCHAR(255),
    disbursement_status     VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    notes                   VARCHAR(1000),
    created_by              VARCHAR(255)    NOT NULL DEFAULT 'SYS',
    created_at              TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by              VARCHAR(255),
    updated_at              TIMESTAMP,
    status                  VARCHAR(3)      NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_loan_disbursements_loan_application_id ON loan_disbursements (loan_application_id);
CREATE INDEX IF NOT EXISTS idx_loan_disbursements_customer_id         ON loan_disbursements (customer_id);
CREATE INDEX IF NOT EXISTS idx_loan_disbursements_disbursement_status ON loan_disbursements (disbursement_status);
CREATE INDEX IF NOT EXISTS idx_loan_disbursements_created_at          ON loan_disbursements (created_at);
