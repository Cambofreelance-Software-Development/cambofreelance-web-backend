CREATE TABLE IF NOT EXISTS collection_cases (
    id                   VARCHAR(36)    PRIMARY KEY,
    loan_application_id  VARCHAR(36)    NOT NULL UNIQUE REFERENCES loan_applications(id),
    loan_number          VARCHAR(50),
    currency             VARCHAR(10),
    customer_id          VARCHAR(36),
    customer_code        VARCHAR(50),
    customer_first_name  VARCHAR(100),
    customer_last_name   VARCHAR(100),
    collection_status    VARCHAR(20)    NOT NULL DEFAULT 'CURRENT',
    assigned_officer_id  VARCHAR(255),
    dpd                  INT            NOT NULL DEFAULT 0,
    total_overdue_amount NUMERIC(14,2)  NOT NULL DEFAULT 0,
    penalty_rate         NUMERIC(8,6)   NOT NULL DEFAULT 0.001,
    penalty_amount       NUMERIC(14,2)  NOT NULL DEFAULT 0,
    remarks              VARCHAR(2000),
    created_by           VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at           TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by           VARCHAR(255),
    updated_at           TIMESTAMP,
    status               VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_collection_cases_loan_id     ON collection_cases (loan_application_id);
CREATE INDEX IF NOT EXISTS idx_collection_cases_status      ON collection_cases (collection_status);
CREATE INDEX IF NOT EXISTS idx_collection_cases_officer     ON collection_cases (assigned_officer_id);
CREATE INDEX IF NOT EXISTS idx_collection_cases_dpd         ON collection_cases (dpd DESC);

CREATE TABLE IF NOT EXISTS collection_notes (
    id                  VARCHAR(36)   PRIMARY KEY,
    collection_case_id  VARCHAR(36)   NOT NULL REFERENCES collection_cases(id),
    loan_application_id VARCHAR(36)   NOT NULL,
    note_type           VARCHAR(20)   NOT NULL,
    content             VARCHAR(2000) NOT NULL,
    contact_person      VARCHAR(100),
    contact_result      VARCHAR(20),
    created_by          VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at          TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by          VARCHAR(255),
    updated_at          TIMESTAMP,
    status              VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_collection_notes_case_id ON collection_notes (collection_case_id);

CREATE TABLE IF NOT EXISTS promise_to_pay (
    id                  VARCHAR(36)   PRIMARY KEY,
    collection_case_id  VARCHAR(36)   NOT NULL REFERENCES collection_cases(id),
    loan_application_id VARCHAR(36)   NOT NULL,
    promise_date        DATE          NOT NULL,
    promise_amount      NUMERIC(14,2) NOT NULL,
    ptp_status          VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    notes               VARCHAR(500),
    created_by          VARCHAR(255)  NOT NULL DEFAULT 'SYS',
    created_at          TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by          VARCHAR(255),
    updated_at          TIMESTAMP,
    status              VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_ptp_case_id ON promise_to_pay (collection_case_id);
