CREATE TABLE IF NOT EXISTS repayment_installments (
    id                  VARCHAR(36)     PRIMARY KEY,
    loan_application_id VARCHAR(36)     NOT NULL REFERENCES loan_applications(id) ON DELETE CASCADE,
    loan_number         VARCHAR(20)     NOT NULL,
    installment_no      INT             NOT NULL,
    due_date            DATE            NOT NULL,
    principal_amount    NUMERIC(14,2)   NOT NULL,
    interest_amount     NUMERIC(14,2)   NOT NULL,
    total_payment       NUMERIC(14,2)   NOT NULL,
    paid_amount         NUMERIC(14,2)   NOT NULL DEFAULT 0,
    remaining_balance   NUMERIC(14,2)   NOT NULL,
    installment_status  VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    paid_date           DATE,
    notes               VARCHAR(500),
    created_by          VARCHAR(255)    NOT NULL DEFAULT 'SYS',
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by          VARCHAR(255),
    updated_at          TIMESTAMP,
    status              VARCHAR(3)      NOT NULL DEFAULT 'ACT',
    UNIQUE (loan_application_id, installment_no)
);

CREATE INDEX IF NOT EXISTS idx_repayment_installments_loan_id     ON repayment_installments (loan_application_id);
CREATE INDEX IF NOT EXISTS idx_repayment_installments_status      ON repayment_installments (installment_status);
CREATE INDEX IF NOT EXISTS idx_repayment_installments_due_date    ON repayment_installments (due_date);
