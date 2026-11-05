# Running the QA checks

Start the database and app first with `docker compose up --build` from the root. Keep that terminal open.

## Backend

```powershell
cd backend
.\mvnw.cmd test
```

Maven Wrapper files are not bundled in this initial scaffold. If the wrapper command is unavailable, install Maven 3.9+ and run `mvn test` from `backend`.

## API automation

From repository root:

```powershell
cd automation\api
mvn test
```

Requires Maven 3.9+ installed and running API. Override API URL with `mvn -Dtenantflow.api=http://localhost:8080 test`.

## Playwright

```powershell
cd automation\playwright
npm install
npx playwright install chromium
npm test
```

## Selenium

```powershell
cd automation\selenium
mvn test
```

Requires Maven and Chrome. Selenium Manager may download a matching driver on first run, which requires network access. Override the UI URL with `mvn -Dtenantflow.web=http://localhost:5173 test`.

## SQL checks

Connect to local database (`tenantflow` database, `tenantflow` user) and run [`automation/sql/assert-tenant-integrity.sql`](../automation/sql/assert-tenant-integrity.sql). To inspect tenant linkage manually:

```sql
SELECT t.slug, u.email, u.role
FROM app_user u JOIN tenant t ON t.id = u.tenant_id
ORDER BY t.slug, u.email;
```

To inspect the isolation predicate used by the API, compare the expected result for the Northwind tenant UUID `c9c908c2-12ce-48d5-bec3-4f808f3d2c11`:

```sql
SELECT email FROM app_user
WHERE tenant_id = 'c9c908c2-12ce-48d5-bec3-4f808f3d2c11';
```
