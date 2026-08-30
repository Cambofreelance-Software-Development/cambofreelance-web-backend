package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.CdnSettingRequest;
import com.cambofreelance.webbackend.dto.request.IpWhitelistRequest;
import java.util.List;
import com.cambofreelance.webbackend.dto.request.CmsGeneralSettingRequest;
import com.cambofreelance.webbackend.dto.request.CmsSeoSettingRequest;
import com.cambofreelance.webbackend.dto.request.CmsSocialSettingRequest;
import com.cambofreelance.webbackend.dto.request.HardwarePageSettingRequest;
import com.cambofreelance.webbackend.dto.request.HomepagePageSettingRequest;
import com.cambofreelance.webbackend.dto.request.PageCtasRequest;
import com.cambofreelance.webbackend.dto.request.PageHeroesRequest;
import com.cambofreelance.webbackend.dto.request.PartnerCtaSettingRequest;
import com.cambofreelance.webbackend.dto.request.SmtpSettingRequest;
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
import com.cambofreelance.webbackend.dto.response.SmtpSettingResponse;
import com.cambofreelance.webbackend.dto.response.StorageSettingResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CmsSettingService {

    CmsGeneralSettingResponse getGeneralSettings();
    CmsGeneralSettingResponse updateGeneralSettings(CmsGeneralSettingRequest request);
    String uploadLogo(MultipartFile file);

    /** Sends a one-off test email to confirm outgoing SMTP is actually working. Throws on failure. */
    void sendTestEmail(String to);

    SmtpSettingResponse getSmtpSettings();
    SmtpSettingResponse updateSmtpSettings(SmtpSettingRequest request);

    CmsSeoSettingResponse getSeoSettings();
    CmsSeoSettingResponse updateSeoSettings(CmsSeoSettingRequest request);

    CmsSocialSettingResponse getSocialSettings();
    CmsSocialSettingResponse updateSocialSettings(CmsSocialSettingRequest request);

    CdnSettingResponse getCdnSettings();
    CdnSettingResponse updateCdnSettings(CdnSettingRequest request);

    StorageSettingResponse getStorageSettings();
    StorageSettingResponse updateStorageSettings(StorageSettingRequest request);

    /** Applies CORS rules to the configured bucket so browsers can PUT directly. */
    void setupStorageCors(List<String> allowedOrigins);

    IpWhitelistResponse getIpWhitelistSettings();
    IpWhitelistResponse updateIpWhitelistSettings(IpWhitelistRequest request);

    SiteStatsResponse getStatsSettings();
    SiteStatsResponse updateStatsSettings(int projectsCompleted, int happyClients, int clientSatisfaction);

    SitePublicConfigResponse getSitePublicConfig();

    HardwarePageSettingResponse getHardwarePageSettings();
    HardwarePageSettingResponse updateHardwarePageSettings(HardwarePageSettingRequest request);

    HomepagePageSettingResponse getHomepagePageSettings();
    HomepagePageSettingResponse updateHomepagePageSettings(HomepagePageSettingRequest request);

    PartnerCtaSettingResponse getPartnerCtaSettings();
    PartnerCtaSettingResponse updatePartnerCtaSettings(PartnerCtaSettingRequest request);

    PageHeroesResponse getPageHeroes();
    PageHeroesResponse updatePageHeroes(PageHeroesRequest request);

    PageCtasResponse getPageCtas();
    PageCtasResponse updatePageCtas(PageCtasRequest request);
}
