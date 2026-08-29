# AI Agent Build Specification
# Multi-Tenant Inventory & Sales Traceability System
# Spring Boot + Next.js + PostgreSQL

## 1. Mission

Implement a new **Inventory & Sales Traceability System** inside an existing SaaS platform.

The existing SaaS platform already has:

- Customer/company subscriptions
- Tenant management
- Existing Mini Loan feature
- Mini Loan database/schema
- Existing authentication and authorization
- Existing frontend/backend architecture

Technology:

- Backend: Spring Boot
- Frontend: Next.js
- Database: PostgreSQL

The new Inventory module must be production-ready, multi-tenant, secure, transactional, and extensible.

Primary business use case:

> Motorcycle dealership inventory and sales with bank-financed vehicle sales.

The system must also support:

- Cars
- Trucks
- Spare parts
- Electronics
- General products

---

# 2. FIRST RULE: INSPECT THE EXISTING PROJECT

Before changing any code, inspect the existing repository.

Do not assume architecture.

Identify:

### Backend

- Spring Boot version
- Kotlin or Java
- Package structure
- Existing modules
- Existing tenant architecture
- Existing authentication
- Existing authorization
- Existing user model
- Existing database configuration
- Existing JPA/Hibernate configuration
- Existing transaction management
- Flyway or Liquibase
- Existing exception handling
- Existing API response format
- Existing pagination
- Existing logging/audit
- Existing file storage
- Existing event/message infrastructure

### Frontend

- Next.js version
- App Router or Pages Router
- TypeScript usage
- Existing UI library
- Existing design system
- Existing form library
- Existing validation
- Existing API client
- Existing authentication
- Existing permission handling
- Existing table components
- Existing modal/dialog components

### Database

Identify:

- Shared/public schema
- Tenant table
- Subscription table
- Existing Mini Loan schema
- How tenant schemas are created
- How migrations are executed per tenant
- How the application selects the current tenant schema
- Existing naming conventions
- Existing indexes
- Existing audit tables

Do not modify the existing Mini Loan implementation during analysis.

---

# 3. MULTI-TENANT ARCHITECTURE

The SaaS uses **schema-per-tenant**.

A SaaS customer/company is a Tenant.

Example:

```text
SaaS Platform
│
├── Tenant A
│   └── tenant_001
│
├── Tenant B
│   └── tenant_002
│
└── Tenant C
    └── tenant_003
```

Each tenant receives an isolated PostgreSQL schema.

Example:

```text
tenant_001
tenant_002
tenant_003
```

The Inventory tables belong to the tenant schema.

Example:

```text
tenant_001.product
tenant_001.product_variant
tenant_001.inventory_item
tenant_001.warehouse
tenant_001.customer
tenant_001.sale
```

Another tenant:

```text
tenant_002.product
tenant_002.product_variant
tenant_002.inventory_item
tenant_002.warehouse
tenant_002.customer
tenant_002.sale
```

Tenant A must never access Tenant B data.

---

# 4. IMPORTANT: DO NOT INVENT A SECOND TENANT SYSTEM

The existing application already has tenant/subscription management.

Reuse it.

Do not create:

```text
inventory.tenant
```

if the existing SaaS already has a tenant model.

Do not add a second `tenant_id` architecture unless the existing system requires it.

Follow the existing project's tenant isolation mechanism.

---

# 5. SCHEMA CREATION

When a SaaS customer subscribes:

```text
Customer subscribes
        ↓
Tenant created
        ↓
Tenant schema created
        ↓
Tenant migrations executed
        ↓
Inventory tables created
        ↓
Tenant ready
```

Example:

```text
Tenant:
ABC Motors

Schema:
tenant_001
```

Then:

```text
tenant_001.product
tenant_001.product_variant
tenant_001.inventory_item
...
```

Do not manually create tables from Java service code.

Use the project's existing migration mechanism.

If the project uses Flyway, extend Flyway appropriately.

If the project uses Liquibase, follow Liquibase.

Do not introduce a second migration framework.

---

# 6. MINI LOAN DOMAIN BOUNDARY

The existing Mini Loan feature is separate.

Do not directly couple Inventory tables to Mini Loan tables.

Avoid:

```text
inventory.sale.loan_application_id
```

where `loan_application` belongs to Mini Loan.

Instead, Inventory owns a lightweight financing integration model.

Example:

```text
inventory.sale
      ↓
inventory.financing_application
      ↓
external_reference
      ↓
Mini Loan
```

Integration should use:

- REST API
- Internal service interface
- Domain event
- Messaging/event infrastructure

Use whatever integration mechanism already exists in the project.

The Inventory module must remain independently understandable.

---

# 7. DOMAIN MODEL

Core hierarchy:

