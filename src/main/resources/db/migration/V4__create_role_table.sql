CREATE TABLE if not exists public.roles (
    id VARCHAR(36) PRIMARY KEY,
    code VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    convention VARCHAR(100),
    level INTEGER,
    status VARCHAR(100),
    description TEXT
);