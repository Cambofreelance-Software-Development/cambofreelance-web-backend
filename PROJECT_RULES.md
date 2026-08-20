# PROJECT_RULES.md

Rules and conventions for AI assistants working in this repository. This file reflects the
**actual current state** of the codebase, including its inconsistencies. Where the codebase
disagrees with itself, this file tells you which variant to follow for new code.

## 1. Project Overview

Java 21 / Spring Boot 3.2.5 backend (Gradle), package root `com.cambofreelance.webbackend`.
It is a CMS-style API for a marketing/hardware-pricing site (articles, authors, hardware,
pricing plans, features, testimonials, FAQs, media, CMS settings) plus its own auth/user/role
system. Packaged as a WAR-capable Spring Boot app, deployed as a Docker container behind
Postgres, Redis, and MinIO.

### Entry point

- `src/main/java/com/cambofreelance/webbackend/WebBackendServiceApplication.java` —
  `@SpringBootApplication(scanBasePackages = "com.cambofreelance.webbackend", exclude = UserDetailsServiceAutoConfiguration.class)`,
  `@EnableScheduling`. On `@PostConstruct` it calls `Startup.initApiMigrate()`.
- `startup/Startup.java` → `registry/ApiMigrateRegistry.java`: runs on every boot to (a) load
  response codes into the Redis/in-memory cache, and (b) seed default roles
  (`ADMIN`, `USER`, `CREATOR_USER`, `PUBLIC_USER`) and a default admin user if none exist.
  This is the app's boot-time seeding mechanism — there are no `CommandLineRunner` beans.
- Database schema is owned entirely by **Flyway** (`spring.jpa.hibernate.ddl-auto: none`).
  Migrations live in `src/main/resources/db/migration`.

### Layered architecture

One feature = one vertical slice through these layers (see `ArticleController` /
`ArticleService` / `ArticleServiceImpl` / `ArticleRepository` / `ArticleEntity` /
`ArticleCreateRequest` / `ArticleUpdateRequest` / `ArticleResponse` as the canonical example):

```
controllers/        @RestController — thin. Validates (@Valid), calls ONE service method,
                     wraps the result in MessageResponse, returns ResponseEntity.
                     No business logic here.
services/            Service interfaces (XxxService).
services/impl/       @Service implementations (XxxServiceImpl). All business logic,
                     entity<->DTO mapping, slug generation, @Transactional boundaries live here.
repository/          Spring Data JPA interfaces extending JpaRepository, plus @Query methods.
entities/            @Entity classes, all extend entities/BaseEntity.
dto/request/         Inbound request DTOs.
dto/response/        Outbound response DTOs.
dto/taxonomy/        Generic cross-cutting DTOs: PaginationRequest, FilterRequest,
                     PaginateResponse<T>, PaginationMetadata.
configs/             @Configuration classes (SecurityConfig, RedisConfig, BeanConfig,
                     OpenApiConfig, WebMvcConfig, CustomAuthenticationEntryPoint).
filters/             Servlet filters (AuthTokenFilter, IpWhitelistFilter, MutableHttpServletRequest).
audit/               @Auditable annotation + AuditAspect (AOP) — logs create/update/delete/
                     status-change actions with before/after snapshots.
caches/              Redis-backed + in-memory caches (TokenRedisCache, ResponseCodeRedisCache,
                     ResponseManagerCache, IpWhitelistCache, PasswordResetCache).
constants/           Constants.java + "enum-like" final classes (see §5).
registry/            Boot-time seeding/cache-priming (ApiMigrateRegistry).
scripts/             One-off utilities not part of the request path (e.g. web scrapers).
utils/               Stateless helpers (JwtUtils, PaginationUtils, SpecificationBuilder, ...).
logger/              Self-contained custom request-logging + global-exception-handling
                     sub-framework (see §6 and §7). Has its own configs/, contants/
                     [sic, misspelled — keep it], dto/, exceptions/, utils/ subpackages.
```

## 2. File Placement Rules (strict)

When adding a new feature or resource, create files in these exact locations with these
exact suffixes. Do not invent new top-level packages for something that fits an existing one.

