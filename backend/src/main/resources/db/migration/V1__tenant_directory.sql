CREATE TABLE tenant (
  id UUID PRIMARY KEY,
  slug VARCHAR(80) NOT NULL UNIQUE,
  display_name VARCHAR(160) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE app_user (
  id UUID PRIMARY KEY,
  email VARCHAR(254) NOT NULL UNIQUE,
  full_name VARCHAR(160) NOT NULL,
  password_hash VARCHAR(100) NOT NULL,
  role VARCHAR(32) NOT NULL CHECK (role IN ('PLATFORM_ADMIN', 'ORG_ADMIN', 'MANAGER', 'EMPLOYEE')),
  tenant_id UUID NOT NULL REFERENCES tenant(id),
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE (id, tenant_id)
);

CREATE INDEX idx_app_user_tenant ON app_user(tenant_id);
