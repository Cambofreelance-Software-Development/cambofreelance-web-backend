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
            .siteDescription(m.getOrDefault("site_description", ""))
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
        if (req.getSiteDescription() != null) {
            upsert("site_description", req.getSiteDescription(), SettingGroup.GENERAL);
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
        upsert("social_twitter",   req.getSocialTwitter()   != null ? req.getSocialTwitter()   : "", SettingGroup.SOCIAL);
        upsert("social_linkedin",  req.getSocialLinkedin()  != null ? req.getSocialLinkedin()  : "", SettingGroup.SOCIAL);
        upsert("social_instagram", req.getSocialInstagram() != null ? req.getSocialInstagram() : "", SettingGroup.SOCIAL);
        upsert("social_facebook",  req.getSocialFacebook()  != null ? req.getSocialFacebook()  : "", SettingGroup.SOCIAL);
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
        Map<String, String> social  = loadGroup(SettingGroup.SOCIAL);
        return SitePublicConfigResponse.builder()
            .siteName(general.getOrDefault("site_name", "Cambo Freelance"))
            .siteDescription(general.getOrDefault("site_description",
                "Our digital space is undergoing a major redesign to serve you better. "
                    + "We're preparing to launch advanced Point-of-Sale (POS) management platforms "
                    + "and retail solutions very soon."))
            .siteLogo(general.getOrDefault("site_logo", ""))
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
        upsert("hardware_hero_title",           req.getHeroTitle(),          SettingGroup.HARDWARE);
        upsert("hardware_hero_title_kh",        req.getHeroTitleKh(),        SettingGroup.HARDWARE);
        upsert("hardware_hero_subtitle",        req.getHeroSubtitle(),       SettingGroup.HARDWARE);
        upsert("hardware_hero_subtitle_kh",     req.getHeroSubtitleKh(),     SettingGroup.HARDWARE);
        upsert("hardware_hero_cta_label",       req.getHeroCtaLabel(),       SettingGroup.HARDWARE);
        upsert("hardware_hero_cta_label_kh",    req.getHeroCtaLabelKh(),     SettingGroup.HARDWARE);
        upsert("hardware_hero_cta_link",        req.getHeroCtaLink(),        SettingGroup.HARDWARE);
        upsert("hardware_download_android_url", req.getDownloadAndroidUrl(), SettingGroup.HARDWARE);
        upsert("hardware_download_ios_url",     req.getDownloadIosUrl(),     SettingGroup.HARDWARE);
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
        upsert("home_hero_title",              req.getHeroTitle(),              SettingGroup.HOMEPAGE);
        upsert("home_hero_title_kh",           req.getHeroTitleKh(),            SettingGroup.HOMEPAGE);
        upsert("home_hero_subtitle",           req.getHeroSubtitle(),           SettingGroup.HOMEPAGE);
        upsert("home_hero_subtitle_kh",        req.getHeroSubtitleKh(),         SettingGroup.HOMEPAGE);
        upsert("home_hero_cta_label",          req.getHeroCtaLabel(),           SettingGroup.HOMEPAGE);
        upsert("home_hero_cta_label_kh",       req.getHeroCtaLabelKh(),         SettingGroup.HOMEPAGE);
        upsert("home_hero_cta_link",           req.getHeroCtaLink(),            SettingGroup.HOMEPAGE);
        upsert("home_hero_video_label",        req.getHeroVideoLabel(),         SettingGroup.HOMEPAGE);
        upsert("home_hero_video_label_kh",     req.getHeroVideoLabelKh(),       SettingGroup.HOMEPAGE);
        upsert("home_hero_video_url",          req.getHeroVideoUrl(),           SettingGroup.HOMEPAGE);
        upsert("home_hero_image_url",          req.getHeroImageUrl(),           SettingGroup.HOMEPAGE);

        upsert("home_metrics_title",           req.getMetricsTitle(),           SettingGroup.HOMEPAGE);
        upsert("home_metrics_title_kh",        req.getMetricsTitleKh(),         SettingGroup.HOMEPAGE);
        upsert("home_metrics_subtitle",        req.getMetricsSubtitle(),        SettingGroup.HOMEPAGE);
        upsert("home_metrics_subtitle_kh",     req.getMetricsSubtitleKh(),      SettingGroup.HOMEPAGE);
        upsert("home_lifestyle_image_1_url",   req.getLifestyleImage1Url(),     SettingGroup.HOMEPAGE);
        upsert("home_lifestyle_image_2_url",   req.getLifestyleImage2Url(),     SettingGroup.HOMEPAGE);
        upsert("home_lifestyle_image_3_url",   req.getLifestyleImage3Url(),     SettingGroup.HOMEPAGE);

        upsert("home_help_title",              req.getHelpTitle(),              SettingGroup.HOMEPAGE);
        upsert("home_help_title_kh",           req.getHelpTitleKh(),            SettingGroup.HOMEPAGE);

        upsert("home_help_livechat_title",            req.getHelpLivechatTitle(),          SettingGroup.HOMEPAGE);
        upsert("home_help_livechat_title_kh",         req.getHelpLivechatTitleKh(),        SettingGroup.HOMEPAGE);
        upsert("home_help_livechat_description",      req.getHelpLivechatDescription(),    SettingGroup.HOMEPAGE);
        upsert("home_help_livechat_description_kh",   req.getHelpLivechatDescriptionKh(),  SettingGroup.HOMEPAGE);
        upsert("home_help_livechat_cta_label",        req.getHelpLivechatCtaLabel(),       SettingGroup.HOMEPAGE);
        upsert("home_help_livechat_cta_label_kh",     req.getHelpLivechatCtaLabelKh(),     SettingGroup.HOMEPAGE);
        upsert("home_help_livechat_cta_link",         req.getHelpLivechatCtaLink(),        SettingGroup.HOMEPAGE);

        upsert("home_help_center_title",            req.getHelpCenterTitle(),          SettingGroup.HOMEPAGE);
        upsert("home_help_center_title_kh",         req.getHelpCenterTitleKh(),        SettingGroup.HOMEPAGE);
        upsert("home_help_center_description",      req.getHelpCenterDescription(),    SettingGroup.HOMEPAGE);
        upsert("home_help_center_description_kh",   req.getHelpCenterDescriptionKh(),  SettingGroup.HOMEPAGE);
        upsert("home_help_center_cta_label",        req.getHelpCenterCtaLabel(),       SettingGroup.HOMEPAGE);
        upsert("home_help_center_cta_label_kh",     req.getHelpCenterCtaLabelKh(),     SettingGroup.HOMEPAGE);
        upsert("home_help_center_cta_link",         req.getHelpCenterCtaLink(),        SettingGroup.HOMEPAGE);

        upsert("home_help_community_title",            req.getHelpCommunityTitle(),          SettingGroup.HOMEPAGE);
        upsert("home_help_community_title_kh",         req.getHelpCommunityTitleKh(),        SettingGroup.HOMEPAGE);
        upsert("home_help_community_description",      req.getHelpCommunityDescription(),    SettingGroup.HOMEPAGE);
        upsert("home_help_community_description_kh",   req.getHelpCommunityDescriptionKh(),  SettingGroup.HOMEPAGE);
        upsert("home_help_community_cta_label",        req.getHelpCommunityCtaLabel(),       SettingGroup.HOMEPAGE);
        upsert("home_help_community_cta_label_kh",     req.getHelpCommunityCtaLabelKh(),     SettingGroup.HOMEPAGE);
        upsert("home_help_community_cta_link",         req.getHelpCommunityCtaLink(),        SettingGroup.HOMEPAGE);

        upsert("home_partner_title",           req.getPartnerTitle(),           SettingGroup.HOMEPAGE);
        upsert("home_partner_title_kh",        req.getPartnerTitleKh(),         SettingGroup.HOMEPAGE);
        upsert("home_partner_description",     req.getPartnerDescription(),     SettingGroup.HOMEPAGE);
        upsert("home_partner_description_kh",  req.getPartnerDescriptionKh(),   SettingGroup.HOMEPAGE);
        upsert("home_partner_cta_label",       req.getPartnerCtaLabel(),        SettingGroup.HOMEPAGE);
        upsert("home_partner_cta_label_kh",    req.getPartnerCtaLabelKh(),      SettingGroup.HOMEPAGE);
        upsert("home_partner_cta_link",        req.getPartnerCtaLink(),         SettingGroup.HOMEPAGE);

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
        upsert("partner_cta_title",    req.getTitle(),    SettingGroup.PARTNER_CTA);
        upsert("partner_cta_title_kh", req.getTitleKh(),  SettingGroup.PARTNER_CTA);
        upsert("partner_cta_body",     req.getBody(),     SettingGroup.PARTNER_CTA);
        upsert("partner_cta_body_kh",  req.getBodyKh(),   SettingGroup.PARTNER_CTA);
        upsert("partner_cta_label",    req.getLabel(),    SettingGroup.PARTNER_CTA);
        upsert("partner_cta_label_kh", req.getLabelKh(),  SettingGroup.PARTNER_CTA);
        upsert("partner_cta_link",     req.getLink(),     SettingGroup.PARTNER_CTA);
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
            for (Map.Entry<String, PageHeroesRequest.PageHero> entry : req.getPages().entrySet()) {
                String slug = entry.getKey();
                if (!PAGE_HERO_SLUGS.contains(slug)) continue;
                PageHeroesRequest.PageHero hero = entry.getValue();
                if (hero == null) continue;
                upsert(heroKey(slug, "heading"),       hero.getHeading(),      SettingGroup.PAGE_HEROES);
                upsert(heroKey(slug, "heading_kh"),    hero.getHeadingKh(),    SettingGroup.PAGE_HEROES);
                upsert(heroKey(slug, "subheading"),    hero.getSubheading(),   SettingGroup.PAGE_HEROES);
                upsert(heroKey(slug, "subheading_kh"), hero.getSubheadingKh(), SettingGroup.PAGE_HEROES);
            }
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
            for (Map.Entry<String, PageCtasRequest.PageCta> entry : req.getPages().entrySet()) {
                String slug = entry.getKey();
                if (!PAGE_CTA_SLUGS.contains(slug)) continue;
                PageCtasRequest.PageCta cta = entry.getValue();
                if (cta == null) continue;
                upsert(ctaKey(slug, "heading"),         cta.getHeading(),        SettingGroup.PAGE_CTAS);
                upsert(ctaKey(slug, "heading_kh"),      cta.getHeadingKh(),      SettingGroup.PAGE_CTAS);
                upsert(ctaKey(slug, "subheading"),      cta.getSubheading(),     SettingGroup.PAGE_CTAS);
                upsert(ctaKey(slug, "subheading_kh"),   cta.getSubheadingKh(),   SettingGroup.PAGE_CTAS);
                upsert(ctaKey(slug, "button_label"),    cta.getButtonLabel(),    SettingGroup.PAGE_CTAS);
                upsert(ctaKey(slug, "button_label_kh"), cta.getButtonLabelKh(),  SettingGroup.PAGE_CTAS);
                upsert(ctaKey(slug, "button_link"),     cta.getButtonLink(),     SettingGroup.PAGE_CTAS);
            }
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
