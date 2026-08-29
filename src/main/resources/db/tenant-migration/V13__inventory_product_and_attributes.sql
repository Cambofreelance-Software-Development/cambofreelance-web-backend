-- Tenant-scoped Inventory & Product Catalog tables.
-- Follows schema-per-tenant isolation (no tenant_id column needed).

-- Sequence for human-readable product references if needed
CREATE SEQUENCE IF NOT EXISTS product_code_seq START WITH 1 INCREMENT BY 1;

-- 1. PRODUCTS (Parent Model / Catalog Definition)
CREATE TABLE IF NOT EXISTS products (
    id                     VARCHAR(36)    PRIMARY KEY,
    product_code           VARCHAR(30)    UNIQUE,
    sku                    VARCHAR(100)   NOT NULL UNIQUE,
    barcode                VARCHAR(100),
    name                   VARCHAR(255)   NOT NULL,
    product_type           VARCHAR(50)    NOT NULL DEFAULT 'STOCK', -- STOCK, VEHICLE, PART, ELECTRONICS, SERVICE
    category_id            VARCHAR(50)    NOT NULL,                  -- motorcycle, car, truck, parts, tyres, lubricants, accessories, phones, electronics, general
    brand                  VARCHAR(100),
    model                  VARCHAR(100),
    model_year             INTEGER,
    unit                   VARCHAR(50)    NOT NULL DEFAULT 'Piece',
    preferred_supplier     VARCHAR(255),
    description            TEXT,
    image_url              VARCHAR(1000),
    tracking_type          VARCHAR(50)    NOT NULL DEFAULT 'QUANTITY', -- QUANTITY, BATCH, SERIALIZED
    catalog_status         VARCHAR(50)    NOT NULL DEFAULT 'ACTIVE',   -- DRAFT, ACTIVE, INACTIVE
    has_variants           BOOLEAN        NOT NULL DEFAULT FALSE,

    -- Pricing defaults
    currency               VARCHAR(10)    NOT NULL DEFAULT 'USD',
    cost_price             NUMERIC(14,2),
    retail_price           NUMERIC(14,2),
    wholesale_price        NUMERIC(14,2),
    vip_price              NUMERIC(14,2),
    discount_value         NUMERIC(14,2),
    discount_type          VARCHAR(20)    DEFAULT 'percent', -- percent, fixed
    tax_rate               VARCHAR(20)    DEFAULT '10',

    -- Inventory thresholds
    reorder_level          INTEGER,
    min_stock              INTEGER,
    max_stock              INTEGER,
    default_warehouse_id   VARCHAR(36),
    default_location       VARCHAR(100),

    -- Packaging conversions
    case_name              VARCHAR(50),
    case_qty               INTEGER,
    case_barcode           VARCHAR(100),
    box_name               VARCHAR(50),
    box_qty                INTEGER,
    box_barcode            VARCHAR(100),

    -- Vehicle specifications (shared at model level)
    engine_cc              INTEGER,
    fuel_type              VARCHAR(50),
    transmission           VARCHAR(50),
    vehicle_condition      VARCHAR(50),
    warranty_period        VARCHAR(100),

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_products_sku ON products (sku);
CREATE INDEX IF NOT EXISTS idx_products_barcode ON products (barcode) WHERE barcode IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_products_category ON products (category_id);
CREATE INDEX IF NOT EXISTS idx_products_type ON products (product_type);
CREATE INDEX IF NOT EXISTS idx_products_tracking ON products (tracking_type);
CREATE INDEX IF NOT EXISTS idx_products_catalog_status ON products (catalog_status);
CREATE INDEX IF NOT EXISTS idx_products_status ON products (status);
CREATE INDEX IF NOT EXISTS idx_products_created_at ON products (created_at);

-- 2. PRODUCT VARIANTS (Sellable Configurations)
CREATE TABLE IF NOT EXISTS product_variants (
    id                     VARCHAR(36)    PRIMARY KEY,
    product_id             VARCHAR(36)    NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    name                   VARCHAR(255)   NOT NULL, -- e.g. "Black / 125cc"
    sku                    VARCHAR(100)   NOT NULL UNIQUE,
    barcode                VARCHAR(100),
    cost_price_override    NUMERIC(14,2),
    retail_price_override  NUMERIC(14,2),
    wholesale_price_override NUMERIC(14,2),
    vip_price_override     NUMERIC(14,2),
    image_url              VARCHAR(1000),
    is_default             BOOLEAN        NOT NULL DEFAULT FALSE,
    variant_status         VARCHAR(50)    NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_product_variants_product_id ON product_variants (product_id);
CREATE INDEX IF NOT EXISTS idx_product_variants_sku ON product_variants (sku);
CREATE INDEX IF NOT EXISTS idx_product_variants_barcode ON product_variants (barcode) WHERE barcode IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_product_variants_status ON product_variants (status);

-- 3. DYNAMIC ATTRIBUTES (Attribute Definitions)
CREATE TABLE IF NOT EXISTS attributes (
    id                     VARCHAR(36)    PRIMARY KEY,
    code                   VARCHAR(50)    NOT NULL UNIQUE,
    name                   VARCHAR(100)   NOT NULL,
    data_type              VARCHAR(20)    NOT NULL DEFAULT 'TEXT', -- TEXT, NUMBER, BOOLEAN, DATE, SELECT
    is_variant_attribute   BOOLEAN        NOT NULL DEFAULT FALSE,
    applicable_category    VARCHAR(50),                            -- NULL = all, or motorcycle, car, phones, etc.
    sort_order             INTEGER        NOT NULL DEFAULT 0,

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_attributes_category ON attributes (applicable_category);
CREATE INDEX IF NOT EXISTS idx_attributes_code ON attributes (code);

-- 4. ATTRIBUTE PREDEFINED VALUES
CREATE TABLE IF NOT EXISTS attribute_values (
    id                     VARCHAR(36)    PRIMARY KEY,
    attribute_id           VARCHAR(36)    NOT NULL REFERENCES attributes(id) ON DELETE CASCADE,
    code                   VARCHAR(50)    NOT NULL,
    value                  VARCHAR(255)   NOT NULL,
    sort_order             INTEGER        NOT NULL DEFAULT 0,

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT',

    CONSTRAINT uq_attribute_val UNIQUE (attribute_id, code)
);

CREATE INDEX IF NOT EXISTS idx_attribute_values_attr_id ON attribute_values (attribute_id);

-- 5. PRODUCT ATTRIBUTES (Additional Attributes bound to Product)
CREATE TABLE IF NOT EXISTS product_attributes (
    id                     VARCHAR(36)    PRIMARY KEY,
    product_id             VARCHAR(36)    NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    attribute_id           VARCHAR(36)    REFERENCES attributes(id) ON DELETE SET NULL,
    attribute_name         VARCHAR(100)   NOT NULL,
    attribute_value        VARCHAR(1000)  NOT NULL,

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT'
);

CREATE INDEX IF NOT EXISTS idx_product_attributes_product_id ON product_attributes (product_id);

-- 6. VARIANT ATTRIBUTES (Attributes creating Variant Dimensions)
CREATE TABLE IF NOT EXISTS variant_attributes (
    id                     VARCHAR(36)    PRIMARY KEY,
    variant_id             VARCHAR(36)    NOT NULL REFERENCES product_variants(id) ON DELETE CASCADE,
    attribute_id           VARCHAR(36)    REFERENCES attributes(id) ON DELETE SET NULL,
    attribute_name         VARCHAR(100)   NOT NULL,
    attribute_value        VARCHAR(255)   NOT NULL,

    -- BaseEntity audit fields
    created_by             VARCHAR(255)   NOT NULL DEFAULT 'SYS',
    created_at             TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by             VARCHAR(255),
    updated_at             TIMESTAMP,
    status                 VARCHAR(3)     NOT NULL DEFAULT 'ACT',

    CONSTRAINT uq_variant_attr_name UNIQUE (variant_id, attribute_name)
);

CREATE INDEX IF NOT EXISTS idx_variant_attributes_variant_id ON variant_attributes (variant_id);

-- 7. SEED INITIAL ATTRIBUTES & PRESETS
INSERT INTO attributes (id, code, name, data_type, is_variant_attribute, applicable_category, sort_order) VALUES
  ('attr-001', 'COLOUR',         'Colour',           'SELECT', TRUE,  NULL,          10),
  ('attr-002', 'ENGINE_CC',      'Engine CC',        'NUMBER', TRUE,  'motorcycle',  20),
  ('attr-003', 'FUEL_TYPE',      'Fuel Type',        'SELECT', FALSE, 'motorcycle',  30),
  ('attr-004', 'TRANSMISSION',   'Transmission',     'SELECT', TRUE,  'motorcycle',  40),
  ('attr-005', 'BRAKE_TYPE',     'Brake Type',       'SELECT', FALSE, 'motorcycle',  50),
  ('attr-006', 'STARTER',        'Starter',          'SELECT', FALSE, 'motorcycle',  60),
  ('attr-007', 'STORAGE',        'Storage Capacity', 'SELECT', TRUE,  'phones',      70),
  ('attr-008', 'RAM',            'RAM',              'SELECT', TRUE,  'phones',      80),
  ('attr-009', 'WARRANTY_MONTHS','Warranty',         'SELECT', FALSE, NULL,          90),
  ('attr-010', 'ORIGIN_COUNTRY', 'Country of Origin','TEXT',   FALSE, NULL,         100)
ON CONFLICT (code) DO NOTHING;

INSERT INTO attribute_values (id, attribute_id, code, value, sort_order) VALUES
  ('val-001', 'attr-001', 'BLACK',    'Black',      1),
  ('val-002', 'attr-001', 'RED',      'Red',        2),
  ('val-003', 'attr-001', 'WHITE',    'White',      3),
  ('val-004', 'attr-001', 'BLUE',     'Blue',       4),
  ('val-005', 'attr-001', 'GREY',     'Grey',       5),
  ('val-006', 'attr-003', 'PETROL',   'Petrol',     1),
  ('val-007', 'attr-003', 'DIESEL',   'Diesel',     2),
  ('val-008', 'attr-003', 'ELECTRIC', 'Electric',   3),
  ('val-009', 'attr-004', 'MANUAL',   'Manual',     1),
  ('val-010', 'attr-004', 'AUTO',     'Automatic',  2),
  ('val-011', 'attr-004', 'SEMI_AUTO','Semi-Auto',  3),
  ('val-012', 'attr-007', '64GB',     '64 GB',      1),
  ('val-013', 'attr-007', '128GB',    '128 GB',     2),
  ('val-014', 'attr-007', '256GB',    '256 GB',     3),
  ('val-015', 'attr-007', '512GB',    '512 GB',     4),
  ('val-016', 'attr-008', '4GB',      '4 GB',       1),
  ('val-017', 'attr-008', '8GB',      '8 GB',       2),
  ('val-018', 'attr-008', '12GB',     '12 GB',      3),
  ('val-019', 'attr-008', '16GB',     '16 GB',      4)
ON CONFLICT (attribute_id, code) DO NOTHING;
