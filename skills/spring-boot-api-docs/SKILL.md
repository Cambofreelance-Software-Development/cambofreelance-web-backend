---
name: spring-boot-api-docs
description: >-
  Generates and updates API documentation (Postman local collection YAML files and
  Markdown API reference docs) from Spring Boot REST controllers and DTOs. Use this
  skill automatically after implementing or updating any Spring Boot controller,
  endpoint, or request/response DTO, or when the user asks to document APIs.
---

# Spring Boot API Documentation Skill

Whenever you finish implementing or modifying a Spring Boot `@RestController` or its DTOs in this repository, follow this workflow to keep both the **Postman Local Collection** and **Markdown API Reference** synchronized with the code.

---

## 1. Inspect the Controller & DTOs

Before writing documentation, read the target controller and all referenced request/response DTOs:

1. **Endpoint Mapping**: Combine class-level `@RequestMapping` with method-level `@GetMapping`, `@PostMapping`, `@PutMapping`, `@PatchMapping`, or `@DeleteMapping`.
2. **Authentication & Headers**:
   - Protected endpoints (`/cms/**`, `/me/**`, or methods with `@PreAuthorize`): include `Authorization: Bearer {{access_token}}`.
   - Multi-tenant endpoints (`/me/**` or `@RequestHeader(Constants.TENANT_ID)`): include `Tenant-Id: "{{tenant_id}}"`.
   - User-context endpoints (`@RequestHeader(Constants.USER_ID)`): include `User-Id: "{{user_id}}"`.
   - Public endpoints (`/articles/**`, `/public/**`, `/auth/**`): omit auth/tenant headers unless explicitly required in the method signature.
3. **Permissions**: Note the authority string inside `@PreAuthorize("hasAuthority('...')")`.
4. **Parameters & Payloads**:
   - `@PathVariable`: convert `{varName}` in Spring paths to `:varName` in Postman URLs and map to `{{snake_case_id}}` in `pathVariables`.
   - `@RequestParam`: record name, type, `required`, and `defaultValue` (e.g., `page = 0`, `size = 20`).
   - `@RequestBody`: inspect the DTO class in `com.cambofreelance.webbackend.dto.request.*` (including nested DTOs and `jakarta.validation.constraints.*` annotations).
   - **Response Envelope**: All endpoints return `MessageResponse` (`code`, `message`, `data`, `success`, `timestamp`, `traceId`) with `HttpStatus.OK` (200) or `HttpStatus.CREATED` (201).

---

## 2. Update Postman Local Collection (`CamboFreelance Web Backend - All APIs/`)

This repository uses Postman's multi-file YAML format in `CamboFreelance Web Backend - All APIs/`.

### Step 2.1: Ensure Domain Folder Exists
1. Choose or create a domain folder under `CamboFreelance Web Backend - All APIs/<Domain Name>/` (e.g., `Inventory - Products`, `Inventory - Warehouses`, `Inventory - Sales`, `CMS - Articles`).
2. Ensure `CamboFreelance Web Backend - All APIs/<Domain Name>/.resources/definition.yaml` exists:
   ```yaml
   $kind: collection
   order: 20000
   ```
3. If your controller introduces new path variable placeholders (e.g., `{{product_id}}`, `{{warehouse_id}}`, `{{sale_id}}`), add default UUID variables to `CamboFreelance Web Backend - All APIs/.resources/definition.yaml`.

### Step 2.2: Create/Update `<Request Name>.request.yaml`
For each endpoint in the controller, create or update `<Request Name>.request.yaml` in the domain folder:
- Increment `order` by `1000` per request (`1000`, `2000`, `3000`, ...).
- Use `:paramName` syntax in `url` for path variables and declare `pathVariables`.
- Include realistic sample JSON in `body.content` matching every field in the request DTO.
- Point `examples` to `./.resources/<Request Name>.resources/examples`.

### Step 2.3: Create/Update `Simple success.example.yaml`
For each request, create or update `.resources/<Request Name>.resources/examples/Simple success.example.yaml`:
- Mirror the request definition (`url`, `method`, `headers`, `pathVariables`, `queryParams`, `body`).
- Set `response.statusCode` (`200` or `201`) and `response.statusText` (`OK` or `Created`).
- Populate `response.body.content` with the standard `MessageResponse` JSON structure containing realistic sample response DTO fields inside `"data"`.

See [references/templates.md](./references/templates.md) for exact YAML and Markdown templates.

---

## 3. Create/Update Markdown API Reference (`md/api-docs/<module>.md`)

Create or update a Markdown reference file in `md/api-docs/<module>.md` (e.g., `md/api-docs/inventory-products.md`) for frontend and mobile developers. Include:

1. **Module Overview & Base Path**: Controller name, base URL path, required headers, and multi-tenant scope.
2. **Endpoints Summary Table**: Method, Path, Permission (`@PreAuthorize`), HTTP Status, and Description.
3. **Detailed Endpoint Sections**:
   - **Headers**: Required headers (`Authorization`, `Tenant-Id`, `User-Id`).
   - **Path & Query Parameters**: Table with parameter name, type, required/optional, default value, and description.
   - **Request Body Schema & Example**: JSON payload example plus field constraints (`@NotBlank`, `@NotNull`, allowed enum values, nested objects).
   - **Response Example**: Standard `MessageResponse` JSON with realistic `data` payload (or paginated structure when returning a page/list).
   - **Error Responses**: Common `ErrorCode` values thrown by the controller/service (`BAD_REQUEST`, `UNAUTHORIZED`, `FORBIDDEN`, `NOT_FOUND`, etc.).

---

## 4. Verification Checklist

Before finishing your task, verify:
- [ ] Every endpoint in the newly implemented/updated `@RestController` has a matching `.request.yaml` and `.resources/<Request Name>.resources/examples/Simple success.example.yaml`.
- [ ] Spring `{id}` path variables are converted to `:id` in Postman `url` and listed under `pathVariables:`.
- [ ] Multi-tenant `/me/**` endpoints include `Tenant-Id: "{{tenant_id}}"` in both request and example headers.
- [ ] All DTO fields (including newly added fields) are reflected in both the Postman JSON body and `md/api-docs/<module>.md`.
