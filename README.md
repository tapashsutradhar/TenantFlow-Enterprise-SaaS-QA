# TenantFlow

TenantFlow is a portfolio project for managing organizations, members and access in a multi-tenant SaaS workspace. It pairs a small working product slice with a QA/SDET suite that exercises the same tenant boundary through the UI, API and database.

## What is implemented

- Spring Boot REST API with PostgreSQL persistence, Flyway migrations, validation and tenant-scoped organization/member endpoints.
- React + TypeScript workspace with organization summary and member directory.
- Tenant isolation enforced on the server: the authenticated principal's membership determines the active tenant; callers cannot select another tenant by changing a request parameter.
- Selenium/Java and Playwright/TypeScript UI examples, REST Assured API checks and SQL data assertions.
- Docker Compose development stack and GitHub Actions checks.

Authentication currently uses HTTP Basic with configurable demo accounts so the sample can be run without an external identity provider. It is a development foundation, not a production identity design. See [security notes](docs/SECURITY.md) and [implementation status](docs/IMPLEMENTATION-STATUS.md).

## Stack

Java 21 · Spring Boot 3.5 · Maven · React 19 · TypeScript · Vite · PostgreSQL 17 · Flyway · Docker Compose

## Run locally (Windows 11 / PowerShell)

Prerequisites: JDK 21, Maven 3.9+, Node.js 22+, npm, Git and Docker Desktop with Compose v2.

```powershell
Copy-Item .env.example .env
docker compose up --build
```

Open <http://localhost:5173>. The API is at <http://localhost:8080/api>; health check: <http://localhost:8080/actuator/health>.

Demo accounts (change these in `.env` before running): `admin@northwind.test` / `Northwind-Dev-Only!` and `admin@contoso.test` / `Contoso-Dev-Only!`. Each account belongs to exactly one tenant. Keep `.env` out of Git; never use these credentials outside a local demo.

To stop the services:

```powershell
docker compose down
```

To remove the local database volume too:

```powershell
docker compose down -v
```

## Checks

Run these from the repository root. UI tests require the app running as above. Selenium requires Chrome installed; Playwright installs its own browser.

```powershell
cd backend; mvn test; cd ..
cd frontend; npm ci; npm run build; cd ..
cd automation\api; mvn test; cd ..\..
cd automation\playwright; npm ci; npx playwright install chromium; npm test; cd ..\..
cd automation\selenium; mvn test
```

Tests are supplied as executable checks, but no results are claimed here; run them in your environment and review the output. Full local automation instructions are in [QA guide](docs/QA-GUIDE.md).

## Repository map

```text
backend/                 Spring Boot API, migration and unit/integration tests
frontend/                React + TypeScript application
automation/api/           REST Assured API tests
automation/playwright/    Playwright UI and tenant-isolation checks
automation/selenium/      Selenium WebDriver Java UI checks
docs/                     Product, architecture, security and QA documents
.github/workflows/        Backend, frontend and automation CI
```

```
TenantFlow-Enterprise-SaaS-QA/
├── .env.example
├── .github/
│   └── workflows/
│       ├── automation.yml
│       ├── backend.yml
│       └── frontend.yml
├── .gitignore
├── README.md
├── automation/
│   ├── api/
│   │   ├── pom.xml
│   │   └── src/test/java/com/tenantflow/qa/TenantIsolationApiTest.java
│   ├── playwright/
│   │   ├── package-lock.json
│   │   ├── package.json
│   │   ├── playwright.config.ts
│   │   └── tests/workspace.spec.ts
│   ├── selenium/
│   │   ├── pom.xml
│   │   └── src/test/java/com/tenantflow/qa/WorkspaceLoginTest.java
│   └── sql/
│       └── assert-tenant-integrity.sql
├── backend/
│   ├── .dockerignore
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/tenantflow/
│       │   │   ├── TenantFlowApplication.java
│       │   │   ├── config/SecurityConfig.java
│       │   │   ├── directory/DirectoryController.java
│       │   │   └── security/
│       │   │       ├── DatabaseUserDetailsService.java
│       │   │       └── TenantUserDetails.java
│       │   └── resources/
│       │       ├── application.yml
│       │       └── db/migration/V1__tenant_directory.sql
│       └── test/java/com/tenantflow/directory/DirectoryControllerTest.java
├── docker-compose.yml
├── docs/
│   ├── ARCHITECTURE.md
│   ├── IMPLEMENTATION-STATUS.md
│   ├── PRD.md
│   ├── QA-GUIDE.md
│   ├── SECURITY.md
│   ├── TEST-CASES.md
│   └── TEST-PLAN.md
└── frontend/
    ├── .dockerignore
    ├── Dockerfile
    ├── index.html
    ├── package-lock.json
    ├── package.json
    ├── tsconfig.json
    ├── vite.config.ts
    └── src/
        ├── App.tsx
        ├── api.ts
        ├── main.tsx
        ├── styles.css
        └── vite-env.d.ts
```

## Scope

The current slice covers authentication for local demo users, tenant context, organization summary and member directory. Subscription/licence management, approval workflows, notifications/audit history and reporting are documented as next increments; they are not represented as implemented features.

## License

MIT. See [LICENSE](LICENSE).