```text
Product
    ↓
Product Variant
    ↓
Inventory Item
    ↓
Reservation
    ↓
Sale
    ↓
Financing Application
    ↓
Documents
```

Example:

```text
Honda Dream 2026
│
├── Black / 125cc
│   ├── VIN001
│   ├── VIN002
│   └── VIN003
│
├── Red / 125cc
│   ├── VIN004
│   └── VIN005
│
└── White / 125cc
    └── VIN006
```

---

# 8. PRODUCT

Product represents the parent/catalog definition.

Example:

```text
Honda Dream 2026
Brand: Honda
Category: Motorcycle
Model: Dream
Year: 2026
```

Product must NOT contain:

- Customer ID
- Sale ID
- VIN of a physical unit
- Reservation status
- Loan status
- Sold status

Recommended fields:

```text
id
sku
barcode
name
product_type
category_id
brand_id
model
model_year
description
tracking_type
base_unit
default pricing
default inventory settings
status
created_at
updated_at
```

---

# 9. PRODUCT TYPE

Support:

```text
STOCK
VEHICLE
PART
ELECTRONICS
SERVICE
```

Keep product type configurable if the existing system supports master data.

---

# 10. TRACKING TYPE

Support:

```text
QUANTITY
BATCH
SERIALIZED
```

### QUANTITY

Use for:

- Spare parts
- Accessories
- Consumables
- General goods

Example:

```text
Brake Pad
Stock = 500
```

### BATCH

Use for:

- Lot-based products
- Expiry-controlled products

Fields:

```text
batch_no
quantity
manufactured_at
expires_at
cost_price
warehouse
location
```

### SERIALIZED

Use for:

- Motorcycles
- Cars
- Trucks
- Phones
- Laptops
- Other individually tracked products

Each physical unit is an Inventory Item.

---

# 11. PRODUCT VARIANT

A Product Variant is a sellable configuration.

Example:

```text
Honda Dream 2026
├── Black / 125cc
├── Red / 125cc
└── White / 125cc
```

Variant fields:

```text
id
product_id
name
sku
barcode
cost_price_override
retail_price_override
wholesale_price_override
vip_price_override
image
status
created_at
updated_at
```

SKU and barcode must be unique.

---

# 12. IMPORTANT: VIN IS NOT A VARIANT

Do NOT model:

```text
Honda Dream
├── VIN001 variant
├── VIN002 variant
└── VIN003 variant
```

Correct:

```text
Honda Dream
└── Black / 125cc
    ├── VIN001
    ├── VIN002
    └── VIN003
```

VIN belongs to `inventory_item`.

---

# 13. ATTRIBUTES

Use dynamic attributes.

Tables/concepts:

```text
attribute
attribute_value
product_attribute
variant_attribute
```

Example attributes:

```text
COLOR
ENGINE_CC
FUEL_TYPE
TRANSMISSION
RAM
STORAGE
WARRANTY
```

Attribute data types:

```text
TEXT
NUMBER
BOOLEAN
DATE
SELECT
```

Do not add a database column for every possible attribute.

---

# 14. ADDITIONAL ATTRIBUTES

Additional Attributes describe the Product but do not necessarily create a separate SKU.

Example:

```text
Warranty = 24 months
Manufacturer = Honda
Country of Origin = Cambodia
```

UI:

```text
Additional Attributes

Attribute              Value
--------------------------------
Warranty               24 months
Manufacturer           Honda
Country of Origin      Cambodia

[ + Add Attribute ]
```

---

# 15. VARIANT ATTRIBUTES

Variant Attributes define combinations that create separate sellable variants.

Example:

```text
Color
Engine CC
```

Generate:

```text
Black / 125cc
Red / 125cc
White / 125cc
```

Allow users to remove unwanted combinations before saving.

Do not automatically create thousands of combinations without confirmation.

---

# 16. SKU GENERATION

Support automatic SKU generation.

Example:

```text
Parent SKU:
HON-DREAM-2026

Variant:
Black / 125cc

Generated:
HON-DREAM-2026-BLK-125
```

Allow manual override.

Validate uniqueness at the database level.

---

# 17. PRICING

Product owns default pricing.

Variant may override.

Support:

```text
cost_price
retail_price
wholesale_price
vip_price
discount
tax
currency
```

Calculate:

```text
gross_margin
margin_percentage
```

Warn if retail price is below cost.

---

# 18. WAREHOUSE

Support multiple warehouses.

Hierarchy:

```text
Warehouse
    ↓
Location
```

Example:

```text
Main Warehouse
├── A-01-01
├── A-01-02
└── A-02-01

Showroom
├── SHOWROOM-01
└── SHOWROOM-02
```

Inventory Items and Batches can have their own warehouse/location.

---

