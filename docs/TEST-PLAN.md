# Test plan

## Goal

Check the core workspace path and, especially, verify that tenant boundaries are enforced by the API rather than by presentation logic.

## Coverage

- Authentication: valid demo account, wrong password, unauthenticated access.
- Tenant isolation: tenant A sees only tenant A members; tenant selector tampering does not change context; tenant B remains isolated.
- Authorization: only organization admins may add members; invalid email/role is rejected; duplicate email is handled.
- UI: sign-in, workspace identity, directory contents, add-member response and sign-out.
- Data: membership foreign key and role constraint; directory query constrained to the principal tenant.
- Regression: health endpoint, profile, organization and member listing.

## Test levels

1. Backend unit/integration checks with Spring Boot and MockMvc.
2. API contract/security checks using REST Assured.
3. Browser behavior using Playwright; Selenium example retained for Java-based UI automation practice.
4. SQL assertions against an isolated local database after the API checks.

## Entry and exit

Entry: JDK 21, Node 22+, Docker Desktop available; local stack healthy. Exit: checks complete with results reviewed, no unresolved critical tenant-isolation defect, and observed failures documented. This repository does not claim those exit criteria have been met.

## Test data and cleanup

Compose creates two stable demo tenants. Use unique email addresses for member-creation cases. Database data is persistent until `docker compose down -v`; do not use this stack for real customer information.
