
package com.cambofreelance.webbackend.cms;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestMethodOrder;
import org.yaml.snakeyaml.Yaml;

/**
 * Read-only verification of the {@code cms_settings} insertion / seed process against the
 * configured (shared, remote) dev Postgres.
 *
 * <p>By design this test does <b>not</b> boot the Spring application context — doing so would run
 * {@code ApiMigrateRegistry.loadComponentInit()} (which {@code save()}s roles + an admin user) and
 * Flyway migrations, both of which mutate the shared database. Instead it opens a single JDBC
 * connection pinned to {@code setReadOnly(true)} and issues SELECTs only. No INSERT/UPDATE/DELETE
 * is ever executed, so shared data is never modified.
 *
 * <p>Datasource coordinates are read from {@code application-local.yaml} on the test classpath, so
 * no credentials are duplicated here.
 */
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.MethodName.class)
class CmsSettingsInsertionReadOnlyIT {

    private static final String TABLE = "public.cms_settings";

    /** Groups that a Flyway migration is expected to seed (STATS/SOCIAL are default-backed, not seeded). */
    private static final Map<String, String[]> SEEDED_GROUPS = new LinkedHashMap<>();

    static {
        SEEDED_GROUPS.put("GENERAL",      new String[] {"site_name", "environment", "default_language", "time_zone"});
        SEEDED_GROUPS.put("SEO",          new String[] {"seo_title", "seo_robots"});
        SEEDED_GROUPS.put("CDN",          new String[] {"cdn_enabled"});
        SEEDED_GROUPS.put("STORAGE",      new String[] {"storage_provider"});
        SEEDED_GROUPS.put("IP_WHITELIST", new String[] {"ip_whitelist_enabled"});
        SEEDED_GROUPS.put("HARDWARE",     new String[] {"hardware_hero_title"});
        SEEDED_GROUPS.put("HOMEPAGE",     new String[] {"home_hero_title"});
        SEEDED_GROUPS.put("PARTNER_CTA",  new String[] {"partner_cta_title", "partner_cta_body", "partner_cta_link"});
    }

    private static Connection conn;

    @BeforeAll
    static void connect() throws Exception {
        Map<String, String> ds = loadDatasource();
        conn = DriverManager.getConnection(ds.get("url"), ds.get("username"), ds.get("password"));
        conn.setReadOnly(true);
        System.out.println("[report] connected read-only to " + ds.get("url"));
    }

    @AfterAll
    static void close() throws Exception {
        if (conn != null) {
            conn.close();
        }
    }

    // ── 1. Table exists and holds seed data ────────────────────────────────────

    @Test
    void t01_tableExistsAndNotEmpty() throws Exception {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT count(*) FROM " + TABLE)) {
            assertTrue(rs.next());
            int total = rs.getInt(1);
            System.out.println("[report] " + TABLE + " total rows = " + total);
            assertTrue(total > 0, TABLE + " is empty — no seed migrations have been applied");
        }
    }

    // ── 2. Integrity: no blank keys, no duplicates, every row has a group ───────

    @Test
    void t02_keyIntegrity() throws Exception {
        List<String> problems = new ArrayList<>();

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT count(*) FROM " + TABLE + " WHERE setting_key IS NULL OR trim(setting_key) = ''")) {
            rs.next();
            if (rs.getInt(1) > 0) problems.add(rs.getInt(1) + " row(s) with NULL/blank setting_key");
        }
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT count(*) FROM " + TABLE + " WHERE setting_group IS NULL OR trim(setting_group) = ''")) {
            rs.next();
            if (rs.getInt(1) > 0) problems.add(rs.getInt(1) + " row(s) with NULL/blank setting_group");
        }
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT setting_key, count(*) c FROM " + TABLE + " GROUP BY setting_key HAVING count(*) > 1")) {
            while (rs.next()) {
                problems.add("duplicate setting_key '" + rs.getString(1) + "' x" + rs.getInt(2));
            }
        }

        problems.forEach(p -> System.out.println("[inconsistency] " + p));
        assertTrue(problems.isEmpty(), "cms_settings integrity problems: " + problems);
    }

    // ── 3. Per-group inventory (report only, never fails) ──────────────────────

    @Test
    void t03_reportInventoryPerGroup() throws Exception {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT setting_group, count(*) FROM " + TABLE + " GROUP BY setting_group ORDER BY setting_group")) {
            System.out.println("[report] rows per setting_group:");
            while (rs.next()) {
                System.out.printf("[report]   %-14s %d%n", rs.getString(1), rs.getInt(2));
            }
        }
    }

    // ── 4. Every seeded group + its core keys are present ──────────────────────

    @TestFactory
    List<DynamicTest> t04_seededGroupsPresent() {
        List<DynamicTest> tests = new ArrayList<>();
        SEEDED_GROUPS.forEach((group, keys) -> {
            tests.add(dynamicTest("group " + group + " has rows", () -> {
                int count = countForGroup(group);
                System.out.printf("[report]   group %-14s -> %d row(s)%n", group, count);
                assertTrue(count > 0,
                    "group '" + group + "' has no rows — its seed migration has not been applied to this DB");
            }));
            for (String key : keys) {
                tests.add(dynamicTest("key " + key + " present & non-blank", () -> {
                    String value = valueForKey(key);
                    assertFalse(value == null,
                        "expected seeded key '" + key + "' (group " + group + ") is missing from cms_settings");
                    assertFalse(value.trim().isEmpty(),
                        "seeded key '" + key + "' exists but has a blank value");
                }));
            }
        });
        return tests;
    }

    // ── helpers ────────────────────────────────────────────────────────────────

    private static int countForGroup(String group) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
                 "SELECT count(*) FROM " + TABLE + " WHERE setting_group = ?")) {
            ps.setString(1, group);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    /** Returns the value for a key, or {@code null} if the key row does not exist. */
    private static String valueForKey(String key) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
                 "SELECT setting_value FROM " + TABLE + " WHERE setting_key = ?")) {
            ps.setString(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? (rs.getString(1) == null ? "" : rs.getString(1)) : null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, String> loadDatasource() throws Exception {
        try (InputStream in = CmsSettingsInsertionReadOnlyIT.class.getClassLoader()
                 .getResourceAsStream("application-local.yaml")) {
            if (in == null) {
                throw new IllegalStateException("application-local.yaml not found on test classpath");
            }
            Map<String, Object> root = new Yaml().load(in);
            Map<String, Object> spring = (Map<String, Object>) root.get("spring");
            Map<String, Object> ds = (Map<String, Object>) spring.get("datasource");
            Map<String, String> out = new LinkedHashMap<>();
            out.put("url", String.valueOf(ds.get("url")));
            out.put("username", String.valueOf(ds.get("username")));
            out.put("password", String.valueOf(ds.get("password")));
            return out;
        }
    }
}