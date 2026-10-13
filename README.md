# WORKSY Backend — Progress Snapshot (Stage 1: Foundation + Auth)

This is an in-progress build of the WORKSY backend. **This stage is real, working code** —
not a stub — but it covers the foundation + authentication only. Later stages (jobs, ATS
scoring, interviews, notifications, DSA module, admin, frontend) will build on top of this.

## What's included and working end-to-end

- **Project setup**: Maven `pom.xml` with Spring Boot 3.3.4, Spring Security, Spring Data JPA,
  JWT (jjwt), MySQL driver, Mail, WebSocket, Swagger/OpenAPI, PDFBox/POI (for future resume parsing).
- **All 17 JPA entities** with real relationships (`@OneToOne`, `@OneToMany`, `@ManyToOne`,
  `@ManyToMany`): User, Role, HRProfile, Company, Job, JobApplication, Resume, Education,
  Experience, Skill, Interview, Notification, DsaProblem, PracticeSheet, PracticeProgress,
  PracticeSheetProgress, PasswordResetToken.
- **All repositories** (Spring Data JPA) for the above.
- **Full JWT authentication + role-based authorization**:
  - `JwtTokenProvider` — signs/validates tokens, embeds id/email/type/roles.
  - `JwtAuthenticationFilter` — validates the `Authorization: Bearer <token>` header on every request.
  - `CustomUserDetailsService` — looks up credentials across both the `users` and `hr_profiles`
    tables (Admin is a User with `ROLE_ADMIN`).
  - `SecurityConfig` — stateless JWT security, BCrypt password hashing, CORS, and
    **route-level role authorization**: `/api/admin/**` → `ROLE_ADMIN`, job write endpoints →
    `ROLE_HR`, job browsing (`GET /api/jobs`) → public, everything else → any authenticated role.
  - `JwtAuthenticationEntryPoint` — clean JSON 401 responses instead of default Spring error pages.
- **Auth API** (`/api/auth/**`), fully wired to the database — not mocked:
  - `POST /api/auth/register` — job seeker registration
  - `POST /api/auth/hr/register` — HR registration (creates/links a Company, sets `approved=false`
    pending admin approval)
  - `POST /api/auth/login`, `/api/auth/hr/login`, `/api/auth/admin/login`
  - `POST /api/auth/forgot-password`, `/api/auth/reset-password` (token-based, 30-min expiry)
- **Global exception handling** (`@RestControllerAdvice`) with proper HTTP status codes and
  JSON error bodies for not-found, invalid credentials, duplicate resources, validation errors, etc.
- **Bean Validation** on all auth DTOs (`@NotBlank`, `@Email`, `@Size`, ...).
- **Data seeding on startup**: the 3 roles, plus a default Admin account:
  - `admin@worksy.com` / `Admin@123` (change this before production).
- **Email service**: real Spring Mail integration; when `MAIL_ENABLED` is not set, emails are
  logged instead of sent, so you can run the whole app without SMTP credentials configured yet.

## What's NOT built yet (coming in the next stages)

Job posting/search/apply endpoints, ATS scoring, resume upload, interview scheduling,
WebSocket notifications, DSA practice module, Admin management endpoints, the React frontend,
Swagger annotations on every controller, tests, and the full sample-data seeder (companies/jobs/
DSA questions). The entities and repositories for all of these already exist and are ready to
be wired up.

## How to run this stage

**Prerequisites:** Java 17+, Maven, MySQL running locally.

1. Create the database (or let Hibernate auto-create it — already configured via
   `createDatabaseIfNotExist=true`):
   ```sql
   CREATE DATABASE worksy_db;
   ```
2. Set your MySQL credentials (env vars, or edit `application.properties` directly):
   ```
   export DB_USERNAME=root
   export DB_PASSWORD=yourpassword
   ```
3. Run it:
   ```
   cd worksy-backend
   mvn spring-boot:run
   ```
4. On startup, Hibernate creates all tables (`ddl-auto=update`) and the seeder creates the
   3 roles + default admin.
5. Test the auth flow with curl/Postman:
   ```
   POST http://localhost:8080/api/auth/register
   { "fullName": "Test User", "email": "test@example.com", "password": "test123" }

   POST http://localhost:8080/api/auth/login
   { "email": "test@example.com", "password": "test123" }
   ```
   You'll get back a JWT in the response — use it as `Authorization: Bearer <token>` on
   protected routes.

## Environment variables (production)

Never commit real secrets. Set these instead of using the dev defaults in
`application.properties`:
`DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET` (32+ chars), `MAIL_USERNAME`, `MAIL_PASSWORD`,
`MAIL_ENABLED=true`, `CORS_ORIGINS`.
