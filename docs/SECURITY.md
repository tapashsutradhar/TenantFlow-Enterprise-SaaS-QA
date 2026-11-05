# Security notes

## Tenant boundary

Authenticated identity is loaded from the database, and its tenant ID is carried in the security principal. Organization and member queries use that principal value. The API ignores a `tenantId` query parameter. The API tests include cross-tenant response checks; a full review should add mutation, enumeration, role-matrix and database isolation cases.

## Demo-only limitations

- HTTP Basic credentials are sent on each request. Use only on localhost, or behind HTTPS with a deliberate identity design.
- Demo credentials in `.env.example` are public examples. Change them before running and never reuse them.
- UI credentials remain in tab-scoped `sessionStorage` until sign-out/tab close.
- The API currently returns framework-default error payloads for some failures; normalize them before public deployment.
- Member records created from the UI are invite-pending and have no activation flow.
- CSRF is disabled because this demo uses Basic auth; this choice must be revisited with cookie-based sessions.

## Before any shared deployment

Use a managed identity provider, TLS, secrets management, secure session handling, rate limits, explicit account lifecycle, audit trails, dependency scanning and security review. Replace demo seeding and validate tenant access on every new data path.
