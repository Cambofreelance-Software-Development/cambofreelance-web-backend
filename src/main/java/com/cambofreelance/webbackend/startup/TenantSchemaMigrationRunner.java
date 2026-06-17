package com.cambofreelance.webbackend.startup;

import com.cambofreelance.webbackend.entities.TenantEntity;
import com.cambofreelance.webbackend.multitenancy.TenantSchemaProvisioningService;
import com.cambofreelance.webbackend.repository.TenantRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Tenant schemas are otherwise only Flyway-migrated once, at provisioning time
 * ({@link TenantSchemaProvisioningService#provisionSchema}, called from tenant creation/approval).
 * Re-running it for every existing tenant on every boot is safe — "CREATE SCHEMA IF NOT EXISTS"
 * and Flyway's migrate() both only apply what's still pending — so any tenant-migration added
 * after a tenant was first provisioned (e.g. the customers/customer_documents tables) reaches
 * already-existing tenants automatically instead of being stuck behind a manual per-tenant trigger.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TenantSchemaMigrationRunner {

    private final TenantRepository tenantRepository;
    private final TenantSchemaProvisioningService tenantSchemaProvisioningService;

    @PostConstruct
    public void migrateExistingTenantSchemas() {
        for (TenantEntity tenant : tenantRepository.findAll()) {
            try {
                tenantSchemaProvisioningService.provisionSchema(tenant.getId());
            } catch (Exception ex) {
                log.error("Failed to apply pending tenant-schema migrations for tenantId={}", tenant.getId(), ex);
            }
        }
        log.info("Tenant schema migration sweep complete");
    }
}
