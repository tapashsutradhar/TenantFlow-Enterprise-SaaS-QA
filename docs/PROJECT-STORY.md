# TenantFlow: Project Specification and Story

## The idea in one minute

TenantFlow is a portfolio project about a common SaaS problem: several organizations use the same software, but each organization must only see its own people and information.

Imagine a shared office building. Different companies use the same building, but each company has its own locked office. TenantFlow applies that idea to software. Organizations share the application, while the system keeps their workspaces separate.

The current project is a small working foundation: a user can sign in to a local demo workspace, see their organization and its members, and an organization admin can add an invite-pending member. It is not a finished commercial product, and the demo accounts are not for real organizations.

## Why I created it

I created TenantFlow as a portfolio project to learn and demonstrate how a product idea can be carried into an application: defining the problem, choosing an architecture, building a user interface and API, planning database changes, and designing tests.

I chose multi-tenancy because data separation is central to business software. A page can look correct while an API still exposes another organization's records. That makes tenant isolation an important engineering and QA concern, not just a visual feature.

## Who the project is for

### People who might use a product like this

- **Organization admins** need a place to review their workspace and manage who belongs to it.
- **Managers** could review team requests as approval features are developed.
- **Employees** could submit and follow requests as those workflows are developed.
- **Platform admins** could manage organizations across the service in a later version.

Only the local organization-admin directory flow is represented in the current implementation. The other roles and related product areas help define the future direction; they are not finished features.

### People who can learn from or review this project

- **QA and SDET reviewers** can examine the planned UI, API, and database checks, particularly the tenant-isolation cases.
- **Developers** can review the Java/Spring API, React/TypeScript interface, PostgreSQL schema, and migration approach.
- **Recruiters and hiring teams** can use the repository to understand the project scope, the choices made, and what remains to be verified.
- **Nontechnical readers** can use the plain-language sections in this document to understand the product goal without needing to know the frameworks.

TenantFlow is a learning and portfolio example. It has no claimed production users or customer outcomes.

## The problem and proposed benefit

As organizations grow, employee lists, approvals, subscription information, and operational updates can end up spread across different tools. A single workspace could make those tasks easier to find and manage.

The main risk in a shared SaaS product is that one organization's user might see another organization's information. TenantFlow addresses this in its current directory slice by resolving the organization from the authenticated account on the server and using that organization when reading records. The browser does not get to choose which tenant it can access.

The intended benefit is clearer organization-level access and a foundation for related management workflows. This benefit is a product goal, not a measured result from real users.

## What the product is intended to include

The broader product concept has seven areas:

1. Authentication and session management.
2. Tenant and organization management.
3. User and role administration.
4. Subscription and licence management.
5. Workflow and approval management.
6. Notifications and audit logs.
7. Dashboard and reporting.

The current code implements a smaller first slice: local demo sign-in, an organization summary, a tenant-scoped member directory, and organization-admin member creation. Member creation is invite-pending; invitation delivery and account activation are not implemented. The remaining product areas are planned, not available features.

## How the current slice works

1. A user signs in with one of the local demo accounts.
2. The API authenticates the account against the database.
3. The account's database membership supplies its tenant identity.
4. The API uses that identity to return the organization and its members.
5. The React interface displays the workspace data returned by the API.

If a caller adds another tenant's ID to a request, the API should still use the authenticated account's tenant. The browser is not treated as the authority for access.

## Technology, explained simply

| Technology | What it does in this project |
| --- | --- |
| Java 21 and Spring Boot | Run the backend API and server-side access checks. |
| React and TypeScript | Build the screens and make API requests from the browser. |
| PostgreSQL | Store organizations and member accounts. |
| Flyway | Apply versioned database schema changes in order. |
| Docker Compose | Start the database, API, and web interface together for local development. |
| Selenium and Playwright | Automate browser scenarios using Java and TypeScript. |
| REST Assured | Send API requests in automated checks. |
| SQL | Inspect database relationships and tenant data. |
| GitHub Actions | Describe repeatable build and test jobs when code is pushed or a workflow is run. |

The demo currently uses HTTP Basic authentication and local demo credentials. This is a development arrangement, not a production identity system.