| Artifact | Location | Naming |
|---|---|---|
| Controller | `controllers/` | `XxxController` |
| Service interface | `services/` | `XxxService` |
| Service implementation | `services/impl/` | `XxxServiceImpl` |
| Repository | `repository/` | `XxxRepository` (extends `JpaRepository`) |
| Entity | `entities/` | `XxxEntity` (must extend `BaseEntity`) |
| Create/update request DTO | `dto/request/` | `XxxCreateRequest`, `XxxUpdateRequest`, `XxxStatusRequest` |
| Response DTO | `dto/response/` | `XxxResponse` |
| Generic pagination/filter DTO | `dto/taxonomy/request` or `.../response` | — |
| DB migration | `src/main/resources/db/migration` | `V<next-number>__snake_case_description.sql` |
| Enum-like status/type constant | `constants/` | `XxxStatus`, `XxxType` (see §5 for pattern) |

Rules:

- **Never put business logic in a controller.** A controller method should be: validate input →
  call exactly one service method → wrap in `MessageResponse` → return `ResponseEntity`.
- **Every service gets both an interface (`services/`) and an implementation
  (`services/impl/`)**, even if there is currently only one implementation. This is a
  universal pattern in this codebase — do not skip the interface "to save time."
- **Every entity must extend `entities/BaseEntity`**, which supplies
  `createdAt`, `createdBy`, `updatedAt`, `updatedBy`, `status` (soft-delete flag:
  `Constants.STATUS_ACTIVE` = `"ACT"` / `Constants.STATUS_DELETE` = `"DEL"`, not a boolean).
  There is no automatic `@PrePersist`/`@PreUpdate` — you must set `updatedAt`/`updatedBy`
  manually in the service layer.
- **New entities use `String` UUID primary keys assigned manually** in the service layer
  (`UUID.randomUUID().toString()`) before `save()`. Do **not** use `@GeneratedValue` for new
  entities — the one entity that uses it (`ResponseCodeEntity`) is a legacy exception, not
  the pattern to copy.
- **New public endpoints must be added to the `permitAll()` matcher list in
  `configs/SecurityConfig.java`** — routes are not public by default.
- **New authority-gated endpoints need a matching `permissions` row**, typically seeded via
  the same Flyway migration that creates/alters the feature's table (see §4, and precedent
  in `V54__create_contact_messages_table.sql`, which creates a table, inserts a `permissions`
  row, and wires it to `ADMIN`/`SUPER_ADMIN` via `role_permissions` in one file).
- Do not add unrelated fields to `constants/Constants.java`. It already contains leftover
  fields from a different (gambling/banking) template project (`TRANSACTION_PLAY_GAME`,
  `COMMISSION`, `KHR`, etc.) that are **not** part of this domain — reuse genuinely-used
  constants (status codes, header names, Redis key prefixes) but don't treat every existing
  field as meaningful, and don't add more unrelated ones.
- Don't create a `logger/` subpackage member unless you are genuinely extending the
  request-logging/exception-handling sub-framework itself — for normal application logging in
  new feature code, just use `@Slf4j` (see §7).

## 3. Code Style

- **Indentation**: 4 spaces, K&R brace style (opening brace on same line). `utils/JwtUtils.java`
  uses 2-space indent — that's a pre-existing outlier, not a style to replicate.
