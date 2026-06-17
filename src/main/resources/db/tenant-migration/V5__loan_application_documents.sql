-- Supporting documents for loan applications.
-- Physical isolation is via Postgres schema (schema-per-tenant); no tenant_id column needed.
-- media_id cross-references public.media_files via the shared upload pipeline.

CREATE TABLE IF NOT EXISTS loan_application_documents (
    id                      VARCHAR(36)  PRIMARY KEY,
    loan_application_id     VARCHAR(36)  NOT NULL REFERENCES loan_applications(id) ON DELETE CASCADE,
    media_id                VARCHAR(36)  NOT NULL UNIQUE REFERENCES public.media_files(id),
    document_type           VARCHAR(40)  NOT NULL,
    created_by              VARCHAR(255) NOT NULL DEFAULT 'SYS',
    created_at              TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by              VARCHAR(255),
    updated_at              TIMESTAMP,
    status                  VARCHAR(3)   NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_loan_docs_loan_application_id
    ON loan_application_documents (loan_application_id);
CREATE INDEX IF NOT EXISTS idx_loan_docs_document_type
    ON loan_application_documents (document_type);
CREATE INDEX IF NOT EXISTS idx_loan_docs_status
    ON loan_application_documents (status);
