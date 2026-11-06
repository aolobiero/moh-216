# MOH 216 Implementation Progress

Updated: 2026-11-06

## Phase 1: Project audit

Status: complete.

### Confirmed working

- Spring Boot 4.1.2 backend builds with Java 21 and uses Spring Data JPA.
- PostgreSQL is configured through environment-backed settings in
  `backend/src/main/resources/application.properties`.
- Existing mother registration, listing, lookup, and QR-code endpoints exist.
- The `mothers` table and QR-token migrations are present in `database/`.
- React/Vite frontend and the staff login flow exist.
- The frontend proxies `/api` requests to the backend on port 8080.

### Partially implemented

- Mother registration has a form and list, but no edit/profile view, pagination,
  server-side search, validation response model, or authorized archive workflow.
- The dashboard displays hardcoded zeroes instead of backend statistics.
- Navigation exposes Children, Immunizations, Appointments, Health Records,
  Reports, and Settings, but those entries render placeholders.
- Authentication currently relies on Spring Security's generated in-memory
  development user and HTTP Basic credentials. It is not suitable for
  production staff or facility-level authorization.
- Error handling is currently mostly browser alerts and console logging.

### Not implemented

- Pregnancy and ANC visit records.
- Labour and delivery records.
- Postnatal visits.
- Child profiles and child health visits.
- Configurable immunization catalogue, schedules, and administration records.
- Growth monitoring with verified WHO reference data.
- Appointments, referrals, reports, staff/facility management,
  notifications, and audit logs.
- Flyway or an equivalent controlled migration runner.
- DTOs, global exception handling, pagination, role-based authorization,
  integration tests, and production deployment configuration.

## First vertical slice: dashboard summary

Status: implemented and build-verified.

- Added `GET /api/dashboard/summary`, protected by the existing security chain.
- The endpoint reports the real mother count and five most recent registrations.
- The dashboard now loads the mother count from PostgreSQL.
- Child, appointment, and immunization cards explicitly show
  `Not available` until those data models exist; no fabricated statistics are
  displayed.

## Child registration and management

Status: implemented and focused-test verified. PostgreSQL integration is
blocked until the additive migration is applied to the configured database.

### Completed

- Added additive `database/004_create_children.sql` migration with a foreign
  key to `mothers`, QR token, indexes, and a positive birth-weight constraint.
- Added `Child` entity, `ChildRequest` and `ChildResponse` DTOs,
  `ChildRepository`, `ChildService`, and `ChildController`.
- Added `POST /api/children`, `GET /api/children?search=`, and
  `GET /api/children/{id}` endpoints.
- Added mother existence validation and `404 Not Found` handling for missing
  mother or child records.
- Added child registration form, mother selector, client-side search, loading
  and error states, and a child profile view.
- Added real registered-child count to the dashboard summary.
- Added synthetic-data service tests for successful registration and missing
  mother rejection.

### Verification

- `mvn -Dtest=ChildServiceTest test`: 2 tests passed.
- `mvn -DskipTests package`: passed.
- `npm run build`: passed.
- `git diff --check`: passed.

### Outstanding

- Apply `database/004_create_children.sql` to the intended development
  database before starting the application with the child module enabled.
- Add authenticated controller integration tests after the development
  migration is applied.
- Production staff identities, role-based authorization, facility scoping,
  audit logging, and secure session handling remain blockers.

## Current risks and blockers

1. A valid `DB_PASSWORD` must be supplied through the environment; no database
   credential is committed.
2. The current generated Spring Security user is development-only.
3. Clinical schedules, growth references, and alert rules require verified
   Kenyan Ministry of Health or WHO source data before implementation.
4. The database currently contains only the mother record model, so later
   modules require additive migrations and relationship design.

## Antenatal care (ANC)

Status: implemented in backend and frontend; database integration remains
blocked until migrations are applied.

### Added

