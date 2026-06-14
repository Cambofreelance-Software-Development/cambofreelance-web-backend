CREATE TABLE if not exists public.user_refresh_tokens (
    id VARCHAR(255) PRIMARY KEY,
    refresh_token TEXT NOT NULL,
    access_token TEXT NOT NULL,
    expire_on TIMESTAMP NOT NULL,
    device_id VARCHAR(100),
    user_id VARCHAR(100) NOT NULL,
    client_id VARCHAR(100),
    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP,
    deleted_by VARCHAR(255),
    deleted_at TIMESTAMP,
    status VARCHAR(3) DEFAULT 'ACT'
);
CREATE INDEX idx_refresh_user_id ON user_refresh_tokens(user_id);
CREATE INDEX idx_refresh_expire ON user_refresh_tokens(expire_on);
CREATE INDEX idx_refresh_status ON user_refresh_tokens(status);