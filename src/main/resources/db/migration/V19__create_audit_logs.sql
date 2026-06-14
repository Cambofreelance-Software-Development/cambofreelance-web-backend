CREATE TABLE IF NOT EXISTS public.audit_logs (
    id          VARCHAR(36)   PRIMARY KEY,
    user_id     VARCHAR(36),
    username    VARCHAR(200),
    action      VARCHAR(100)  NOT NULL,
    module      VARCHAR(100)  NOT NULL,
    entity_id   VARCHAR(200),
    description TEXT,
    old_value   TEXT,
    new_value   TEXT,
    ip_address  VARCHAR(100),
    user_agent  VARCHAR(500),
    created_at  TIMESTAMP     NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_audit_logs_user_id    ON public.audit_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_module     ON public.audit_logs(module);
CREATE INDEX IF NOT EXISTS idx_audit_logs_action     ON public.audit_logs(action);
CREATE INDEX IF NOT EXISTS idx_audit_logs_created_at ON public.audit_logs(created_at DESC);