- Additive `database/005_create_pregnancies_and_anc_visits.sql` migration.
- Pregnancy and ANC visit entities, repositories, request DTOs, service, and
  secured REST controller.
- Pregnancy creation/listing and ANC visit creation/history endpoints.
- Validation for required mother/pregnancy references and positive observations.
- ANC React workflow with pregnancy selection, pregnancy creation, visit
  capture, and visit history.

### Verification

- `mvn -DskipTests package`: passed.
- `npm run build`: passed after the backend additions.
- ANC unit tests cover pregnancy creation, missing pregnancy rejection, and
  visit recording.
- Database migration and endpoint integration tests are pending because the
  child migration has not yet been applied to the configured PostgreSQL
  database.

### Next steps

1. Apply migrations 004 and 005 to the development database.
2. Add authenticated integration tests for child and ANC endpoints.
3. Run database-backed tests against the migrated development database.
4. Only then begin PNC.

## Integration verification update

The requested database verification was not rerun or altered by this session.
The user confirmed that migrations 004 and 005 are already applied and that
the expected foreign keys exist.

### Results

- Child and ANC unit tests: **5 passed**.
- Frontend production build: **passed**.
- Backend compile/package build: **passed**.
- Unauthenticated API request: **401**, confirming the security filter is
  active.
- Full Spring context test: **blocked** because PostgreSQL rejected the
  `DB_PASSWORD` available to the test process for `moh216_user`.
- Authenticated child, ANC, persistence, dashboard, and QR endpoint checks:
  **pending valid database and development-user credentials**.
- Existing QR implementation remains present and unchanged; live QR response
  verification is pending authenticated API access.

No synthetic records were inserted because authenticated database access was
not available, and no existing data was modified.

## PNC preparation

PNC architecture is documented in `docs/PNC_ARCHITECTURE.md`.

Migration `database/006_create_pnc_records.sql` was approved and executed
against the development database. Verification confirmed:

- `pnc_visits` exists and references `mothers(id)`.
- `newborn_followups` exists and references `children(id)`.
- Both supporting indexes were created.

No existing tables or records were deleted or altered.

PNC backend and frontend implementation is the next development step.

## Settings module

Status: first settings slice implemented; migration pending approval.

### Implemented

- Added additive `database/007_create_settings.sql` for staff profiles,
  facility settings, user preferences, and audit logs.
- Added authenticated profile and preference REST endpoints.
- Added administrator-protected facility settings, audit list, and database
  health endpoint.
- Added BCrypt password encoder infrastructure and method security.
- Added responsive Settings page with profile, facility, notification, and
  appearance sections.
- Added persisted light/dark theme and text-size preferences.
- Added honest UI messaging that SMS/email delivery and password changes for
  the generated development user are not enabled.

### Not yet implemented

- Database-backed staff accounts, roles, password change flow, MFA, photo
  storage, notification delivery providers, patient consent management,
  searchable audit UI, export/backup workflows.
- These require identity, storage, delivery, and operational decisions and
  must not be represented as active features.

### Verification

- Backend package build: passed.
- Frontend production build: passed.
- Migration 007: created but not executed.

## Theme system update

Status: implemented and build-verified.

- Replaced the Light/Dark selector with Light, Midnight Navy, Dusty Plum, and
  Rose Charcoal.
- Added shared CSS design tokens for page surfaces, cards, borders, text,
  accents, sidebar gradients, and focus-friendly controls.
- Applied coordinated theme styling across the dashboard, navigation, forms,
  tables, Settings, Children, and ANC pages.
- Theme and text-size preferences apply immediately and persist in browser
  storage; server persistence uses the existing preferences endpoint when
  migration 007 is available.
- Settings requests now use `Promise.allSettled`, so an admin-only facility
  failure does not hide profile or appearance settings.
- API errors include the exact HTTP method and endpoint for diagnosis.