# 19. INVENTORY ITEM

Inventory Item represents one physical serialized item.

Fields:

```text
id
variant_id
warehouse_id
location_id
serial_no
vin
engine_no
cost_price
status
received_at
created_at
updated_at
```

For vehicles:

```text
VIN
Engine Number
Chassis Number
Color
```

Do not force vehicle-only fields onto generic products unless the project architecture supports flexible metadata.

---

# 20. INVENTORY ITEM STATUS

Support:

```text
AVAILABLE
RESERVED
LOAN_PENDING
SOLD
DELIVERED
RETURNED
DAMAGED
```

Product Status is separate:

```text
DRAFT
ACTIVE
INACTIVE
```

Example:

```text
Product:
Honda Dream 2026
ACTIVE

Inventory Item:
VIN001
LOAN_PENDING
```

---

# 21. BATCH

Batch fields:

```text
id
variant_id
warehouse_id
location_id
batch_no
manufactured_at
expires_at
cost_price
quantity
created_at
```

Unique rule:

```text
variant + warehouse + batch_no
```

---

# 22. INVENTORY MOVEMENT

Create an immutable stock ledger.

Movement types:

```text
OPENING
PURCHASE
SALE
RETURN_IN
RETURN_OUT
TRANSFER_IN
TRANSFER_OUT
ADJUSTMENT_IN
ADJUSTMENT_OUT
DAMAGE
```

Every stock-changing operation must create a movement.

Do not delete historical movements.

---

# 23. STOCK CALCULATION

Quantity inventory:

```text
Stock =
Opening
+ Purchase
+ Return In
+ Transfer In
+ Adjustment In
- Sale
- Return Out
- Transfer Out
- Adjustment Out
- Damage
```

For serialized products, physical Inventory Items are the source of truth.

Example:

```text
Total = 500
Available = 465
Reserved = 3
Loan Pending = 2
Sold = 25
Delivered = 5
```

Do not manually change calculated stock without an inventory transaction.

---

# 24. CUSTOMER

This is the person/company buying products from the tenant.

Do not confuse this with the SaaS Tenant.

Example:

```text
Tenant:
ABC Motors

Sales Customer:
Customer A
```

Customer fields:

```text
id
customer_no
customer_type
full_name
phone
email
address
status
created_at
updated_at
```

One customer can have:

- Multiple sales
- Multiple vehicles
- Multiple financing applications
- Multiple documents

---

# 25. SALE

Sale fields:

```text
id
sale_no
customer_id
salesperson_id
warehouse_id
sale_status
subtotal
discount_amount
tax_amount
total_amount
down_payment
financed_amount
created_at
updated_at
```

Sale statuses:

```text
DRAFT
RESERVED
LOAN_PENDING
CONFIRMED
CANCELLED
COMPLETED
```

---

# 26. SALE ITEM

Sale Item:

```text
sale_id
variant_id
inventory_item_id
quantity
unit_price
discount_amount
cost_price
total_amount
```

For serialized vehicles:

```text
quantity = 1
inventory_item_id = VIN001
```

For quantity products:

```text
quantity = 10
inventory_item_id = NULL
```

---

# 27. RESERVATION

A reservation prevents two salespeople from selling the same serialized item.

Example:

```text
VIN001
AVAILABLE

Salesperson 1
    ↓
Reserve

VIN001
RESERVED
```

Reservation fields:

```text
id
inventory_item_id
sale_id
customer_id
reserved_by
status
reserved_at
expires_at
released_at
```

Statuses:

```text
ACTIVE
RELEASED
EXPIRED
CONVERTED
```

Database must prevent multiple active reservations for one Inventory Item.

Use a PostgreSQL partial unique index if supported by the existing migration approach.

---

# 28. BANK

Inventory can maintain its own bank master:

```text
bank
----
id
code
name
active
```

Do not duplicate an existing global bank master if the platform already has one. Reuse or reference it according to existing architecture.

---

# 29. FINANCING APPLICATION

Inventory needs a lightweight financing tracking entity.

Fields:

```text
id
application_no
sale_id
customer_id
bank_id
requested_amount
approved_amount
status
external_reference
submitted_at
approved_at
rejected_at
rejection_reason
created_at
updated_at
```

Statuses:

```text
DRAFT
SUBMITTED
UNDER_REVIEW
ADDITIONAL_DOCUMENT_REQUIRED
APPROVED
REJECTED
CANCELLED
EXPIRED
```

`external_reference` is used for integration with Mini Loan or an external financing system.

---

# 30. MINI LOAN INTEGRATION

When financing is submitted:

```text
Inventory Sale
      ↓
Financing Application
      ↓
Integration Service
      ↓
Mini Loan
```

Do not query Mini Loan tables directly.

