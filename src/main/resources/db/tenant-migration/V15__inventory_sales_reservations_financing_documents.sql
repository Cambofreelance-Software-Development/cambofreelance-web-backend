-- Tenant-scoped Sales, Reservations, Financing Applications, Banks, and Polymorphic Documents.
-- Complete isolation within tenant schema.

CREATE SEQUENCE IF NOT EXISTS sale_no_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS financing_app_no_seq START WITH 1 INCREMENT BY 1;

-- 1. SALES
CREATE TABLE IF NOT EXISTS sales (
    id                     VARCHAR(36)    PRIMARY KEY,
    sale_no                VARCHAR(50)    NOT NULL UNIQUE,
    customer_id            VARCHAR(36)    NOT NULL REFERENCES customers(id) ON DELETE RESTRICT,
    salesperson_id         VARCHAR(36),
    warehouse_id           VARCHAR(36)    REFERENCES warehouses(id) ON DELETE RESTRICT,

    -- DRAFT, RESERVED, LOAN_PENDING, CONFIRMED, DELIVERED, CANCELLED, COMPLETED
    sale_status            VARCHAR(30)    NOT NULL DEFAULT 'DRAFT',

    -- CASH, INSTALLMENT, BANK_LOAN
    payment_type           VARCHAR(30)    NOT NULL DEFAULT 'CASH',
    currency               VARCHAR(10)    NOT NULL DEFAULT 'USD',

    -- Financial Breakdown
    subtotal               NUMERIC(14,2)  NOT NULL DEFAULT 0.00,
    discount_amount        NUMERIC(14,2)  NOT NULL DEFAULT 0.00,
    tax_amount             NUMERIC(14,2)  NOT NULL DEFAULT 0.00,
    total_amount           NUMERIC(14,2)  NOT NULL DEFAULT 0.00,
    down_payment           NUMERIC(14,2)  NOT NULL DEFAULT 0.00,
    financed_amount        NUMERIC(14,2)  NOT NULL DEFAULT 0.00,

    contract_date          TIMESTAMP,
    delivery_date          TIMESTAMP,
    notes                  TEXT,

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_sales_customer_id ON sales (customer_id);
CREATE INDEX IF NOT EXISTS idx_sales_salesperson_id ON sales (salesperson_id);
CREATE INDEX IF NOT EXISTS idx_sales_sale_status ON sales (sale_status);
CREATE INDEX IF NOT EXISTS idx_sales_sale_no ON sales (sale_no);
CREATE INDEX IF NOT EXISTS idx_sales_contract_date ON sales (contract_date);

-- 2. SALE ITEMS
CREATE TABLE IF NOT EXISTS sale_items (
    id                     VARCHAR(36)    PRIMARY KEY,
    sale_id                VARCHAR(36)    NOT NULL REFERENCES sales(id) ON DELETE CASCADE,
    variant_id             VARCHAR(36)    NOT NULL REFERENCES product_variants(id) ON DELETE RESTRICT,
    inventory_item_id      VARCHAR(36)    REFERENCES inventory_items(id) ON DELETE RESTRICT,
    batch_id               VARCHAR(36)    REFERENCES inventory_batches(id) ON DELETE RESTRICT,

    quantity               NUMERIC(14,2)  NOT NULL DEFAULT 1.00,
    unit_price             NUMERIC(14,2)  NOT NULL DEFAULT 0.00,
    discount_amount        NUMERIC(14,2)  NOT NULL DEFAULT 0.00,
    cost_price             NUMERIC(14,2)  NOT NULL DEFAULT 0.00,
    total_amount           NUMERIC(14,2)  NOT NULL DEFAULT 0.00,

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_sale_items_sale_id ON sale_items (sale_id);
CREATE INDEX IF NOT EXISTS idx_sale_items_variant_id ON sale_items (variant_id);
CREATE INDEX IF NOT EXISTS idx_sale_items_item_id ON sale_items (inventory_item_id);

-- 3. INVENTORY RESERVATIONS (Double-booking prevention for Serialized Units)
CREATE TABLE IF NOT EXISTS inventory_reservations (
    id                     VARCHAR(36)    PRIMARY KEY,
    inventory_item_id      VARCHAR(36)    NOT NULL REFERENCES inventory_items(id) ON DELETE RESTRICT,
    sale_id                VARCHAR(36)    REFERENCES sales(id) ON DELETE SET NULL,
    customer_id            VARCHAR(36)    REFERENCES customers(id) ON DELETE SET NULL,
    reserved_by            VARCHAR(255)   NOT NULL, -- Salesperson / User

    -- ACTIVE, RELEASED, EXPIRED, CONVERTED
    status                 VARCHAR(30)    NOT NULL DEFAULT 'ACTIVE',
    reserved_at            TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at             TIMESTAMP,
    released_at            TIMESTAMP,
    notes                  VARCHAR(1000),

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP
);

-- Crucial Concurrency Constraint: At most ONE active reservation per serialized item
CREATE UNIQUE INDEX IF NOT EXISTS uq_active_reservation_per_item
    ON inventory_reservations (inventory_item_id) WHERE status = 'ACTIVE';

CREATE INDEX IF NOT EXISTS idx_inventory_reservations_item ON inventory_reservations (inventory_item_id);
CREATE INDEX IF NOT EXISTS idx_inventory_reservations_sale ON inventory_reservations (sale_id);
CREATE INDEX IF NOT EXISTS idx_inventory_reservations_status ON inventory_reservations (status);

-- 4. BANKS (Financing Partners)
CREATE TABLE IF NOT EXISTS banks (
    id                     VARCHAR(36)    PRIMARY KEY,
    code                   VARCHAR(50)    NOT NULL UNIQUE,
    name                   VARCHAR(255)   NOT NULL,
    logo_url               VARCHAR(1000),
    phone_number           VARCHAR(50),
    email                  VARCHAR(100),
    active                 BOOLEAN        NOT NULL DEFAULT TRUE,

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_banks_code ON banks (code);

-- Seed initial major partner banks in Cambodia
INSERT INTO banks (id, code, name, phone_number, active, created_by) VALUES
  ('bank-acleda',   'ACLEDA',   'ACLEDA Bank Plc.',               '+855 23 999 999', TRUE, 'SYS'),
  ('bank-aba',      'ABA',      'ABA Bank (Advanced Bank of Asia)','+855 23 225 333', TRUE, 'SYS'),
  ('bank-canadia',  'CANADIA',  'Canadia Bank PLC',               '+855 23 868 222', TRUE, 'SYS'),
  ('bank-sathapana','SATHAPANA','Sathapana Bank Plc',             '+855 23 999 010', TRUE, 'SYS'),
  ('bank-amk',      'AMK',      'AMK Microfinance Institution',   '+855 23 220 202', TRUE, 'SYS'),
  ('bank-lolc',     'LOLC',     'LOLC (Cambodia) Plc.',           '+855 23 991 991', TRUE, 'SYS'),
  ('bank-wing',     'WING',     'Wing Bank (Cambodia) Plc',       '+855 23 999 989', TRUE, 'SYS')
ON CONFLICT (code) DO NOTHING;

-- 5. FINANCING APPLICATIONS (Decoupled Financing Tracking)
CREATE TABLE IF NOT EXISTS financing_applications (
    id                     VARCHAR(36)    PRIMARY KEY,
    application_no         VARCHAR(50)    NOT NULL UNIQUE,
    sale_id                VARCHAR(36)    NOT NULL REFERENCES sales(id) ON DELETE CASCADE,
    customer_id            VARCHAR(36)    NOT NULL REFERENCES customers(id) ON DELETE RESTRICT,
    bank_id                VARCHAR(36)    REFERENCES banks(id) ON DELETE SET NULL,

    requested_amount       NUMERIC(14,2)  NOT NULL,
    approved_amount        NUMERIC(14,2),
    interest_rate          NUMERIC(6,3),
    term_months            INTEGER,
    monthly_installment    NUMERIC(14,2),

    -- DRAFT, SUBMITTED, UNDER_REVIEW, ADDITIONAL_DOCUMENT_REQUIRED, APPROVED, REJECTED, CANCELLED, EXPIRED
    status                 VARCHAR(40)    NOT NULL DEFAULT 'DRAFT',

    -- External reference bridge to Mini Loan or Bank API
    external_reference     VARCHAR(100),
    guarantor_customer_id  VARCHAR(36)    REFERENCES customers(id) ON DELETE SET NULL,

    submitted_at           TIMESTAMP,
    approved_at            TIMESTAMP,
    rejected_at            TIMESTAMP,
    rejection_reason       VARCHAR(1000),

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_financing_app_sale_id ON financing_applications (sale_id);
CREATE INDEX IF NOT EXISTS idx_financing_app_customer_id ON financing_applications (customer_id);
CREATE INDEX IF NOT EXISTS idx_financing_app_bank_id ON financing_applications (bank_id);
CREATE INDEX IF NOT EXISTS idx_financing_app_status ON financing_applications (status);
CREATE INDEX IF NOT EXISTS idx_financing_app_ext_ref ON financing_applications (external_reference);

-- 6. POLYMORPHIC INVENTORY & SALES DOCUMENTS
CREATE TABLE IF NOT EXISTS inventory_documents (
    id                     VARCHAR(36)    PRIMARY KEY,
    -- CUSTOMER, SALE, FINANCING_APPLICATION, INVENTORY_ITEM, PRODUCT, VARIANT
    owner_type             VARCHAR(50)    NOT NULL,
    owner_id               VARCHAR(36)    NOT NULL,

    media_id               VARCHAR(36)    REFERENCES public.media_files(id) ON DELETE SET NULL,
    -- NATIONAL_ID, FAMILY_BOOK, SALARY_CERTIFICATE, BANK_STATEMENT, REGISTRATION, INVOICE, CONTRACT, WARRANTY, SPECIFICATION, OTHER
    document_type          VARCHAR(50)    NOT NULL,
    document_name          VARCHAR(255)   NOT NULL,
    file_url               VARCHAR(1000),
    mime_type              VARCHAR(100),
    file_size              BIGINT,

    -- UPLOADED, PENDING_REVIEW, VERIFIED, REJECTED, EXPIRED, REPLACED
    document_status        VARCHAR(30)    NOT NULL DEFAULT 'UPLOADED',
    version                INTEGER        NOT NULL DEFAULT 1,
    expires_at             DATE,

    verified_by            VARCHAR(255),
    verified_at            TIMESTAMP,

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_inventory_docs_owner ON inventory_documents (owner_type, owner_id);
CREATE INDEX IF NOT EXISTS idx_inventory_docs_type ON inventory_documents (document_type);
CREATE INDEX IF NOT EXISTS idx_inventory_docs_status ON inventory_documents (document_status);
