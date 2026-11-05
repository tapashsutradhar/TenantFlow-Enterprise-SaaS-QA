# Architecture

## Runtime layout

```text
Browser (React / Vite) -- HTTP Basic over local HTTP --> Spring Boot REST API -- JDBC --> PostgreSQL
                                                           | Flyway migration
```

The browser sends credentials with API requests. Spring Security authenticates against the `app_user` table. `DatabaseUserDetailsService` returns the user's immutable tenant ID from their database membership. Controllers scope organization and member queries to that ID. No request parameter or header can override it.

## Data model

`tenant` owns `app_user`; each account belongs to one tenant in this first slice. `app_user.role` is constrained to the four product roles. PostgreSQL foreign keys and a `(id, tenant_id)` unique key provide a base for future tenant-scoped foreign keys. The member list query always includes the authenticated tenant ID.

## API

| Method | Route | Purpose |
| --- | --- | --- |
| GET | `/actuator/health` | Liveness/readiness response |
| GET | `/api/me` | Current identity and tenant summary |
| GET | `/api/organization` | Current organization |
| GET | `/api/members` | Members in current tenant |
| POST | `/api/members` | Add an invite-pending member (ORG_ADMIN only) |

The API returns standard HTTP status codes. Authentication uses HTTP Basic in this demo; use HTTPS and a proper identity/session design before deployment beyond a local machine.

## Decisions and known limits

- A single shared PostgreSQL schema with explicit tenant predicates keeps the demo simple. Production systems should consider row-level security, query-level enforcement, stronger isolation tests and tenant-scoped foreign keys throughout the domain.
- Member creation stores an invite-pending marker, not a password. There is no invitation or activation flow yet; such accounts cannot sign in.
- Demo tenants and accounts are seeded idempotently at application startup. Existing demo account passwords are not silently reset when environment variables change.
- The UI stores demo Basic credentials in tab-scoped `sessionStorage`; this is acceptable only for a local portfolio demo. Production auth should use secure session cookies or a well-reviewed OIDC flow.
- CORS only allows local Vite origins. Configure a deliberate allowlist when hosting the UI elsewhere.