Use the existing:

- REST client
- internal service
- event bus
- messaging

whichever exists.

Store:

```text
external_reference
```

Example:

```text
Inventory Financing:
FIN-000001

Mini Loan:
ML-000001
```

---

# 31. FINANCED MOTORCYCLE FLOW

Implement:

```text
AVAILABLE
    ↓
RESERVED
    ↓
LOAN_PENDING
    ├── APPROVED
    │      ↓
    │   CONFIRMED
    │      ↓
    │     SOLD
    │      ↓
    │   DELIVERED
    │
    └── REJECTED
           ↓
        RELEASED
           ↓
        AVAILABLE
```

Critical business rule:

> A vehicle waiting for bank approval is NOT SOLD.

Example:

```text
Inventory Item:
VIN001
Status = LOAN_PENDING

Sale:
SALE-000001
Status = LOAN_PENDING

Financing:
FIN-000001
Status = UNDER_REVIEW
```

---

# 32. BANK APPROVAL

When approved:

```text
Financing:
UNDER_REVIEW → APPROVED

Sale:
LOAN_PENDING → CONFIRMED

Inventory:
LOAN_PENDING → SOLD
```

Create sale inventory movement.

Do all related changes in one database transaction.

---

# 33. BANK REJECTION

When rejected:

```text
Financing:
UNDER_REVIEW → REJECTED

Reservation:
ACTIVE → RELEASED

Inventory:
LOAN_PENDING → AVAILABLE

Sale:
LOAN_PENDING → CANCELLED
```

Preserve the full audit history.

---

# 34. DOCUMENT MANAGEMENT

Documents can belong to:

```text
CUSTOMER
SALE
FINANCING_APPLICATION
INVENTORY_ITEM
PRODUCT
VARIANT
```

Examples:

Customer:

```text
National ID
Passport
Family Book
Salary Certificate
Employment Letter
Bank Statement
```

Financing:

```text
Loan Application
Credit Documents
Loan Contract
Additional Documents
```

Sale:

```text
Sales Contract
Invoice
Receipt
```

Vehicle:

```text
Vehicle Certificate
Warranty
Manufacturer Document
```

---

# 35. FILE STORAGE

Do not store large binary files directly in business tables.

Create file metadata:

```text
file_storage
------------
id
storage_provider
storage_key
original_filename
content_type
file_size
checksum
created_at
```

Actual file storage should use the existing project storage.

Possible implementations:

```text
S3-compatible
MinIO
Cloud Object Storage
```

If the project already has file storage, reuse it.

---

# 36. DOCUMENT VERSIONING

Do not overwrite existing documents.

Example:

```text
Salary Certificate

Version 1
REJECTED

Version 2
VERIFIED
```

Document statuses:

```text
UPLOADED
PENDING_REVIEW
VERIFIED
REJECTED
EXPIRED
REPLACED
```

---

# 37. AUDIT LOG

Create or reuse the existing audit system.

Audit:

```text
entity_type
entity_id
action
old_data
new_data
performed_by
created_at
```

Important events:

```text
Product Created
Product Updated
Variant Created
Inventory Received
Inventory Adjusted
Inventory Transferred

Sale Created
Reservation Created
Reservation Released
Sale Confirmed
Sale Cancelled

Financing Submitted
Financing Approved
Financing Rejected

Document Uploaded
Document Verified
Document Rejected

Vehicle Delivered
```

---

# 38. TRACEABILITY

Every serialized vehicle must have a complete timeline.

Example:

```text
VIN001

Purchased
    ↓
Received
    ↓
Available
    ↓
Reserved by Salesperson 1
    ↓
Customer A
    ↓
Loan Submitted
    ↓
Bank Review
    ↓
Additional Document Required
    ↓
Document Uploaded
    ↓
Loan Approved
    ↓
Sale Confirmed
    ↓
Sold
    ↓
Delivered
```

Timeline must show:

- Date
- Time
- User
- Action
- Previous Status
- New Status
- Reference
- Notes

---

# 39. PRODUCT CREATE UI

Preserve the existing Create Product design.

Recommended order:

```text
1. Product Information
2. Additional Attributes
3. Product Variants
4. Pricing
5. Inventory
6. Vehicle Information
7. Batch Details
8. Serialized Inventory Items
9. Unit & Packaging
10. Product Documents
```

Use conditional sections.

---

# 40. PRODUCT INFORMATION UI

Fields:

```text
Product Type
Product Name *
SKU *
Barcode
Category *
Brand
Model
Model Year
Unit *
Description
Product Image
Status
Manufacturer Part Number
Preferred Supplier
```

---

# 41. ADDITIONAL ATTRIBUTES UI

Example:

