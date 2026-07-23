package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.caches.IpWhitelistCache;
import com.cambofreelance.webbackend.constants.SettingGroup;
import com.cambofreelance.webbackend.dto.request.CdnSettingRequest;
import com.cambofreelance.webbackend.dto.request.CmsGeneralSettingRequest;
import com.cambofreelance.webbackend.dto.request.CmsSeoSettingRequest;
import com.cambofreelance.webbackend.dto.request.CmsSocialSettingRequest;
import com.cambofreelance.webbackend.dto.request.HardwarePageSettingRequest;
import com.cambofreelance.webbackend.dto.request.HomepagePageSettingRequest;
import com.cambofreelance.webbackend.dto.request.IpWhitelistRequest;
import com.cambofreelance.webbackend.dto.request.PageCtasRequest;
import com.cambofreelance.webbackend.dto.request.PageHeroesRequest;
import com.cambofreelance.webbackend.dto.request.PartnerCtaSettingRequest;
import com.cambofreelance.webbackend.dto.request.StorageSettingRequest;
import com.cambofreelance.webbackend.dto.response.CdnSettingResponse;
import com.cambofreelance.webbackend.dto.response.CmsGeneralSettingResponse;
import com.cambofreelance.webbackend.dto.response.CmsSeoSettingResponse;
import com.cambofreelance.webbackend.dto.response.CmsSocialSettingResponse;
import com.cambofreelance.webbackend.dto.response.HardwarePageSettingResponse;
import com.cambofreelance.webbackend.dto.response.HomepagePageSettingResponse;
import com.cambofreelance.webbackend.dto.response.PageCtasResponse;
import com.cambofreelance.webbackend.dto.response.PageHeroesResponse;
import com.cambofreelance.webbackend.dto.response.PartnerCtaSettingResponse;
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
import java.util.LinkedHashMap;
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
            .siteLogoFooter(m.getOrDefault("site_logo_footer", ""))
            .siteDescription(m.getOrDefault("site_description", ""))
            .build();
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "SETTINGS", description = "Updated general settings")
    public CmsGeneralSettingResponse updateGeneralSettings(CmsGeneralSettingRequest req) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("site_name",        req.getSiteName());
        values.put("environment",      req.getEnvironment());
        values.put("default_language", req.getDefaultLanguage());
        values.put("time_zone",        req.getTimeZone());
        if (req.getSiteLogo() != null) {
            values.put("site_logo", req.getSiteLogo());
        }
        if (req.getSiteLogoFooter() != null) {
            values.put("site_logo_footer", req.getSiteLogoFooter());
        }
        if (req.getSiteDescription() != null) {
            values.put("site_description", req.getSiteDescription());
        }
        batchUpsert(SettingGroup.GENERAL, values);
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
        Map<String, String> values = new LinkedHashMap<>();
        values.put("seo_title",          req.getTitle());
        values.put("seo_description",    req.getDescription());
        values.put("seo_keywords",       req.getKeywords());
        values.put("seo_canonical_url",  req.getCanonicalUrl());
        values.put("seo_robots",         req.getRobots());
        values.put("seo_og_title",       req.getOgTitle());
        values.put("seo_og_description", req.getOgDescription());
        values.put("seo_og_image",       req.getOgImage());
        batchUpsert(SettingGroup.SEO, values);
        return getSeoSettings();
    }

    // ── Social ────────────────────────────────────────────────────────────────

    @Override
    public CmsSocialSettingResponse getSocialSettings() {
        Map<String, String> m = loadGroup(SettingGroup.SOCIAL);
        return CmsSocialSettingResponse.builder()
            .socialTwitter(m.getOrDefault("social_twitter", ""))
            .socialLinkedin(m.getOrDefault("social_linkedin", ""))
            .socialInstagram(m.getOrDefault("social_instagram", ""))
            .socialFacebook(m.getOrDefault("social_facebook", ""))
            .build();
    }

    @Override
    @Auditable(action = "UPDATE", module = "SETTINGS", description = "Updated social settings")
    public CmsSocialSettingResponse updateSocialSettings(CmsSocialSettingRequest req) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("social_twitter",   req.getSocialTwitter()   != null ? req.getSocialTwitter()   : "");
        values.put("social_linkedin",  req.getSocialLinkedin()  != null ? req.getSocialLinkedin()  : "");
        values.put("social_instagram", req.getSocialInstagram() != null ? req.getSocialInstagram() : "");
        values.put("social_facebook",  req.getSocialFacebook()  != null ? req.getSocialFacebook()  : "");
        batchUpsert(SettingGroup.SOCIAL, values);
        return getSocialSettings();
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
        Map<String, String> values = new LinkedHashMap<>();
        values.put("cdn_enabled",       String.valueOf(req.isEnabled()));
        values.put("cdn_provider",      req.getProvider());
        values.put("cdn_base_url",      req.getBaseUrl());
        values.put("cdn_custom_domain", req.getCustomDomain());
        batchUpsert(SettingGroup.CDN, values);
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
        Map<String, String> values = new LinkedHashMap<>();
        values.put("storage_provider",   req.getProvider());
        values.put("storage_bucket",     req.getBucket());
        values.put("storage_access_key", req.getAccessKey());
        // Blank secretKey means "keep existing" — do not overwrite
        if (StringUtils.hasText(req.getSecretKey())) {
            values.put("storage_secret_key", req.getSecretKey());
        }
        values.put("storage_endpoint",   req.getEndpoint());
        values.put("storage_region",     req.getRegion());
        values.put("storage_public_url", req.getPublicUrl());
        batchUpsert(SettingGroup.STORAGE, values);

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
        Map<String, String> values = new LinkedHashMap<>();
        values.put("ip_whitelist_enabled", String.valueOf(request.isEnabled()));
        values.put("ip_whitelist_rules", (request.getRules() != null)
            ? String.join(",", request.getRules())
            : "");
        batchUpsert(SettingGroup.IP_WHITELIST, values);
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
        Map<String, String> values = new LinkedHashMap<>();
        values.put("stat_projects_completed",  String.valueOf(projectsCompleted));
        values.put("stat_happy_clients",       String.valueOf(happyClients));
        values.put("stat_client_satisfaction", String.valueOf(clientSatisfaction));
        batchUpsert(SettingGroup.STATS, values);
        return getStatsSettings();
    }

    // ── Public site config ────────────────────────────────────────────────────

    @Override
    public SitePublicConfigResponse getSitePublicConfig() {
        Map<String, String> general = loadGroup(SettingGroup.GENERAL);
        Map<String, String> social  = loadGroup(SettingGroup.SOCIAL);
        return SitePublicConfigResponse.builder()
            .siteName(general.getOrDefault("site_name", "Cambo Freelance"))
            .siteDescription(general.getOrDefault("site_description",
                "Our digital space is undergoing a major redesign to serve you better. "
                    + "We're preparing to launch advanced Point-of-Sale (POS) management platforms "
                    + "and retail solutions very soon."))
            .siteLogo(general.getOrDefault("site_logo", ""))
            .siteLogoFooter(general.getOrDefault("site_logo_footer", ""))
            .siteAddress(general.getOrDefault("site_address", "Street 123, BKK1, Phnom Penh, Cambodia"))
            .siteEmail(general.getOrDefault("site_email", "hello@cambofreelance.com"))
            .sitePhone(general.getOrDefault("site_phone", "+855 (0) 12 345 678"))
            .socialTwitter(social.getOrDefault("social_twitter", ""))
            .socialLinkedin(social.getOrDefault("social_linkedin", ""))
            .socialInstagram(social.getOrDefault("social_instagram", ""))
            .socialFacebook(social.getOrDefault("social_facebook", ""))
            .build();
    }

    // ── Hardware page ─────────────────────────────────────────────────────────

    @Override
    public HardwarePageSettingResponse getHardwarePageSettings() {
        Map<String, String> m = loadGroup(SettingGroup.HARDWARE);
        return HardwarePageSettingResponse.builder()
            .heroTitle(m.getOrDefault("hardware_hero_title", ""))
            .heroTitleKh(m.getOrDefault("hardware_hero_title_kh", ""))
            .heroSubtitle(m.getOrDefault("hardware_hero_subtitle", ""))
            .heroSubtitleKh(m.getOrDefault("hardware_hero_subtitle_kh", ""))
            .heroCtaLabel(m.getOrDefault("hardware_hero_cta_label", ""))
            .heroCtaLabelKh(m.getOrDefault("hardware_hero_cta_label_kh", ""))
            .heroCtaLink(m.getOrDefault("hardware_hero_cta_link", ""))
            .downloadAndroidUrl(m.getOrDefault("hardware_download_android_url", ""))
            .downloadIosUrl(m.getOrDefault("hardware_download_ios_url", ""))
            .build();
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "SETTINGS", description = "Updated hardware page settings")
    public HardwarePageSettingResponse updateHardwarePageSettings(HardwarePageSettingRequest req) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("hardware_hero_title",           req.getHeroTitle());
        values.put("hardware_hero_title_kh",        req.getHeroTitleKh());
        values.put("hardware_hero_subtitle",        req.getHeroSubtitle());
        values.put("hardware_hero_subtitle_kh",     req.getHeroSubtitleKh());
        values.put("hardware_hero_cta_label",       req.getHeroCtaLabel());
        values.put("hardware_hero_cta_label_kh",    req.getHeroCtaLabelKh());
        values.put("hardware_hero_cta_link",        req.getHeroCtaLink());
        values.put("hardware_download_android_url", req.getDownloadAndroidUrl());
        values.put("hardware_download_ios_url",     req.getDownloadIosUrl());
        batchUpsert(SettingGroup.HARDWARE, values);
        return getHardwarePageSettings();
    }

    // ── Homepage ──────────────────────────────────────────────────────────────

    @Override
    public HomepagePageSettingResponse getHomepagePageSettings() {
        Map<String, String> m = loadGroup(SettingGroup.HOMEPAGE);
        return HomepagePageSettingResponse.builder()
            .heroTitle(m.getOrDefault("home_hero_title", ""))
            .heroTitleKh(m.getOrDefault("home_hero_title_kh", ""))
            .heroSubtitle(m.getOrDefault("home_hero_subtitle", ""))
            .heroSubtitleKh(m.getOrDefault("home_hero_subtitle_kh", ""))
            .heroCtaLabel(m.getOrDefault("home_hero_cta_label", ""))
            .heroCtaLabelKh(m.getOrDefault("home_hero_cta_label_kh", ""))
            .heroCtaLink(m.getOrDefault("home_hero_cta_link", ""))
            .heroVideoLabel(m.getOrDefault("home_hero_video_label", ""))
            .heroVideoLabelKh(m.getOrDefault("home_hero_video_label_kh", ""))
            .heroVideoUrl(m.getOrDefault("home_hero_video_url", ""))
            .heroImageUrl(m.getOrDefault("home_hero_image_url", ""))
            .metricsTitle(m.getOrDefault("home_metrics_title", ""))
            .metricsTitleKh(m.getOrDefault("home_metrics_title_kh", ""))
            .metricsSubtitle(m.getOrDefault("home_metrics_subtitle", ""))
            .metricsSubtitleKh(m.getOrDefault("home_metrics_subtitle_kh", ""))
            .lifestyleImage1Url(m.getOrDefault("home_lifestyle_image_1_url", ""))
            .lifestyleImage2Url(m.getOrDefault("home_lifestyle_image_2_url", ""))
            .lifestyleImage3Url(m.getOrDefault("home_lifestyle_image_3_url", ""))
            .helpTitle(m.getOrDefault("home_help_title", ""))
            .helpTitleKh(m.getOrDefault("home_help_title_kh", ""))
            .helpLivechatTitle(m.getOrDefault("home_help_livechat_title", ""))
            .helpLivechatTitleKh(m.getOrDefault("home_help_livechat_title_kh", ""))
            .helpLivechatDescription(m.getOrDefault("home_help_livechat_description", ""))
            .helpLivechatDescriptionKh(m.getOrDefault("home_help_livechat_description_kh", ""))
            .helpLivechatCtaLabel(m.getOrDefault("home_help_livechat_cta_label", ""))
            .helpLivechatCtaLabelKh(m.getOrDefault("home_help_livechat_cta_label_kh", ""))
            .helpLivechatCtaLink(m.getOrDefault("home_help_livechat_cta_link", ""))
            .helpCenterTitle(m.getOrDefault("home_help_center_title", ""))
            .helpCenterTitleKh(m.getOrDefault("home_help_center_title_kh", ""))
            .helpCenterDescription(m.getOrDefault("home_help_center_description", ""))
            .helpCenterDescriptionKh(m.getOrDefault("home_help_center_description_kh", ""))
            .helpCenterCtaLabel(m.getOrDefault("home_help_center_cta_label", ""))
            .helpCenterCtaLabelKh(m.getOrDefault("home_help_center_cta_label_kh", ""))
            .helpCenterCtaLink(m.getOrDefault("home_help_center_cta_link", ""))
            .helpCommunityTitle(m.getOrDefault("home_help_community_title", ""))
            .helpCommunityTitleKh(m.getOrDefault("home_help_community_title_kh", ""))
            .helpCommunityDescription(m.getOrDefault("home_help_community_description", ""))
            .helpCommunityDescriptionKh(m.getOrDefault("home_help_community_description_kh", ""))
            .helpCommunityCtaLabel(m.getOrDefault("home_help_community_cta_label", ""))
            .helpCommunityCtaLabelKh(m.getOrDefault("home_help_community_cta_label_kh", ""))
            .helpCommunityCtaLink(m.getOrDefault("home_help_community_cta_link", ""))
            .partnerTitle(m.getOrDefault("home_partner_title", ""))
            .partnerTitleKh(m.getOrDefault("home_partner_title_kh", ""))
            .partnerDescription(m.getOrDefault("home_partner_description", ""))
            .partnerDescriptionKh(m.getOrDefault("home_partner_description_kh", ""))
            .partnerCtaLabel(m.getOrDefault("home_partner_cta_label", ""))
            .partnerCtaLabelKh(m.getOrDefault("home_partner_cta_label_kh", ""))
            .partnerCtaLink(m.getOrDefault("home_partner_cta_link", ""))
            .build();
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "SETTINGS", description = "Updated homepage settings")
    public HomepagePageSettingResponse updateHomepagePageSettings(HomepagePageSettingRequest req) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("home_hero_title",              req.getHeroTitle());
        values.put("home_hero_title_kh",           req.getHeroTitleKh());
        values.put("home_hero_subtitle",           req.getHeroSubtitle());
        values.put("home_hero_subtitle_kh",        req.getHeroSubtitleKh());
        values.put("home_hero_cta_label",          req.getHeroCtaLabel());
        values.put("home_hero_cta_label_kh",       req.getHeroCtaLabelKh());
        values.put("home_hero_cta_link",           req.getHeroCtaLink());
        values.put("home_hero_video_label",        req.getHeroVideoLabel());
        values.put("home_hero_video_label_kh",     req.getHeroVideoLabelKh());
        values.put("home_hero_video_url",          req.getHeroVideoUrl());
        values.put("home_hero_image_url",          req.getHeroImageUrl());

        values.put("home_metrics_title",           req.getMetricsTitle());
        values.put("home_metrics_title_kh",        req.getMetricsTitleKh());
        values.put("home_metrics_subtitle",        req.getMetricsSubtitle());
        values.put("home_metrics_subtitle_kh",     req.getMetricsSubtitleKh());
        values.put("home_lifestyle_image_1_url",   req.getLifestyleImage1Url());
        values.put("home_lifestyle_image_2_url",   req.getLifestyleImage2Url());
        values.put("home_lifestyle_image_3_url",   req.getLifestyleImage3Url());

        values.put("home_help_title",              req.getHelpTitle());
        values.put("home_help_title_kh",           req.getHelpTitleKh());

        values.put("home_help_livechat_title",          req.getHelpLivechatTitle());
        values.put("home_help_livechat_title_kh",       req.getHelpLivechatTitleKh());
        values.put("home_help_livechat_description",    req.getHelpLivechatDescription());
        values.put("home_help_livechat_description_kh", req.getHelpLivechatDescriptionKh());
        values.put("home_help_livechat_cta_label",      req.getHelpLivechatCtaLabel());
        values.put("home_help_livechat_cta_label_kh",   req.getHelpLivechatCtaLabelKh());
        values.put("home_help_livechat_cta_link",       req.getHelpLivechatCtaLink());

        values.put("home_help_center_title",          req.getHelpCenterTitle());
        values.put("home_help_center_title_kh",       req.getHelpCenterTitleKh());
        values.put("home_help_center_description",    req.getHelpCenterDescription());
        values.put("home_help_center_description_kh", req.getHelpCenterDescriptionKh());
        values.put("home_help_center_cta_label",      req.getHelpCenterCtaLabel());
        values.put("home_help_center_cta_label_kh",   req.getHelpCenterCtaLabelKh());
        values.put("home_help_center_cta_link",       req.getHelpCenterCtaLink());

        values.put("home_help_community_title",          req.getHelpCommunityTitle());
        values.put("home_help_community_title_kh",       req.getHelpCommunityTitleKh());
        values.put("home_help_community_description",    req.getHelpCommunityDescription());
        values.put("home_help_community_description_kh", req.getHelpCommunityDescriptionKh());
        values.put("home_help_community_cta_label",      req.getHelpCommunityCtaLabel());
        values.put("home_help_community_cta_label_kh",   req.getHelpCommunityCtaLabelKh());
        values.put("home_help_community_cta_link",       req.getHelpCommunityCtaLink());

        values.put("home_partner_title",           req.getPartnerTitle());
        values.put("home_partner_title_kh",        req.getPartnerTitleKh());
        values.put("home_partner_description",     req.getPartnerDescription());
        values.put("home_partner_description_kh",  req.getPartnerDescriptionKh());
        values.put("home_partner_cta_label",       req.getPartnerCtaLabel());
        values.put("home_partner_cta_label_kh",    req.getPartnerCtaLabelKh());
        values.put("home_partner_cta_link",        req.getPartnerCtaLink());

        batchUpsert(SettingGroup.HOMEPAGE, values);
        return getHomepagePageSettings();
    }

    // ── Partner CTA ───────────────────────────────────────────────────────────

    @Override
    public PartnerCtaSettingResponse getPartnerCtaSettings() {
        Map<String, String> m = loadGroup(SettingGroup.PARTNER_CTA);
        return PartnerCtaSettingResponse.builder()
            .title(m.getOrDefault("partner_cta_title", ""))
            .titleKh(m.getOrDefault("partner_cta_title_kh", ""))
            .body(m.getOrDefault("partner_cta_body", ""))
            .bodyKh(m.getOrDefault("partner_cta_body_kh", ""))
            .label(m.getOrDefault("partner_cta_label", ""))
            .labelKh(m.getOrDefault("partner_cta_label_kh", ""))
            .link(m.getOrDefault("partner_cta_link", ""))
            .build();
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "SETTINGS", description = "Updated Partner CTA settings")
    public PartnerCtaSettingResponse updatePartnerCtaSettings(PartnerCtaSettingRequest req) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("partner_cta_title",    req.getTitle());
        values.put("partner_cta_title_kh", req.getTitleKh());
        values.put("partner_cta_body",     req.getBody());
        values.put("partner_cta_body_kh",  req.getBodyKh());
        values.put("partner_cta_label",    req.getLabel());
        values.put("partner_cta_label_kh", req.getLabelKh());
        values.put("partner_cta_link",     req.getLink());
        batchUpsert(SettingGroup.PARTNER_CTA, values);
        return getPartnerCtaSettings();
    }

    private int parseInt(String value) {
        try { return Integer.parseInt(value.trim()); } catch (Exception e) { return 0; }
    }

    // ── Page Heroes ───────────────────────────────────────────────────────────

    /**
     * Slugs of the public pages that have an editable hero. Must match the
     * frontend slugs used in usePageHero (see admin/settings/HomepagePageHeroes).
     * Hyphens in slugs are converted to underscores for the DB setting_key.
     */
    private static final List<String> PAGE_HERO_SLUGS = Arrays.asList(
        "articles", "business-types", "contact", "courses", "features",
        "hardware", "partner", "pricing", "products", "services",
        "team", "tutorials",
        // Home page section headings (edited alongside page heroes for convenience).
        "home-feature-tabs",
        "home-products",
        "home-business-types",
        "home-testimonials",
        "home-faq"
    );

    private static String heroKey(String slug, String field) {
        return "page_hero_" + slug.replace('-', '_') + "_" + field;
    }

    @Override
    public PageHeroesResponse getPageHeroes() {
        Map<String, String> m = loadGroup(SettingGroup.PAGE_HEROES);
        Map<String, PageHeroesResponse.PageHero> pages = new LinkedHashMap<>();
        for (String slug : PAGE_HERO_SLUGS) {
            pages.put(slug, PageHeroesResponse.PageHero.builder()
                .heading(m.getOrDefault(heroKey(slug, "heading"), ""))
                .headingKh(m.getOrDefault(heroKey(slug, "heading_kh"), ""))
                .subheading(m.getOrDefault(heroKey(slug, "subheading"), ""))
                .subheadingKh(m.getOrDefault(heroKey(slug, "subheading_kh"), ""))
                .build());
        }
        return PageHeroesResponse.builder().pages(pages).build();
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "SETTINGS", description = "Updated page heroes")
    public PageHeroesResponse updatePageHeroes(PageHeroesRequest req) {
        if (req.getPages() != null) {
            Map<String, String> values = new LinkedHashMap<>();
            for (Map.Entry<String, PageHeroesRequest.PageHero> entry : req.getPages().entrySet()) {
                String slug = entry.getKey();
                if (!PAGE_HERO_SLUGS.contains(slug)) continue;
                PageHeroesRequest.PageHero hero = entry.getValue();
                if (hero == null) continue;
                values.put(heroKey(slug, "heading"),       hero.getHeading());
                values.put(heroKey(slug, "heading_kh"),    hero.getHeadingKh());
                values.put(heroKey(slug, "subheading"),    hero.getSubheading());
                values.put(heroKey(slug, "subheading_kh"), hero.getSubheadingKh());
            }
            batchUpsert(SettingGroup.PAGE_HEROES, values);
        }
        return getPageHeroes();
    }

    // ── Page CTAs (bottom "Ready to get started?" blocks) ─────────────────────

    private static final List<String> PAGE_CTA_SLUGS = Arrays.asList(
        "business-types", "partner", "features", "hardware", "products",
        "pricing", "courses", "team", "tutorials", "services", "articles", "contact"
    );

    private static String ctaKey(String slug, String field) {
        return "page_cta_" + slug.replace('-', '_') + "_" + field;
    }

    @Override
    public PageCtasResponse getPageCtas() {
        Map<String, String> m = loadGroup(SettingGroup.PAGE_CTAS);
        Map<String, PageCtasResponse.PageCta> pages = new LinkedHashMap<>();
        for (String slug : PAGE_CTA_SLUGS) {
            pages.put(slug, PageCtasResponse.PageCta.builder()
                .heading(m.getOrDefault(ctaKey(slug, "heading"), ""))
                .headingKh(m.getOrDefault(ctaKey(slug, "heading_kh"), ""))
                .subheading(m.getOrDefault(ctaKey(slug, "subheading"), ""))
                .subheadingKh(m.getOrDefault(ctaKey(slug, "subheading_kh"), ""))
                .buttonLabel(m.getOrDefault(ctaKey(slug, "button_label"), ""))
                .buttonLabelKh(m.getOrDefault(ctaKey(slug, "button_label_kh"), ""))
                .buttonLink(m.getOrDefault(ctaKey(slug, "button_link"), ""))
                .build());
        }
        return PageCtasResponse.builder().pages(pages).build();
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "SETTINGS", description = "Updated page CTAs")
    public PageCtasResponse updatePageCtas(PageCtasRequest req) {
        if (req.getPages() != null) {
            Map<String, String> values = new LinkedHashMap<>();
            for (Map.Entry<String, PageCtasRequest.PageCta> entry : req.getPages().entrySet()) {
                String slug = entry.getKey();
                if (!PAGE_CTA_SLUGS.contains(slug)) continue;
                PageCtasRequest.PageCta cta = entry.getValue();
                if (cta == null) continue;
                values.put(ctaKey(slug, "heading"),         cta.getHeading());
                values.put(ctaKey(slug, "heading_kh"),      cta.getHeadingKh());
                values.put(ctaKey(slug, "subheading"),      cta.getSubheading());
                values.put(ctaKey(slug, "subheading_kh"),   cta.getSubheadingKh());
                values.put(ctaKey(slug, "button_label"),    cta.getButtonLabel());
                values.put(ctaKey(slug, "button_label_kh"), cta.getButtonLabelKh());
                values.put(ctaKey(slug, "button_link"),     cta.getButtonLink());
            }
            batchUpsert(SettingGroup.PAGE_CTAS, values);
        }
        return getPageCtas();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Map<String, String> loadGroup(String group) {
        List<CmsSettingEntity> rows = repository.findAllBySettingGroup(group);
        return rows.stream()
            .collect(Collectors.toMap(CmsSettingEntity::getSettingKey, e ->
                e.getSettingValue() != null ? e.getSettingValue() : ""));
    }

    /**
     * Upserts a whole settings group in one round trip: a single SELECT loads the
     * existing rows, updates ride on JPA dirty checking at commit, and only new
     * keys are inserted via saveAll. The per-key upsert() below issues 2 queries
     * per field, which for large groups (homepage = 46 fields) exceeded the
     * frontend's 10s timeout on a slow DB link.
     */
    private void batchUpsert(String group, Map<String, String> values) {
        Map<String, CmsSettingEntity> existing = repository.findAllBySettingGroup(group).stream()
            .collect(Collectors.toMap(CmsSettingEntity::getSettingKey, e -> e, (a, b) -> a));
        Date now = new Date();
        List<CmsSettingEntity> created = new java.util.ArrayList<>();
        values.forEach((key, value) -> {
            CmsSettingEntity entity = existing.get(key);
            if (entity == null) {
                entity = new CmsSettingEntity();
                entity.setSettingId(UUID.randomUUID().toString());
                entity.setSettingKey(key);
                entity.setSettingGroup(group);
                entity.setSettingValue(value);
                entity.setUpdatedAt(now);
                created.add(entity);
            } else {
                entity.setSettingValue(value);
                entity.setUpdatedAt(now);
            }
        });
        if (!created.isEmpty()) {
            repository.saveAll(created);
        }
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
