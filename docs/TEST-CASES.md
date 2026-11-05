# Core test cases

| ID | Scenario | Expected result | Level |
| --- | --- | --- | --- |
| AUTH-01 | Read members without credentials | `401` | API |
| AUTH-02 | Sign in with a valid seeded account | Workspace for that account appears | UI/API |
| AUTH-03 | Use an incorrect password | `401`; no workspace data returned | API/UI |
| TEN-01 | Northwind lists members | Every result belongs to Northwind | API/SQL |
| TEN-02 | Add a foreign `tenantId` query parameter | Context remains Northwind | API |
| TEN-03 | Sign in as Contoso | Contoso workspace appears; Northwind member is absent | UI/API |
| TEN-04 | Organization admin creates a member | `201`; record is linked to the principal's tenant | API/SQL |
| AUTHZ-01 | Non-admin posts a member | `403` | API |
| VAL-01 | Submit malformed email or blank name | `400` with validation feedback | API |
| VAL-02 | Add an email already used | `409` | API |
| DB-01 | Insert unsupported role directly | Check constraint rejects the row | SQL |
| DB-02 | Insert user with missing tenant | Foreign key rejects the row | SQL |

Cases marked in this document describe intended coverage. Only cases present in executable test code are automated; other cases remain manual or planned.