```text
Additional Attributes

Attribute              Value
--------------------------------
Warranty               24 months
Manufacturer           Honda
Country of Origin      Cambodia

[ + Add Attribute ]
```

Support predefined and custom attributes according to existing master-data architecture.

---

# 42. PRODUCT VARIANTS UI

Example:

```text
[✓] This product has variants

Variant Attributes

[✓] Color
[✓] Engine CC
[ ] Fuel Type
[ ] Transmission

[ Generate Variants ]
```

Preview:

```text
Generated Variants: 4

Black / 125cc
Red / 125cc
White / 125cc
Blue / 125cc
```

Allow removal before save.

Variant table:

```text
Variant
SKU
Barcode
Cost
Retail
Stock
Status
Actions
```

---

# 43. INVENTORY UI

Tracking selector:

```text
[ Quantity ] [ Batch ] [ Serialized ]
```

Quantity:

```text
Opening Stock
Reorder Level
Minimum Stock
Maximum Stock
Warehouse
Location
```

Batch:

```text
Opening Stock = calculated

Batch No.
Quantity
Manufacturing Date
Expiry Date
Cost
Warehouse
Location
```

Serialized:

```text
Opening Stock = calculated

VIN / Serial
Engine Number
Color
Cost
Warehouse
Location
Status
```

---

# 44. VEHICLE UI

Only show for vehicle products.

Fields:

```text
VIN / Chassis Number
Engine Number
Color
Engine CC
Fuel Type
Transmission
Model Year
Condition
Warranty
```

Physical vehicle identity must be stored on Inventory Item.

---

# 45. LIVE SUMMARY

Preserve the existing right-side live summary.

Example:

```text
Honda Dream 2026

Honda
Motorcycle

4 Variants

Serialized

Stock
Available: 20
Reserved: 3
Loan Pending: 1
Sold: 1

Pricing
Cost: $1,200
Retail: $2,100
Margin: 42.9%
```

---

# 46. CUSTOMER 360

Customer page should include:

```text
Profile
Documents
Sales
Vehicles
Financing
Payments
Activity
```

Show:

```text
Total Sales
Active Financing
Vehicles
Documents
```

---

# 47. SALES UI

Sales creation should support:

```text
Customer
Salesperson
Product
Variant
Inventory Item
Quantity
Price
Discount
Down Payment
Financing
```

For serialized vehicles:

```text
Select VIN
```

Only valid inventory can be selected.

---

# 48. FINANCING UI

Show:

```text
Customer
Sale
Vehicle
Bank
Requested Amount
Down Payment
Financed Amount
Status
Documents
Timeline
```

Status badge:

```text
SUBMITTED
UNDER_REVIEW
ADDITIONAL_DOCUMENT_REQUIRED
APPROVED
REJECTED
```

---

# 49. DOCUMENT CHECKLIST

For financing, support required document templates.

Example:

```text
Required Documents

✓ National ID
✓ Family Book
✓ Salary Certificate
✓ Employment Letter
✓ Bank Statement

Additional Documents
○ Required by Bank
```

Prevent submission if mandatory documents are missing, according to configured business rules.

---

# 50. NEXT.JS ROUTES

Follow existing route conventions.

Possible routes:

```text
/inventory

/inventory/products
/inventory/products/new
/inventory/products/[id]
/inventory/products/[id]/edit

/inventory/items
/inventory/items/[id]

/inventory/warehouses
/inventory/stock-movements

/inventory/customers
/inventory/customers/[id]

/inventory/sales
/inventory/sales/new
/inventory/sales/[id]

/inventory/financing
/inventory/financing/[id]

/inventory/documents
```

Do not create duplicate UI frameworks.

Reuse existing:

- Layout
- Navigation
- Table
- Form
- Modal
- Button
- Badge
- Input
- Select
- Date picker
- File upload
- Toast
- Pagination

---

# 51. SPRING BOOT API

Follow existing controller/service/repository conventions.

Recommended endpoints:

```text
GET    /api/inventory/products
POST   /api/inventory/products
GET    /api/inventory/products/{id}
PUT    /api/inventory/products/{id}

POST   /api/inventory/products/{id}/variants
GET    /api/inventory/products/{id}/variants

GET    /api/inventory/items
GET    /api/inventory/items/{id}
POST   /api/inventory/items

GET    /api/inventory/warehouses
POST   /api/inventory/warehouses

GET    /api/inventory/movements

GET    /api/inventory/customers
POST   /api/inventory/customers
GET    /api/inventory/customers/{id}

GET    /api/inventory/sales
POST   /api/inventory/sales
GET    /api/inventory/sales/{id}

POST   /api/inventory/sales/{id}/reserve
POST   /api/inventory/sales/{id}/submit-financing
POST   /api/inventory/sales/{id}/confirm
POST   /api/inventory/sales/{id}/cancel

GET    /api/inventory/financing
GET    /api/inventory/financing/{id}
POST   /api/inventory/financing/{id}/submit
POST   /api/inventory/financing/{id}/approve
POST   /api/inventory/financing/{id}/reject

POST   /api/inventory/documents
GET    /api/inventory/customers/{id}/documents
```

