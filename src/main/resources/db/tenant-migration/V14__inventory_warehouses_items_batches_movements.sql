-- Tenant-scoped Warehouses, Locations, Serialized Inventory Items, Batches, and Stock Movement Ledger.
-- Follows schema-per-tenant isolation (physical isolation via Postgres schema).

-- 1. WAREHOUSES
CREATE TABLE IF NOT EXISTS warehouses (
    id                     VARCHAR(36)    PRIMARY KEY,
    code                   VARCHAR(50)    NOT NULL UNIQUE,
    name                   VARCHAR(255)   NOT NULL,
    address                VARCHAR(500),
    phone_number           VARCHAR(50),
    manager_name           VARCHAR(255),
    is_default             BOOLEAN        NOT NULL DEFAULT FALSE,

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_warehouses_code ON warehouses (code);
CREATE INDEX IF NOT EXISTS idx_warehouses_status ON warehouses (status);

-- 2. WAREHOUSE LOCATIONS (Aisles / Racks / Bins / Showroom spots)
CREATE TABLE IF NOT EXISTS warehouse_locations (
    id                     VARCHAR(36)    PRIMARY KEY,
    warehouse_id           VARCHAR(36)    NOT NULL REFERENCES warehouses(id) ON DELETE CASCADE,
    code                   VARCHAR(50)    NOT NULL, -- e.g. A-12-03, SHOWROOM-01
    name                   VARCHAR(100),
    aisle                  VARCHAR(30),
    rack                   VARCHAR(30),
    bin                    VARCHAR(30),

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT',

    CONSTRAINT uq_warehouse_location_code UNIQUE (warehouse_id, code)
);

CREATE INDEX IF NOT EXISTS idx_warehouse_locations_wh_id ON warehouse_locations (warehouse_id);

-- 3. INVENTORY ITEMS (Physical Serialized Units: Motorcycles, Cars, Phones, Laptops)
CREATE TABLE IF NOT EXISTS inventory_items (
    id                     VARCHAR(36)    PRIMARY KEY,
    variant_id             VARCHAR(36)    NOT NULL REFERENCES product_variants(id) ON DELETE RESTRICT,
    warehouse_id           VARCHAR(36)    NOT NULL REFERENCES warehouses(id) ON DELETE RESTRICT,
    location_id            VARCHAR(36)    REFERENCES warehouse_locations(id) ON DELETE SET NULL,

    -- Unit Identifiers
    serial_no              VARCHAR(100),
    vin                    VARCHAR(17),   -- 17-char VIN/Chassis number
    engine_no              VARCHAR(100),  -- Engine number
    color                  VARCHAR(100),  -- Actual unit colour

    -- Unit Financials & Status
    purchase_cost          NUMERIC(14,2)  NOT NULL DEFAULT 0.00,
    item_status            VARCHAR(30)    NOT NULL DEFAULT 'AVAILABLE', -- AVAILABLE, RESERVED, LOAN_PENDING, SOLD, DELIVERED, RETURNED, DAMAGED

    -- Origin & Sourcing
    supplier_name          VARCHAR(255),
    po_reference           VARCHAR(100),
    received_at            DATE           NOT NULL DEFAULT CURRENT_DATE,

    -- Salesperson Hold / Reservation
    reserved_by            VARCHAR(255),
    reserved_until         TIMESTAMP,

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);

-- Partial Unique Indexes for identifiers (nulls allowed for non-vehicle/non-serial types, but unique when present)
CREATE UNIQUE INDEX IF NOT EXISTS uq_inventory_items_vin
    ON inventory_items (vin) WHERE vin IS NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_inventory_items_serial
    ON inventory_items (serial_no) WHERE serial_no IS NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS uq_inventory_items_engine
    ON inventory_items (engine_no) WHERE engine_no IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_inventory_items_variant_id ON inventory_items (variant_id);
CREATE INDEX IF NOT EXISTS idx_inventory_items_warehouse_id ON inventory_items (warehouse_id);
CREATE INDEX IF NOT EXISTS idx_inventory_items_status ON inventory_items (item_status);
CREATE INDEX IF NOT EXISTS idx_inventory_items_received_at ON inventory_items (received_at);

-- 4. INVENTORY BATCHES (Lot & Expiry Tracking: Spare Parts, Fluids, Consumables)
CREATE TABLE IF NOT EXISTS inventory_batches (
    id                     VARCHAR(36)    PRIMARY KEY,
    variant_id             VARCHAR(36)    NOT NULL REFERENCES product_variants(id) ON DELETE RESTRICT,
    warehouse_id           VARCHAR(36)    NOT NULL REFERENCES warehouses(id) ON DELETE RESTRICT,
    location_id            VARCHAR(36)    REFERENCES warehouse_locations(id) ON DELETE SET NULL,

    batch_no               VARCHAR(100)   NOT NULL,
    quantity               NUMERIC(14,2)  NOT NULL DEFAULT 0,
    cost_price             NUMERIC(14,2),
    manufactured_at        DATE,
    expires_at             DATE,

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT',

    CONSTRAINT uq_variant_warehouse_batch UNIQUE (variant_id, warehouse_id, batch_no)
);

CREATE INDEX IF NOT EXISTS idx_inventory_batches_variant_id ON inventory_batches (variant_id);
CREATE INDEX IF NOT EXISTS idx_inventory_batches_warehouse_id ON inventory_batches (warehouse_id);
CREATE INDEX IF NOT EXISTS idx_inventory_batches_expires_at ON inventory_batches (expires_at);

-- 5. INVENTORY MOVEMENTS (Immutable Stock Transaction Ledger)
CREATE TABLE IF NOT EXISTS inventory_movements (
    id                     VARCHAR(36)    PRIMARY KEY,
    movement_type          VARCHAR(50)    NOT NULL, -- OPENING, PURCHASE, SALE, RETURN_IN, RETURN_OUT, TRANSFER_IN, TRANSFER_OUT, ADJUSTMENT_IN, ADJUSTMENT_OUT, DAMAGE
    variant_id             VARCHAR(36)    NOT NULL REFERENCES product_variants(id) ON DELETE RESTRICT,
    inventory_item_id      VARCHAR(36)    REFERENCES inventory_items(id) ON DELETE SET NULL,
    batch_id               VARCHAR(36)    REFERENCES inventory_batches(id) ON DELETE SET NULL,

    from_warehouse_id      VARCHAR(36)    REFERENCES warehouses(id) ON DELETE SET NULL,
    to_warehouse_id        VARCHAR(36)    REFERENCES warehouses(id) ON DELETE SET NULL,

    quantity               NUMERIC(14,2)  NOT NULL,
    unit_cost              NUMERIC(14,2)  NOT NULL DEFAULT 0.00,
    total_cost             NUMERIC(14,2)  NOT NULL DEFAULT 0.00,

    reference_type         VARCHAR(50),   -- PO, SALE, TRANSFER, ADJUSTMENT, INITIAL
    reference_id           VARCHAR(100),
    note                   VARCHAR(1000),

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_inventory_movements_variant_id ON inventory_movements (variant_id);
CREATE INDEX IF NOT EXISTS idx_inventory_movements_item_id ON inventory_movements (inventory_item_id);
CREATE INDEX IF NOT EXISTS idx_inventory_movements_batch_id ON inventory_movements (batch_id);
CREATE INDEX IF NOT EXISTS idx_inventory_movements_type ON inventory_movements (movement_type);
CREATE INDEX IF NOT EXISTS idx_inventory_movements_ref ON inventory_movements (reference_type, reference_id);
CREATE INDEX IF NOT EXISTS idx_inventory_movements_created_at ON inventory_movements (created_at);

-- 6. DEFAULT INITIAL WAREHOUSES
INSERT INTO warehouses (id, code, name, address, is_default, created_by, status) VALUES
  ('wh-main',     'MAIN',     'Main Warehouse — Phnom Penh', 'Phnom Penh, Cambodia', TRUE,  'SYS', 'ACT'),
  ('wh-showroom', 'SHOWROOM', 'Showroom — Toul Kork',         'Toul Kork, Phnom Penh', FALSE, 'SYS', 'ACT'),
  ('wh-service',  'SERVICE',  'Service Centre Store',        'Phnom Penh, Cambodia', FALSE, 'SYS', 'ACT'),
  ('wh-transit',  'TRANSIT',  'Transit / In Bond',           'Customs / Transit',     FALSE, 'SYS', 'ACT')
ON CONFLICT (code) DO NOTHING;