The reported Settings `404` is not caused by the frontend route or Vite proxy:
the client calls `/api/settings/profile`, `/api/settings/preferences`, and
`/api/settings/facility`, matching `SettingsController`. A running backend
process must be rebuilt/restarted with the controller and migration 007
available; otherwise the old process can return 404 (or the new process can
return a schema error if migration 007 is absent). Migration 007 was not
executed by this change.

## Health Records, Appointments, Immunizations, and Reports

Status: initial additive vertical slices implemented and build-verified.

### Completed

- Added `database/008_create_records_appointments_immunizations.sql` without
  executing it or modifying existing records.
- Added longitudinal health-record entries linked to exactly one mother or
  child, including record type, date, author, notes, and a restricted-entry
  flag.
- Added appointment booking and month-window listing with patient linkage,
  appointment type, facility, provider, status, and notes.
- Added child immunization administration records with vaccine name, dose
  label, administration date, provider, and notes. No vaccine schedule was
  invented.
- Added aggregate report counts for scheduled appointments and recorded
  immunizations.
- Replaced the Appointments, Immunizations, Health Records, and Reports
  placeholders with functional React forms and history/summary views.
- Preserved the existing ANC workflow; it remains available for follow-up
  integration rather than being represented as a new health-record type.
- Added `CLINICAL_REQUIREMENTS.md` with sources, publication dates, review
  gates, and current-guidance limitations.
- Removed competing inline login styles and made responsive login positioning
  controlled by its stylesheet.

### Verification

- Backend `mvn -DskipTests package`: passed.
- Frontend `npm run build`: passed.
- `git diff --check`: passed.

### Remaining blockers

- Migration 008 is not executed; database-backed verification requires
  explicit approval and valid local PostgreSQL credentials.
- Sensitive health-record access currently has a schema flag but needs
  database-backed staff identities, role/facility scoping, and audited
  authorization before production use.
- Reports are aggregate-only. Authorized exports, growth charts, current
  Kenyan immunization schedules, reminders, and clinical alerts require
  approved source versions and clinical governance.
- Endpoint integration tests and live authenticated verification remain
  pending database access.

## UI and immunization search update

Status: implemented and build-verified.

- Kept the login background, artwork, colors, fonts, and branding unchanged;
  adjusted only the final card translation to leave the seal visible, with a
  smaller mobile offset.
- Added a searchable vaccine catalogue to immunization entry. The catalogue
  contains BCG, OPV, IPV, pentavalent, PCV, rotavirus, measles-rubella,
  yellow-fever, and HPV vaccine names.
- Vaccine timing, dose counts, eligibility, and geographic requirements remain
  review-gated because the current public Kenya MoH site did not expose a
  reliably dated schedule during research. The UI does not claim that the
  catalogue is the current official schedule.
- Added patient search by name, phone, or county to the mother API and by name
  or linked mother in Health Records.

## Verification completed

- `mvn -DskipTests package` succeeds in `backend/`.
- PostgreSQL accepts connections on localhost:5432.
- The backend starts on port 8080 when valid database credentials are set.
- The frontend development server responds on port 5173.
- Removed a duplicate table-body element that prevented the mother page from
  compiling reliably.
- `npm run build` succeeds in `frontend/`.
- `git diff --check` passes.

`npm run lint` still reports three existing JSX `no-unused-vars` errors for
icon components in `frontend/src/App.jsx`; this is a tooling configuration
issue because the current ESLint setup does not recognize JSX component usage.

## Implementation order

1. Dashboard statistics and reliable frontend loading/error states.
2. Mother profiles, search, validation, and controlled updates.
3. Pregnancy and ANC vertical slice.
4. Delivery and postnatal vertical slices.
5. Child records, immunization, and growth monitoring.
6. Appointments, referrals, reports, staff roles, notifications, and audits.
7. Security hardening, integration tests, and deployment preparation.

Modules are only marked complete after implementation and focused verification.