Use existing API response wrappers and error format.

---

# 52. SECURITY

Reuse existing authentication.

Do not create a second authentication system.

Reuse existing:

- Current user
- Tenant
- Roles
- Permissions
- JWT/session handling

Recommended permissions:

```text
INVENTORY_VIEW
INVENTORY_CREATE
INVENTORY_UPDATE
INVENTORY_ADJUST
INVENTORY_TRANSFER

SALE_VIEW
SALE_CREATE
SALE_CONFIRM
SALE_CANCEL

FINANCING_VIEW
FINANCING_SUBMIT
FINANCING_APPROVE
FINANCING_REJECT

DOCUMENT_VIEW
DOCUMENT_UPLOAD
DOCUMENT_VERIFY
DOCUMENT_DELETE
```

Use existing permission conventions if they differ.

---

# 53. TENANT SECURITY

Every request must execute in the correct tenant context.

Example:

```text
Request
   ↓
Authentication
   ↓
Tenant Resolution
   ↓
TenantContext
   ↓
Tenant Schema
   ↓
Repository
```

Tenant A request:

```text
tenant_001.product
```

Tenant B request:

```text
tenant_002.product
```

Never trust a tenant ID supplied directly by the frontend.

Resolve tenant from the authenticated SaaS context using the existing platform mechanism.

---

# 54. DATABASE SECURITY

Do not allow cross-tenant access.

Validate:

- Tenant schema
- Current user
- Permissions
- Resource ownership

Never build schema names from untrusted user input.

If dynamic schema selection is required, resolve the schema name from the trusted tenant record.

---

# 55. TRANSACTIONS

Use transactions for state-changing business operations.

Example:

```text
Create Sale
+
Reserve Inventory
+
Create Reservation
```

must be atomic.

Confirm sale:

```text
Sale Status Update
+
Inventory Status Update
+
Inventory Movement
+
Reservation Conversion
+
Audit Log
```

must be atomic.

Use:

```java
@Transactional
```

according to existing project conventions.

---

# 56. CONCURRENCY

Prevent two salespeople from reserving the same VIN.

Example:

```text
Salesperson 1
    ↓
Reserve VIN001

Salesperson 2
    ↓
Reserve VIN001
    ↓
REJECT
```

Use:

- Database unique constraint
- Partial unique index
- Transaction
- Locking strategy where required

Frontend validation alone is not enough.

---

# 57. IDEMPOTENCY

For external financing callbacks/events, support idempotent processing.

If the same approval event is received twice:

```text
APPROVED
APPROVED
```

the second event must not:

- Create duplicate sale movements
- Decrease stock twice
- Create duplicate documents
- Duplicate audit events

Use external reference/event ID where appropriate.

---

# 58. ERROR HANDLING

Use existing global exception handling.

Business exceptions should include meaningful codes.

Examples:

```text
INVENTORY_NOT_AVAILABLE
INVENTORY_ALREADY_RESERVED
INVALID_INVENTORY_STATUS
DUPLICATE_VIN
DUPLICATE_SERIAL
DUPLICATE_SKU
DUPLICATE_BARCODE
SALE_NOT_FOUND
FINANCING_NOT_FOUND
INVALID_FINANCING_STATUS
REQUIRED_DOCUMENT_MISSING
TENANT_ACCESS_DENIED
```

Do not expose database/internal errors to frontend.

---

# 59. DATABASE INDEXING

Add indexes for common searches.

Examples:

```text
product.sku
product.barcode
product.name
product.category_id

product_variant.sku
product_variant.barcode
product_variant.product_id

inventory_item.vin
inventory_item.serial_no
inventory_item.engine_no
inventory_item.variant_id
inventory_item.status
inventory_item.warehouse_id

sale.sale_no
sale.customer_id
sale.salesperson_id
sale.sale_status

financing_application.application_no
financing_application.sale_id
financing_application.customer_id
financing_application.status

document.document_type
document.status
```

Use indexes based on existing PostgreSQL conventions and actual query patterns.

---

# 60. DATA INTEGRITY

Database must enforce:

- Unique SKU
- Unique barcode
- Unique VIN
- Unique serial number
- Unique engine number where applicable
- Valid foreign keys
- Valid status values
- Valid positive quantities
- Valid monetary values
- One active reservation per serialized item

Do not rely only on application validation.

---

# 61. STOCK INTEGRITY

