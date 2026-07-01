package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.caches.IpWhitelistCache;
import com.cambofreelance.webbackend.constants.SettingGroup;
import com.cambofreelance.webbackend.dto.request.CdnSettingRequest;
import com.cambofreelance.webbackend.dto.request.CmsGeneralSettingRequest;
import com.cambofreelance.webbackend.dto.request.CmsSeoSettingRequest;
import com.cambofreelance.webbackend.dto.request.ContactSettingRequest;
import com.cambofreelance.webbackend.dto.request.IpWhitelistRequest;
import com.cambofreelance.webbackend.dto.request.StorageSettingRequest;
import com.cambofreelance.webbackend.dto.response.CdnSettingResponse;
import com.cambofreelance.webbackend.dto.response.CmsGeneralSettingResponse;
import com.cambofreelance.webbackend.dto.response.CmsSeoSettingResponse;
import com.cambofreelance.webbackend.dto.response.ContactSettingResponse;
import com.cambofreelance.webbackend.dto.response.IpWhitelistResponse;
import com.cambofreelance.webbackend.dto.response.SitePublicConfigResponse;
import com.cambofreelance.webbackend.dto.response.SiteStatsResponse;
import com.cambofreelance.webbackend.dto.response.StorageSettingResponse;
import com.cambofreelance.webbackend.entities.CmsSettingEntity;
import com.cambofreelance.webbackend.repository.CmsSettingRepository;
import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.services.CmsSettingService;
import com.cambofreelance.webbackend.services.SpacesService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class CmsSettingServiceImpl implements CmsSettingService {

    private final CmsSettingRepository repository;
    private final SpacesService spacesService;
    private final IpWhitelistCache ipWhitelistCache;

    @Value("${cms.upload.dir:uploads/logos}")
    private String uploadDir;

    @Value("${cms.upload.base-url:/uploads/logos}")
    private String uploadBaseUrl;

    // ── General ──────────────────────────────────────────────────────────────

    @Override
    public CmsGeneralSettingResponse getGeneralSettings() {
        Map<String, String> m = loadGroup(SettingGroup.GENERAL);
        return CmsGeneralSettingResponse.builder()
            .siteName(m.getOrDefault("site_name", ""))
            .environment(m.getOrDefault("environment", "DEV"))
            .defaultLanguage(m.getOrDefault("default_language", "en"))
            .timeZone(m.getOrDefault("time_zone", "Asia/Phnom_Penh"))
            .siteLogo(m.getOrDefault("site_logo", ""))
            .build();
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "SETTINGS", description = "Updated general settings")
    public CmsGeneralSettingResponse updateGeneralSettings(CmsGeneralSettingRequest req) {
        upsert("site_name",        req.getSiteName(),        SettingGroup.GENERAL);
        upsert("environment",      req.getEnvironment(),     SettingGroup.GENERAL);
        upsert("default_language", req.getDefaultLanguage(), SettingGroup.GENERAL);
        upsert("time_zone",        req.getTimeZone(),        SettingGroup.GENERAL);
        if (req.getSiteLogo() != null) {
            upsert("site_logo", req.getSiteLogo(), SettingGroup.GENERAL);
        }
        return getGeneralSettings();
    }

    @Override
    @Transactional
    public String uploadLogo(MultipartFile file) {
        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "logo";
        String ext = original.contains(".") ? original.substring(original.lastIndexOf('.')) : ".png";
        String filename = UUID.randomUUID() + ext;

        try {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            Files.copy(file.getInputStream(), dir.resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException("Failed to store logo file", e);
        }

        String logoUrl = uploadBaseUrl + "/" + filename;
        upsert("site_logo", logoUrl, SettingGroup.GENERAL);
        return logoUrl;
    }

    // ── SEO ───────────────────────────────────────────────────────────────────

    @Override
    public CmsSeoSettingResponse getSeoSettings() {
        Map<String, String> m = loadGroup(SettingGroup.SEO);
        return CmsSeoSettingResponse.builder()
            .title(m.getOrDefault("seo_title", ""))
            .description(m.getOrDefault("seo_description", ""))
            .keywords(m.getOrDefault("seo_keywords", ""))
            .canonicalUrl(m.getOrDefault("seo_canonical_url", ""))
            .robots(m.getOrDefault("seo_robots", "index,follow"))
            .ogTitle(m.getOrDefault("seo_og_title", ""))
            .ogDescription(m.getOrDefault("seo_og_description", ""))
            .ogImage(m.getOrDefault("seo_og_image", ""))
            .build();
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "SETTINGS", description = "Updated SEO settings")
    public CmsSeoSettingResponse updateSeoSettings(CmsSeoSettingRequest req) {
        upsert("seo_title",          req.getTitle(),          SettingGroup.SEO);
        upsert("seo_description",    req.getDescription(),    SettingGroup.SEO);
        upsert("seo_keywords",       req.getKeywords(),       SettingGroup.SEO);
        upsert("seo_canonical_url",  req.getCanonicalUrl(),   SettingGroup.SEO);
        upsert("seo_robots",         req.getRobots(),         SettingGroup.SEO);
        upsert("seo_og_title",       req.getOgTitle(),        SettingGroup.SEO);
        upsert("seo_og_description", req.getOgDescription(),  SettingGroup.SEO);
        upsert("seo_og_image",       req.getOgImage(),        SettingGroup.SEO);
        return getSeoSettings();
    }

    // ── CDN ───────────────────────────────────────────────────────────────────

    @Override
    public CdnSettingResponse getCdnSettings() {
        Map<String, String> m = loadGroup(SettingGroup.CDN);
        return CdnSettingResponse.builder()
            .enabled(Boolean.parseBoolean(m.getOrDefault("cdn_enabled", "false")))
            .provider(m.getOrDefault("cdn_provider", ""))
            .baseUrl(m.getOrDefault("cdn_base_url", ""))
            .customDomain(m.getOrDefault("cdn_custom_domain", ""))
            .build();
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "SETTINGS", description = "Updated CDN settings")
    public CdnSettingResponse updateCdnSettings(CdnSettingRequest req) {
        upsert("cdn_enabled",       String.valueOf(req.isEnabled()), SettingGroup.CDN);
        upsert("cdn_provider",      req.getProvider(),               SettingGroup.CDN);
        upsert("cdn_base_url",      req.getBaseUrl(),                SettingGroup.CDN);
        upsert("cdn_custom_domain", req.getCustomDomain(),           SettingGroup.CDN);
        return getCdnSettings();
    }

    // ── Storage ───────────────────────────────────────────────────────────────

    @Override
    public StorageSettingResponse getStorageSettings() {
        Map<String, String> m = loadGroup(SettingGroup.STORAGE);
        return StorageSettingResponse.builder()
            .provider(m.getOrDefault("storage_provider", "MINIO"))
            .bucket(m.getOrDefault("storage_bucket", ""))
            .accessKey(m.getOrDefault("storage_access_key", ""))
            .endpoint(m.getOrDefault("storage_endpoint", ""))
            .region(m.getOrDefault("storage_region", ""))
            .publicUrl(m.getOrDefault("storage_public_url", ""))
            .build();
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "SETTINGS", description = "Updated storage settings")
    public StorageSettingResponse updateStorageSettings(StorageSettingRequest req) {
        upsert("storage_provider",   req.getProvider(),   SettingGroup.STORAGE);
        upsert("storage_bucket",     req.getBucket(),     SettingGroup.STORAGE);
        upsert("storage_access_key", req.getAccessKey(),  SettingGroup.STORAGE);
        // Blank secretKey means "keep existing" — do not overwrite
        if (StringUtils.hasText(req.getSecretKey())) {
            upsert("storage_secret_key", req.getSecretKey(), SettingGroup.STORAGE);
        }
        upsert("storage_endpoint",   req.getEndpoint(),   SettingGroup.STORAGE);
        upsert("storage_region",     req.getRegion(),     SettingGroup.STORAGE);
        upsert("storage_public_url", req.getPublicUrl(),  SettingGroup.STORAGE);

        // Auto-apply CORS after transaction commits so the DB lock is released before the network call
        if (StringUtils.hasText(req.getEndpoint()) && StringUtils.hasText(req.getBucket())) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    try {
                        setupStorageCors(List.of("*"));
                        log.info("CORS rules auto-applied to bucket after storage settings save");
                    } catch (Exception e) {
                        log.error("Auto CORS setup failed: {}", e.getMessage(), e);
                    }
                }
            });
        }

        return getStorageSettings();
    }

    // ── Storage CORS ─────────────────────────────────────────────────────────

    @Override
    public void setupStorageCors(List<String> allowedOrigins) {
        Map<String, String> cfg = loadGroup(SettingGroup.STORAGE);
        String bucket    = cfg.getOrDefault("storage_bucket",     "");
        String accessKey = cfg.getOrDefault("storage_access_key", "");
        String secretKey = cfg.getOrDefault("storage_secret_key", "");
        String endpoint  = cfg.getOrDefault("storage_endpoint",   "");
        String region    = cfg.getOrDefault("storage_region",     "");

        if (!StringUtils.hasText(endpoint) || !StringUtils.hasText(bucket) ||
            !StringUtils.hasText(accessKey) || !StringUtils.hasText(secretKey)) {
            throw new IllegalStateException("Storage settings incomplete — endpoint, bucket, accessKey and secretKey are required");
        }

        // Strip any folder prefix — the actual bucket name is the part before the first "/"
        String actualBucket = bucket.contains("/") ? bucket.substring(0, bucket.indexOf('/')) : bucket;

        log.info("Applying CORS to bucket '{}' via endpoint '{}'", actualBucket, endpoint);
        spacesService.setupBucketCors(endpoint, region, actualBucket, accessKey, secretKey, allowedOrigins);
        log.info("CORS applied successfully to bucket '{}'", actualBucket);
    }

    // ── IP Whitelist ──────────────────────────────────────────────────────────

    @Override
    public IpWhitelistResponse getIpWhitelistSettings() {
        Map<String, String> m = loadGroup(SettingGroup.IP_WHITELIST);
        boolean enabled = Boolean.parseBoolean(m.getOrDefault("ip_whitelist_enabled", "false"));
        String raw = m.getOrDefault("ip_whitelist_rules", "");
        List<String> rules = (raw != null && !raw.isBlank())
            ? Arrays.stream(raw.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList()
            : List.of();
        return IpWhitelistResponse.builder()
            .enabled(enabled)
            .rules(rules)
            .build();
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "SETTINGS", description = "Updated IP whitelist settings")
    public IpWhitelistResponse updateIpWhitelistSettings(IpWhitelistRequest request) {
        upsert("ip_whitelist_enabled", String.valueOf(request.isEnabled()), SettingGroup.IP_WHITELIST);
        String rulesValue = (request.getRules() != null)
            ? String.join(",", request.getRules())
            : "";
        upsert("ip_whitelist_rules", rulesValue, SettingGroup.IP_WHITELIST);
        ipWhitelistCache.refresh();
        return getIpWhitelistSettings();
    }

    // ── Stats ─────────────────────────────────────────────────────────────────

    @Override
    public SiteStatsResponse getStatsSettings() {
        Map<String, String> m = loadGroup(SettingGroup.STATS);
        return SiteStatsResponse.builder()
            .projectsCompleted(parseInt(m.getOrDefault("stat_projects_completed", "50")))
            .happyClients(parseInt(m.getOrDefault("stat_happy_clients", "25")))
            .clientSatisfaction(parseInt(m.getOrDefault("stat_client_satisfaction", "100")))
            .build();
    }

    @Override
    @Transactional
    public SiteStatsResponse updateStatsSettings(int projectsCompleted, int happyClients, int clientSatisfaction) {
        upsert("stat_projects_completed", String.valueOf(projectsCompleted), SettingGroup.STATS);
        upsert("stat_happy_clients",      String.valueOf(happyClients),      SettingGroup.STATS);
        upsert("stat_client_satisfaction",String.valueOf(clientSatisfaction), SettingGroup.STATS);
        return getStatsSettings();
    }

    // ── Public site config ────────────────────────────────────────────────────

    @Override
    public SitePublicConfigResponse getSitePublicConfig() {
        Map<String, String> general = loadGroup(SettingGroup.GENERAL);
        Map<String, String> contact = loadGroup(SettingGroup.CONTACT);
        Map<String, String> social  = loadGroup(SettingGroup.SOCIAL);
        return SitePublicConfigResponse.builder()
            .siteName(general.getOrDefault("site_name", "Cambo Freelance"))
            .siteDescription(general.getOrDefault("site_description",
                "Professional freelance team from Cambodia delivering technology-driven solutions."))
            .siteLogo(general.getOrDefault("site_logo", ""))
            .siteAddress(contact.getOrDefault("contact_address", general.getOrDefault("site_address", "")))
            .siteEmail(contact.getOrDefault("contact_email", general.getOrDefault("site_email", "")))
            .sitePhone(contact.getOrDefault("contact_phone", general.getOrDefault("site_phone", "")))
            .footerCopyright(contact.getOrDefault("contact_footer_copyright", ""))
            .socialTelegram(social.getOrDefault("social_telegram", ""))
            .socialTwitter(social.getOrDefault("social_twitter", ""))
            .socialLinkedin(social.getOrDefault("social_linkedin", ""))
            .socialInstagram(social.getOrDefault("social_instagram", ""))
            .socialFacebook(social.getOrDefault("social_facebook", ""))
            .build();
    }

    // ── Contact & Social ──────────────────────────────────────────────────────

    @Override
    public ContactSettingResponse getContactSettings() {
        Map<String, String> contact = loadGroup(SettingGroup.CONTACT);
        Map<String, String> social  = loadGroup(SettingGroup.SOCIAL);
        return ContactSettingResponse.builder()
            .siteEmail(contact.getOrDefault("contact_email", ""))
            .sitePhone(contact.getOrDefault("contact_phone", ""))
            .siteAddress(contact.getOrDefault("contact_address", ""))
            .footerCopyright(contact.getOrDefault("contact_footer_copyright", ""))
            .socialTelegram(social.getOrDefault("social_telegram", ""))
            .socialFacebook(social.getOrDefault("social_facebook", ""))
            .socialLinkedin(social.getOrDefault("social_linkedin", ""))
            .socialTwitter(social.getOrDefault("social_twitter", ""))
            .socialInstagram(social.getOrDefault("social_instagram", ""))
            .build();
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "SETTINGS", description = "Updated contact & social settings")
    public ContactSettingResponse updateContactSettings(ContactSettingRequest req) {
        upsert("contact_email",            orEmpty(req.getSiteEmail()),        SettingGroup.CONTACT);
        upsert("contact_phone",            orEmpty(req.getSitePhone()),        SettingGroup.CONTACT);
        upsert("contact_address",          orEmpty(req.getSiteAddress()),      SettingGroup.CONTACT);
        upsert("contact_footer_copyright", orEmpty(req.getFooterCopyright()),  SettingGroup.CONTACT);
        upsert("social_telegram",          orEmpty(req.getSocialTelegram()),   SettingGroup.SOCIAL);
        upsert("social_facebook",          orEmpty(req.getSocialFacebook()),   SettingGroup.SOCIAL);
        upsert("social_linkedin",          orEmpty(req.getSocialLinkedin()),   SettingGroup.SOCIAL);
        upsert("social_twitter",           orEmpty(req.getSocialTwitter()),    SettingGroup.SOCIAL);
        upsert("social_instagram",         orEmpty(req.getSocialInstagram()),  SettingGroup.SOCIAL);
        return getContactSettings();
    }

    private String orEmpty(String value) {
        return value != null ? value : "";
    }

    private int parseInt(String value) {
        try { return Integer.parseInt(value.trim()); } catch (Exception e) { return 0; }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Map<String, String> loadGroup(String group) {
        List<CmsSettingEntity> rows = repository.findAllBySettingGroup(group);
        return rows.stream()
            .collect(Collectors.toMap(CmsSettingEntity::getSettingKey, e ->
                e.getSettingValue() != null ? e.getSettingValue() : ""));
    }

    private void upsert(String key, String value, String group) {
        CmsSettingEntity entity = repository.findBySettingKey(key)
            .orElseGet(() -> {
                CmsSettingEntity e = new CmsSettingEntity();
                e.setSettingId(UUID.randomUUID().toString());
                e.setSettingKey(key);
                e.setSettingGroup(group);
                return e;
            });
        entity.setSettingValue(value);
        entity.setUpdatedAt(new Date());
        repository.save(entity);
    }
}