- **Imports**: explicit imports only, no wildcards. (`import jakarta.persistence.*;` appears in
  two legacy entity files only — don't add more wildcard imports.)
- **`var`**: used freely for local variables when the type is obvious from the right-hand side
  (`var result = articleService.create(...)`). This is an accepted, deliberate style choice.
- **Lombok** is the standard, use it instead of hand-written boilerplate:
  - Entities: `@Data @EqualsAndHashCode(callSuper = false)`, often `@DynamicUpdate` (Hibernate),
    `implements Serializable` with an explicit `@Serial serialVersionUID`.
  - Request DTOs: `@Data`.
  - Response DTOs: `@Data @Builder` + `@JsonInclude(JsonInclude.Include.NON_NULL)` so null
    fields are omitted from JSON output.
  - Controllers / services / impls / filters / caches: `@RequiredArgsConstructor` for
    constructor injection (never field `@Autowired`).
  - Logging: `@Slf4j`.
- **Comments**: kept sparse. Use them for non-obvious business rules only — e.g. documenting
  an enum-like value set or a workflow's valid states, or a "why" behind a non-obvious decision.
  Do not narrate what the code already says. Section-divider comments are an established
  convention in controllers/service impls to separate logical groups of endpoints, e.g.:
  ```java
  // ── Admin endpoints (require authentication) ──────────────────────────────
  ...
  // ── Public endpoints (no authentication required) ─────────────────────────
  ```
- **`@Transactional`**: prefer `jakarta.transaction.Transactional` on service impl methods
  that write data (this is the majority usage, e.g. `ArticleServiceImpl`); Spring's
  `org.springframework.transaction.annotation.Transactional` also appears in a few places
  (`ApiMigrateRegistry`) but is not the pattern to prefer for new service code.
- No formatter/linter is configured (no Checkstyle, Spotless, or `.editorconfig`) — match the
  surrounding file's formatting by hand.

## 4. DTOs, Mapping, and Persistence

- **DTOs are plain Lombok classes, not Java records.**
- **Entity → Response mapping**: a `public static XxxResponse from(XxxEntity e)` static
  factory method defined on the response class itself, built with the Lombok builder:
  ```java
  public static ArticleResponse from(ArticleEntity e) {
      return ArticleResponse.builder()
          .id(e.getId())
          // ...
          .build();
  }
  ```
  Do not introduce MapStruct or another mapping library — none is used or configured.
- **Request → Entity mapping**: manual, field-by-field, inside the `ServiceImpl`
  (`entity.setTitle(request.getTitle())`, etc.). Keep this pattern for consistency rather
  than adding a generic mapper.
- **Validation**: `jakarta.validation.constraints` annotations (`@NotBlank`, etc.) directly on
  request DTO fields; controller methods take `@Valid @RequestBody`.
- **Column/table naming for new entities**: use lowercase snake_case
  (`@Table(name = "articles")`, `@Column(name = "title")`) — this is the convention used by
  every recent CMS-domain entity. Older entities (e.g. `UserEntity`) use `UPPERCASE_SNAKE`
  table/column names; that's legacy, don't extend it to new tables.
- **Migrations**: Flyway, `V<N>__description.sql` in `src/main/resources/db/migration`,
  strictly incrementing version numbers, no repeatable (`R__`) migrations. It's normal for a
  single migration to both alter schema and seed reference data (permissions, role
  assignments, lookup rows) in one file — follow that combined pattern for new CMS features
  rather than splitting schema and seed data across separate migrations.

## 5. Status/Type Constants — Not Java `enum`

This codebase deliberately does **not** use Java `enum` for domain status/type fields. Instead
it uses a final class with `public static final String` constants, e.g.
`constants/ArticleWorkflowStatus`, `ArticleType`, `MediaType`, `SettingGroup`. For workflow
state machines, valid transitions are encoded as a static `Map<String, Set<String>>` with
`isValid()`/`canTransition()` helper methods. Follow this exact pattern for any new
status/type/category field — do not introduce a Java `enum` for it, since entities store
these as plain `String` columns and the rest of the codebase (DTOs, JSON, DB) expects strings.

`logger/contants/enums/` is the one place real Java `enum`s are used (`AcceptLanguage`,
`AppLoggerMode`, `StatusConstant`) — that's internal to the logging sub-framework, not a
precedent for domain code.

## 6. Error Handling

- Global handler: `logger/exceptions/AppLoggerResponseEntityExceptionHandler`
  (`@ControllerAdvice @Order(Ordered.HIGHEST_PRECEDENCE)`), extends
  `ResponseEntityExceptionHandler`. Do not add new `@ExceptionHandler`/`@ControllerAdvice`
  classes elsewhere — extend this one if a new exception type needs handling.
- To signal a business/validation failure from a service, throw
  `logger/exceptions/AppException`:
  ```java
  throw new AppException("ARTICLE_NOT_FOUND", "Article not found: " + id);
  ```
  Set a non-default HTTP status when needed: `ex.setHttpStatus(HttpStatus.NOT_FOUND)` (default
  is `400 BAD_REQUEST`).
- Error codes/messages are **not hardcoded strings for the client** — they resolve dynamically
  against a DB-backed (`response_codes` table) + Redis/in-memory cached `ResponseCodeDto`
  lookup, supporting EN/KM/CN via the `Accept-Language` header. When adding a new user-facing
  error, add a corresponding `response_codes` row via migration rather than only relying on the
  literal message string passed to `AppException`.
- **Two response envelopes exist — use the right one:**
  - Success responses from controllers: `logger/exceptions/MessageResponse` —
    `new MessageResponse(result, ErrorCode.SUCCESS)`.
  - Error responses (from the global handler, automatic): `logger/dto/BaseResponse<ErrorResponse>`.
  - New controller code should only ever construct `MessageResponse` for success; let thrown
    exceptions + the global handler produce the error envelope. Don't build ad hoc error JSON
    in a controller.

## 7. Logging

- For normal application logging in feature code (services, controllers, filters, utils), use
  plain SLF4J via Lombok: `@Slf4j` on the class, then `log.info(...)`, `log.warn(...)`,
  `log.error(...)`. This is the established pattern everywhere outside `logger/`.
- Automatic HTTP request/response logging (bodies, sizes, sensitive-field masking) is handled
  globally by `logger/configs/LoggingFilter.java` + `logger/dto/AppLogger.java`, gated by the
  `applogger.enabled`/`applogger.mode` properties. **New endpoint code does not need to touch
  `AppLogger` directly** — it's wired in automatically via the filter chain and the global
  exception handler.

## 8. Security

- `configs/SecurityConfig.java`: stateless sessions, CSRF disabled, method-level
  `@PreAuthorize("hasAuthority('permission.code')")` per protected endpoint — this is
  permission-code based, not simple role checks. Follow the `resource.action` naming
  convention already in use (`articles.create`, `articles.update`, `articles.publish`,
  `articles.delete`, `articles.view`).
- Public endpoints must be explicitly added to the `permitAll()` matcher list in
  `SecurityConfig` — nothing is public by default.
- JWT (`io.jsonwebtoken` / jjwt) issued and validated via `utils/JwtUtils`. Tokens are also
  checked against `caches/TokenRedisCache` on every request (`filters/AuthTokenFilter`) to
  support server-side revocation, and current permissions are loaded fresh from the DB per
  request so permission changes apply without re-login. `filters/IpWhitelistFilter` runs
  before the auth filter.
- Passwords are hashed with BCrypt.
- Request context (current user id/name/device id) is propagated to controllers/services via
  injected headers on `filters/MutableHttpServletRequest`, read with
  `@RequestHeader(value = Constants.USER_ID, required = false)` — follow this pattern rather
  than pulling the user off `SecurityContextHolder` directly in new controller code, to stay
  consistent with existing endpoints.

## 9. Testing

There is effectively no established testing convention yet (two test files total: a trivial
`@SpringBootTest` context-load test, and one hand-rolled read-only JDBC integration test that
deliberately avoids booting the Spring context). Treat this as a green field:

- Do not assume Mockito/`@WebMvcTest`/`@DataJpaTest`/Testcontainers conventions exist — none
  are configured. If the user asks for tests, ask what stack they want, or default to
  `spring-boot-starter-test` (already a dependency) with plain JUnit 5.
- Never write a test that opens a live connection to the shared dev database and mutates data
  — the existing integration test explicitly reads only (`conn.setReadOnly(true)`) for exactly
  this reason.

## 10. Config and Secrets — Known Issues, Don't Extend

- `application-dev.yaml` and `application-local.yaml` currently contain **hardcoded DB/Redis
  credentials committed to the repo**. This is a pre-existing problem, not something to copy
  for new config values — prefer `${ENV_VAR:default}` placeholders for any new secret you add,
  even though most existing entries don't do this.
- `spring.jpa.hibernate.ddl-auto` must stay `none`. Flyway is the sole source of schema truth
  in this project; never switch this to `update`/`validate`/`create`.
- `sample.env` / `.env.local` at the repo root hold Next.js-style (`NEXT_PUBLIC_*`) variables
  unrelated to this Spring Boot app's own runtime config — don't read from or extend them for
  backend configuration; use `application-*.yaml` instead.

## 11. How to Format Code Snippets in Responses

When proposing or showing Java code for this repo:

- Show full, compilable class bodies for new files (package declaration, explicit imports in
  the order they'd appear — no wildcards), not fragments that require the reader to guess
  imports.
- For edits to existing files, show only the changed method/block plus enough surrounding
  context to place it, not the whole file, unless the whole file changed.
- Use 4-space indentation and K&R braces to match the dominant house style (§3).
- Match the layered file-placement rules in §2 — when proposing a new feature, present it as
  the same slice of files the codebase already uses (Controller → Service → ServiceImpl →
  Repository → Entity → Request/Response DTOs → migration), not a single-file shortcut.
- When a new endpoint is added, mention explicitly (in prose, not just in the diff) that
  `SecurityConfig`'s route matchers and/or a `permissions` seed migration may need updating —
  it's easy to add a controller method and forget the authorization wiring.
- SQL migration snippets should be fenced as ```sql and named as a complete
  `V<N>__description.sql` file, following the existing combined
  schema-DDL-plus-seed-data pattern where relevant (§4).