Never allow:

```text
Available = -1
```

unless negative inventory is explicitly supported by existing business requirements.

Use transaction-safe stock updates.

For serialized inventory:

```text
AVAILABLE → RESERVED
```

must verify current status.

For example:

```text
AVAILABLE → RESERVED = valid

SOLD → RESERVED = invalid
DELIVERED → RESERVED = invalid
```

---

# 62. PRODUCT DELETION

Do not physically delete products that have historical transactions.

Use:

```text
ACTIVE
INACTIVE
```

or soft-delete according to existing project conventions.

Historical:

- Sales
- Inventory movements
- Documents
- Audit logs

must remain traceable.

---

# 63. INVENTORY ITEM DELETION

Do not hard-delete an Inventory Item after it has transaction history.

Use status/history.

Example:

```text
DAMAGED
RETURNED
SOLD
DELIVERED
```

Keep historical references.

---

# 64. AUDIT REQUIREMENTS

Audit at least:

```text
Product
Variant
Inventory Item
Inventory Movement
Reservation
Sale
Sale Item
Financing Application
Document
Warehouse
```

Important state transitions must record old/new status.

---

# 65. API PAGINATION

Use the existing pagination implementation.

Do not invent a new pagination response if the project already has one.

Inventory tables must support:

- Page
- Size
- Sort
- Search
- Filters

---

# 66. SEARCH

Inventory search:

```text
SKU
Barcode
Product
Variant
VIN
Engine Number
Serial Number
Warehouse
Status
```

Sales search:

```text
Sale Number
Customer
Phone
Salesperson
VIN
Status
Date
```

Financing search:

```text
Application Number
Customer
Sale
Bank
Status
Date
```

---

# 67. REPORTING

Initial dashboard:

```text
Total Products
Total Variants
Total Inventory
Available
Reserved
Loan Pending
Sold
Delivered
Low Stock
```

Sales:

```text
Today's Sales
Monthly Sales
Pending Financing
Approved Financing
Rejected Financing
Pending Delivery
Sales by Salesperson
Sales by Product
```

---

# 68. END-TO-END EXAMPLE

Tenant:

```text
ABC Motors
Schema:
tenant_001
```

Product:

```text
Honda Dream 2026
```

Variant:

```text
Black / 125cc
```

Purchase:

```text
500 motorcycles
```

Inventory:

```text
VIN001 ... VIN500
```

Initial state:

```text
AVAILABLE = 500
```

Salesperson 1 sells:

```text
VIN001
```

Customer:

```text
Customer A
```

Create:

```text
SALE-000001
```

Reserve:

```text
VIN001 → RESERVED
```

Submit financing:

```text
FIN-000001
Bank A
Status = UNDER_REVIEW
```

Vehicle:

```text
VIN001 → LOAN_PENDING
```

Bank approves:

```text
FIN-000001 → APPROVED
SALE-000001 → CONFIRMED
VIN001 → SOLD
```

After delivery:

```text
VIN001 → DELIVERED
```

All events are recorded in the audit/timeline.

---

# 69. REJECTION EXAMPLE

Bank rejects:

```text
FIN-000001 → REJECTED
```

Then:

```text
Reservation → RELEASED
VIN001 → AVAILABLE
Sale → CANCELLED
```

The motorcycle can now be sold to another customer.

Do not delete the rejected financing record.

---

# 70. TESTING

## Backend unit tests

Test:

- Stock calculation
- Status transitions
- Variant generation
- SKU generation
- Reservation validation
- Financing state transitions

## Integration tests

Test:

- Product creation
- Variant creation
- Inventory creation
- Purchase
- Reservation
- Sale
- Financing
- Approval
- Rejection
- Cancellation
- Delivery
- Document upload
- Tenant isolation

## Concurrency test

Test:

```text
Two users reserve same VIN simultaneously
```

Expected:

```text
One succeeds
One fails
```

## Tenant test

```text
Tenant A cannot read Tenant B product
Tenant A cannot read Tenant B customer
Tenant A cannot read Tenant B sale
Tenant A cannot read Tenant B inventory
```

---

# 71. MIGRATION TESTING

When a new tenant subscribes:

```text
Create tenant
↓
Create schema
↓
Run all tenant migrations
↓
Verify Inventory tables
↓
Verify Mini Loan tables
↓
Tenant ready
```

Existing tenants must also be migrated safely.

Do not assume all tenants are on the same database state.

---

# 72. IMPLEMENTATION PHASES

Do not implement everything in one large change.

## Phase 1 — Discovery

Inspect the existing application.

Produce:

```text
architecture summary
tenant strategy
schema strategy
migration strategy
Mini Loan integration strategy
frontend structure
backend structure
```

Do not code until the architecture is understood.

