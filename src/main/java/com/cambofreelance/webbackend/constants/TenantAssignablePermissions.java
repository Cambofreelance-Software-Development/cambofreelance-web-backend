package com.cambofreelance.webbackend.constants;

import java.util.Set;

/**
 * The only permission codes a Tenant Admin may grant to a custom role within their own
 * tenant. Deliberately the same set TENANT_ADMIN itself holds (see migrations V52/V53) —
 * a Tenant Admin can never grant a permission they don't themselves have.
 */
public final class TenantAssignablePermissions {

    public static final Set<String> ALL = Set.of(
        // Tenant administration
        "my-tenant.users.manage",
        "my-tenant.subscription.view",
        "my-tenant.subscription.upgrade",
        "my-tenant.usage.view",
        // Customer onboarding
        "customers.view", "customers.create", "customers.update", "customers.submit",
        "customers.verify", "customers.approve", "customers.reject", "customers.activate",
        "customer-documents.upload", "customer-documents.view", "customer-documents.delete",
        // Loan applications
        "loan-applications.view", "loan-applications.create", "loan-applications.update",
        "loan-applications.submit", "loan-applications.approve", "loan-applications.reject",
        // Loan disbursements
        "loan-disbursements.view", "loan-disbursements.create",
        "loan-disbursements.upload-contract", "loan-disbursements.disburse",
        // Repayment schedule
        "repayment-schedule.generate", "repayment-schedule.update",
        // Payments
        "payments.view", "payments.create", "payments.reverse",
        // Collections
        "collections.view", "collections.manage",
        // Dashboard & Reports
        "dashboard.view", "reports.view",
        // Inventory & Catalog
        "inventory.products.view", "inventory.products.create", "inventory.products.update", "inventory.products.delete",
        "inventory.attributes.view", "inventory.attributes.manage",
        "inventory.warehouses.view", "inventory.warehouses.manage",
        "inventory.items.view", "inventory.items.create", "inventory.items.update",
        "inventory.batches.view", "inventory.batches.manage",
        "inventory.movements.view", "inventory.stock.adjust",
        // Sales & Financing
        "inventory.sales.view", "inventory.sales.create", "inventory.sales.confirm", "inventory.sales.cancel", "inventory.sales.deliver",
        "inventory.reserve.create", "inventory.reserve.release",
        "inventory.financing.view", "inventory.financing.submit", "inventory.financing.approve", "inventory.financing.reject",
        "inventory.docs.view", "inventory.docs.upload", "inventory.docs.verify", "inventory.docs.delete"
    );

    // Default role permission sets seeded for new tenants
    public static final Set<String> ROLE_ADMINISTRATOR = ALL;

    public static final Set<String> ROLE_LOAN_OFFICER = Set.of(
        "customers.view", "customers.create", "customers.update", "customers.submit",
        "customer-documents.upload", "customer-documents.view",
        "loan-applications.view", "loan-applications.create",
        "loan-applications.update", "loan-applications.submit",
        "collections.view", "collections.manage",
        "dashboard.view"
    );

    public static final Set<String> ROLE_APPROVER = Set.of(
        "customers.view", "customers.verify", "customers.approve", "customers.reject",
        "customer-documents.view",
        "loan-applications.view", "loan-applications.approve", "loan-applications.reject",
        "loan-disbursements.view", "loan-disbursements.create",
        "loan-disbursements.upload-contract", "loan-disbursements.disburse",
        "repayment-schedule.generate", "repayment-schedule.update",
        "dashboard.view", "reports.view"
    );

    public static final Set<String> ROLE_CASHIER = Set.of(
        "customers.view",
        "loan-applications.view",
        "repayment-schedule.update",
        "payments.view", "payments.create", "payments.reverse",
        "dashboard.view"
    );

    private TenantAssignablePermissions() {}
}
