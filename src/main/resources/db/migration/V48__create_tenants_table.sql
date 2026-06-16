CREATE TABLE IF NOT EXISTS public.tenants (
    id               VARCHAR(255)  PRIMARY KEY,
    code             VARCHAR(50)   NOT NULL UNIQUE,
    name             VARCHAR(255)  NOT NULL,
    tenant_type      VARCHAR(50),
    description      VARCHAR(1000),
    logo_url         VARCHAR(500),
    primary_color    VARCHAR(20),
    secondary_color  VARCHAR(20),
    company_name     VARCHAR(255),
    company_address  VARCHAR(500),
    company_email    VARCHAR(255),
    company_phone    VARCHAR(50),
    website          VARCHAR(500),
    plan_start_date  DATE,
    plan_expired_date DATE,
    created_by       VARCHAR(255)  NOT NULL DEFAULT 'SYSTEM',
    created_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by       VARCHAR(255),
    updated_at       TIMESTAMP,
    status           VARCHAR(3)    NOT NULL DEFAULT 'ACT'
);

CREATE INDEX idx_tenants_code        ON public.tenants (code);
CREATE INDEX idx_tenants_status      ON public.tenants (status);
CREATE INDEX idx_tenants_tenant_type ON public.tenants (tenant_type);
