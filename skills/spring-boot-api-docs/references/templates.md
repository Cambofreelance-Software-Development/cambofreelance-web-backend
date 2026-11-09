# Postman YAML & Markdown API Doc Templates

## 1. Postman Request File (`<Request Name>.request.yaml`)

### POST / PUT Request with Body & Path Variable
```yaml
$kind: http-request
name: PUT /me/inventory/products/:productId
url: "{{base_url}}/me/inventory/products/:productId"
method: PUT
headers:
  Authorization: Bearer {{access_token}}
  User-Id: "{{user_id}}"
  Tenant-Id: "{{tenant_id}}"
  Content-Type: application/json
pathVariables:
  productId: "{{product_id}}"
body:
  type: json
  content: |-
    {
      "name": "Honda Click 160i",
      "sku": "HND-CLK160-BLK-2026",
      "barcode": "8850001112223",
      "productCode": "PRD-0001",
      "productType": "VEHICLE",
      "categoryId": "{{category_id}}",
      "brand": "Honda",
      "model": "Click 160",
      "modelYear": 2026,
      "unit": "UNIT",
      "trackingType": "SERIALIZED",
      "catalogStatus": "ACTIVE",
      "hasVariants": false,
      "currency": "USD",
      "costPrice": 2450.00,
      "retailPrice": 2850.00
    }
examples: ./.resources/PUT -me-inventory-products-productId.resources/examples
order: 2000
```

### GET Request with Query Parameters
```yaml
$kind: http-request
name: GET /me/inventory/products
url: "{{base_url}}/me/inventory/products?search=&categoryId=&productType=&trackingType=&catalogStatus=&page=0&size=20"
method: GET
headers:
  Authorization: Bearer {{access_token}}
  User-Id: "{{user_id}}"
  Tenant-Id: "{{tenant_id}}"
queryParams:
  search: ""
  categoryId: ""
  productType: ""
  trackingType: ""
  catalogStatus: ""
  page: "0"
  size: "20"
examples: ./.resources/GET -me-inventory-products.resources/examples
order: 3000
```

---

## 2. Postman Example File (`Simple success.example.yaml`)

Path: `.resources/<Request Name>.resources/examples/Simple success.example.yaml`

```yaml
$kind: http-example
request:
  url: "{{base_url}}/me/inventory/products/:productId"
  method: PUT
  headers:
    Authorization: Bearer {{access_token}}
    User-Id: "{{user_id}}"
    Tenant-Id: "{{tenant_id}}"
    Content-Type: application/json
  pathVariables:
    productId: "{{product_id}}"
  body:
    type: json
    content: |-
      {
        "name": "Honda Click 160i",
        "sku": "HND-CLK160-BLK-2026",
        "productType": "VEHICLE",
        "categoryId": "{{category_id}}",
        "unit": "UNIT",
        "trackingType": "SERIALIZED",
        "currency": "USD",
        "costPrice": 2450.00,
        "retailPrice": 2850.00
      }
response:
  statusCode: 200
  statusText: OK
  headers:
    Content-Type: application/json
  body:
    type: json
    content: |-
      {
        "code": "SUCCESS",
        "message": "Success",
        "data": {
          "id": "00000000-0000-0000-0000-000000000001",
          "name": "Honda Click 160i",
          "sku": "HND-CLK160-BLK-2026",
          "productType": "VEHICLE",
          "trackingType": "SERIALIZED",
          "catalogStatus": "ACTIVE",
          "currency": "USD",
          "costPrice": 2450.00,
          "retailPrice": 2850.00
        }
      }
order: 1000
```

---

## 3. Markdown API Reference Template (`md/api-docs/<module>.md`)

```markdown
# <Module Name> API Reference

- **Controller**: `<ControllerClassName>`
- **Base Path**: `/me/<module>`
- **Authentication**: Bearer Token (`Authorization: Bearer <access_token>`)
- **Tenant Context**: Required (`Tenant-Id: <tenant_uuid>`)

## Endpoints Summary

| Method | Endpoint | Permission (`@PreAuthorize`) | Status | Description |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/me/...` | `<permission.create>` | `201 Created` | Create a new resource |
| `GET` | `/me/...` | `<permission.view>` | `200 OK` | Search/list resources (paginated) |
| `GET` | `/me/.../{id}` | `<permission.view>` | `200 OK` | Get resource by ID |
| `PUT` | `/me/.../{id}` | `<permission.update>` | `200 OK` | Update resource by ID |
| `DELETE` | `/me/.../{id}` | `<permission.delete>` | `200 OK` | Delete/archive resource |

---

## 1. Create <Resource>

`POST /me/...`

### Required Headers
| Header | Value | Required | Description |
| :--- | :--- | :--- | :--- |
| `Authorization` | `Bearer <token>` | Yes | JWT access token |
| `Tenant-Id` | `<uuid>` | Yes | Active tenant identifier |
| `User-Id` | `<uuid>` | No | Acting user identifier |

### Request Body (`<RequestDtoName>`)
```json
{
  "field": "value"
}
```

#### Field Rules
| Field | Type | Required | Constraints / Allowed Values |
| :--- | :--- | :--- | :--- |
| `field` | `String` | Yes | `@NotBlank` |

### Success Response (`201 Created`)
```json
{
  "code": "SUCCESS",
  "message": null,
  "data": {
    "id": "uuid"
  },
  "success": true,
  "timestamp": 1731162000000,
  "traceId": null
}
```
```