## Phase 2 — Tenant Database

Implement tenant migration support for Inventory.

Create:

```text
product
product_variant
attribute
attribute_value
product_attribute
variant_attribute
```

## Phase 3 — Inventory

Implement:

```text
warehouse
warehouse_location
inventory_item
inventory_batch
inventory_movement
```

## Phase 4 — Customers and Sales

Implement:

```text
customer
sale
sale_item
inventory_reservation
```

## Phase 5 — Financing

Implement:

```text
bank
financing_application
```

Then implement integration with Mini Loan.

## Phase 6 — Documents

Implement:

```text
file_storage
document
document_link
```

Reuse existing file storage when possible.

## Phase 7 — Audit

Implement or reuse:

```text
audit_log
timeline
```

## Phase 8 — Backend APIs

Implement APIs incrementally.

## Phase 9 — Next.js

Implement:

```text
Inventory Dashboard
Product List
Create Product
Product Detail
Inventory Item
Warehouse
Stock Movement
Customer
Sales
Financing
Documents
Timeline
```

## Phase 10 — Testing

Run all tests.

## Phase 11 — Review

Verify:

```text
Tenant isolation
Mini Loan compatibility
Stock integrity
Concurrency
Transactions
Permissions
Documents
Audit
Performance
```

---

# 73. AI AGENT CODING RULES

When coding:

1. Inspect before modifying.
2. Reuse existing architecture.
3. Reuse existing components.
4. Reuse existing authentication.
5. Reuse existing tenant resolution.
6. Reuse existing migration framework.
7. Reuse existing API response structure.
8. Reuse existing exception handling.
9. Reuse existing file storage.
10. Reuse existing audit infrastructure if available.
11. Do not create duplicate frameworks.
12. Do not directly modify Mini Loan tables.
13. Do not create direct database foreign keys to Mini Loan domain tables.
14. Do not hard-code tenant schema names.
15. Never trust tenant information from frontend input.
16. Use transactions for stock-changing operations.
17. Use database constraints for uniqueness.
18. Preserve historical transaction data.
19. Do not hard-delete transactional records.
20. Add tests with every business feature.
21. Keep migrations backward compatible.
22. Do not break existing features.
23. Keep frontend and backend types consistent.
24. Use clear domain terminology.
25. Keep the Inventory module independently maintainable.

---

# 74. FINAL ACCEPTANCE CRITERIA

The feature is complete when:

- [ ] Existing SaaS subscription creates tenant schema correctly
- [ ] Inventory tables are created in each tenant schema
- [ ] Existing Mini Loan continues to work
- [ ] Inventory does not directly depend on Mini Loan tables
- [ ] Product CRUD works
- [ ] Product variants work
- [ ] Dynamic attributes work
- [ ] Quantity tracking works
- [ ] Batch tracking works
- [ ] Serialized tracking works
- [ ] VIN tracking works
- [ ] Engine number tracking works
- [ ] Warehouse/location works
- [ ] Inventory movements work
- [ ] Stock calculations are correct
- [ ] Customer management works
- [ ] Sales work
- [ ] Reservation works
- [ ] Double reservation is prevented
- [ ] Financing workflow works
- [ ] Mini Loan integration works through the correct boundary
- [ ] Financing approval changes sale/inventory correctly
- [ ] Financing rejection releases inventory correctly
- [ ] Documents work
- [ ] Document versioning works
- [ ] Customer 360 works
- [ ] Vehicle timeline works
- [ ] Audit history works
- [ ] Tenant isolation works
- [ ] Permissions work
- [ ] Concurrent operations are safe
- [ ] Existing application tests pass
- [ ] New backend tests pass
- [ ] New frontend tests pass

---

# 75. FINAL BUSINESS ARCHITECTURE

```text
                     SaaS PLATFORM
                          │
                    Subscription
                          │
                          ▼
                       TENANT
                          │
                    tenant_001
                          │
        ┌─────────────────┴─────────────────┐
        │                                   │
        ▼                                   ▼
   MINI LOAN DOMAIN                   INVENTORY DOMAIN
                                            │
                                      ┌─────┴─────┐
                                      │           │
                                   PRODUCT      SALES
                                      │           │
                                   VARIANT     CUSTOMER
                                      │           │
                              INVENTORY ITEM  RESERVATION
                                      │           │
                                      └─────┬─────┘
                                            │
                                       FINANCING
                                            │
                                            ▼
                                      Mini Loan API
                                            │
                                            ▼
                                        DOCUMENTS

All important operations
            │
            ▼
       AUDIT / TIMELINE
```

The key design principle is:

> **Tenant schema provides data isolation. Domain boundaries provide business isolation. API/events provide integration between Inventory and Mini Loan.**