## Difficulties I considered and how I approached them

### Keeping organizations' data separate

**Difficulty:** A client can send a tenant ID that does not belong to the signed-in user. Trusting it could expose another organization's records.

**Approach:** Resolve the tenant from the authenticated account on the backend and include that tenant in directory queries. Add API and browser scenarios that check one demo organization does not receive the other one's member data. The automated scenarios are included in the repository; the full backend and browser suites still need to be run in a suitable environment.

### Keeping the project achievable

**Difficulty:** The original product concept spans seven substantial modules. Building all of them at once would make it hard to tell what is implemented and what has only been designed.

**Approach:** Start with sign-in, organization context, and the member directory. Keep the other modules in the product brief and label them planned in the implementation status document.

### Making the tools agree

**Difficulty:** The frontend, API, database, and test tools each need compatible configuration. For example, the TypeScript frontend needs Vite's type definitions to understand environment variables.

**Approach:** Use sample configuration, a database migration, Docker Compose, and separate CI workflows. During frontend verification, adding the Vite type declaration resolved a build error, and the frontend production build then passed.

### Being honest about verification

**Difficulty:** Source code and test files do not prove that an application or test suite has actually run successfully.

**Approach:** Record the checks that were run and leave other checks as unverified. The frontend build passed. Backend and full-stack tests have not been verified in this environment because Java 21, Maven, and Docker were not all available together.

## How I used AI

I used AI as an assistant during planning and implementation. It helped me:

- break a broad product idea into a first deliverable and later features;
- compare the product requirements with the code and documentation;
- draft and refine API, UI, database, and automation examples;
- create test scenarios for authentication and tenant isolation;
- troubleshoot the frontend build and improve the setup instructions; and
- review wording so technical and nontechnical readers can follow the project.

AI suggestions are not proof that code is secure or correct. I reviewed the proposed changes, used available build output as evidence, and kept backend/full-stack checks marked unverified rather than presenting them as passing. I do not put real credentials or private customer information into AI prompts.

### A practical way to ask AI for project help

Give the assistant the context it needs, one specific task, and a way to check the result. For example:

> I am working on TenantFlow, a Java 21/Spring Boot and React/TypeScript portfolio project. The current feature is a tenant-scoped member directory. Review this API method for cross-tenant access risks. Explain any issue in plain English, suggest the smallest code change, and add a test that demonstrates the behavior. Do not claim the test passed unless you run it and show the result.

For a nontechnical explanation, a useful prompt is:

> Explain TenantFlow to someone who does not work in software. Use a familiar example, describe who would use it, and clearly separate what works today from what is planned.

AI is most useful here as a partner for thinking, drafting, and debugging. The project owner remains responsible for checking the code, running the tests, protecting secrets, and deciding what to publish.

## Plain-language explanation for readers without a technical background

TenantFlow is like a set of private offices inside one shared office building. Each company gets its own workspace. People from one company should not be able to open another company's member list.

Today, the demonstration lets a sample organization admin sign in and see that organization's name and member list. The project also includes examples of checks intended to catch a mix-up between organizations. Subscription management, approvals, notifications, and other features are ideas for future work; they are not ready to use yet.

## How I would evaluate whether the project is improving

Before claiming an impact, I would gather evidence such as:

- backend tests passing against the configured database;
- browser tests showing that each demo account only sees its own organization;
- validation and authorization tests for member creation;
- a successful CI run on GitHub; and
- feedback from people who try the interface or review the scenarios.

No performance improvement, adoption figure, or customer outcome is claimed at this stage.

## Current status and next steps

- The frontend production build has passed in the development environment.
- The backend and end-to-end test suites remain unverified here.
- The demo authentication must be replaced before any shared or production deployment.
- Next work should verify the stack with the documented Java, Maven, and Docker versions; run the automated checks; then add the invitation lifecycle and expand role/authorization coverage.

For concise scope and status, see [the product brief](PRD.md), [implementation status](IMPLEMENTATION-STATUS.md), [architecture](ARCHITECTURE.md), and [QA guide](QA-GUIDE.md).
