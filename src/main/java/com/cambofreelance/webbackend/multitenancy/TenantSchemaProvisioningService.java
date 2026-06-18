package com.cambofreelance.webbackend.multitenancy;

import com.cambofreelance.webbackend.logger.exceptions.AppException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

/**
 * Creates a dedicated Postgres schema for a tenant and runs the tenant-scoped Flyway
 * migration set (db/tenant-migration) against it. The main application migrations under
 * db/migration are unaffected — they're explicitly scoped to the "public" schema only.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TenantSchemaProvisioningService {

    private static final String TENANT_MIGRATION_LOCATION = "classpath:db/tenant-migration";

    private final DataSource dataSource;

    public void provisionSchema(String tenantId) {
        String schema = TenantSchemaUtil.schemaName(tenantId);

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS \"" + schema + "\"");
        } catch (SQLException ex) {
            throw new AppException("SCHEMA_PROVISION_FAILED", "Failed to create schema for tenant: " + tenantId);
        }

        Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
            .schemas(schema)
            .locations(TENANT_MIGRATION_LOCATION)
            .load();

        // Repair before migrating so that any previously-failed migration entry is cleared
        // from flyway_schema_history and retried instead of blocking all subsequent migrations.
        flyway.repair();
        flyway.migrate();

        log.info("Provisioned schema {} for tenantId={}", schema, tenantId);
    }
}
