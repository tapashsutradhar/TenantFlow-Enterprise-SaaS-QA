# Product brief

## Problem

Growing companies often manage user access, approvals and subscription details across disconnected tools. TenantFlow is a single workspace concept for keeping that operational context organized while keeping each customer's data separate.

## Users and roles

- **Platform admin**: platform-wide operations (planned).
- **Organization admin**: manages people and workspace settings. The current demo account has this role.
- **Manager**: reviews assigned requests (planned).
- **Employee**: submits and tracks requests (planned).

## Product areas

1. Authentication and session management.
2. Tenant and organization management.
3. User and role administration.
4. Subscription and licence management.
5. Workflow and approval management.
6. Notifications and audit logs.
7. Dashboard and reporting.

## Current slice

This repository implements a local demo sign-in, tenant context resolved from the authenticated account, organization summary and member directory with organization-admin member creation. It does not yet implement invitations, user onboarding, platform administration, subscriptions, approval workflows, notifications, audit history or reporting beyond the small workspace summary.

## Product constraints

- Tenant identity comes from server-side membership, never from a caller-controlled tenant selector.
- User roles do not grant cross-tenant access.
- Demo credentials are local-only and must be replaced for any shared deployment.
- Changes that affect access should eventually have audit events and explicit lifecycle controls.
