CREATE TABLE if not exists public.response_codes (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),
    code VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255),
    http_status VARCHAR(50),
    key VARCHAR(255),
    message_en VARCHAR(255),
    message_cn VARCHAR(255),
    message_km VARCHAR(255),
    type VARCHAR(255),
    service_type VARCHAR(100),
    deleted_at TIMESTAMP,
    deleted_by VARCHAR(100),
    status VARCHAR(3) DEFAULT 'ACT'
);