CREATE TABLE if not exists public.users (
    user_id VARCHAR(255) PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    application_id VARCHAR(100),
    user_type VARCHAR(50),
    register_channel VARCHAR(50),
    is_force_change_password VARCHAR(10),
    password_change_at TIMESTAMP,
    invalid_password_count INTEGER,
    invalid_password_at TIMESTAMP,
    invalid_otp_count INTEGER,
    invalid_otp_at TIMESTAMP,
    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP,
    deleted_by VARCHAR(255),
    deleted_at TIMESTAMP,
    status VARCHAR(3) DEFAULT 'ACT'
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone_number);
CREATE INDEX idx_users_status ON users(status);