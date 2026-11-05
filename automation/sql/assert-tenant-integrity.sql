-- Run after the app has seeded its demo data. Exits with an error on failure.
DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM app_user u
    LEFT JOIN tenant t ON t.id = u.tenant_id
    WHERE t.id IS NULL
  ) THEN
    RAISE EXCEPTION 'Found an account without a valid tenant';
  END IF;

  IF EXISTS (
    SELECT 1 FROM app_user
    WHERE role NOT IN ('PLATFORM_ADMIN', 'ORG_ADMIN', 'MANAGER', 'EMPLOYEE')
  ) THEN
    RAISE EXCEPTION 'Found an unsupported account role';
  END IF;

  IF (SELECT count(*) FROM tenant WHERE slug IN ('northwind', 'contoso')) <> 2 THEN
    RAISE EXCEPTION 'Expected both seeded demo tenants';
  END IF;
END $$;

-- Expected: Northwind query returns only Northwind accounts.
SELECT u.email, t.slug
FROM app_user u JOIN tenant t ON t.id = u.tenant_id
WHERE t.id = 'c9c908c2-12ce-48d5-bec3-4f808f3d2c11'
ORDER BY u.email;
