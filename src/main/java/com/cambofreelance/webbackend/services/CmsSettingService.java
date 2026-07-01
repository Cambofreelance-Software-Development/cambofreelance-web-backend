package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.CdnSettingRequest;
import com.cambofreelance.webbackend.dto.request.ContactSettingRequest;
import com.cambofreelance.webbackend.dto.request.IpWhitelistRequest;
import java.util.List;
import com.cambofreelance.webbackend.dto.request.CmsGeneralSettingRequest;
import com.cambofreelance.webbackend.dto.request.CmsSeoSettingRequest;
import com.cambofreelance.webbackend.dto.request.StorageSettingRequest;
import com.cambofreelance.webbackend.dto.response.CdnSettingResponse;
import com.cambofreelance.webbackend.dto.response.CmsGeneralSettingResponse;
import com.cambofreelance.webbackend.dto.response.CmsSeoSettingResponse;
import com.cambofreelance.webbackend.dto.response.ContactSettingResponse;
import com.cambofreelance.webbackend.dto.response.IpWhitelistResponse;
import com.cambofreelance.webbackend.dto.response.SitePublicConfigResponse;
import com.cambofreelance.webbackend.dto.response.SiteStatsResponse;
import com.cambofreelance.webbackend.dto.response.StorageSettingResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CmsSettingService {

    CmsGeneralSettingResponse getGeneralSettings();
    CmsGeneralSettingResponse updateGeneralSettings(CmsGeneralSettingRequest request);
    String uploadLogo(MultipartFile file);

    CmsSeoSettingResponse getSeoSettings();
    CmsSeoSettingResponse updateSeoSettings(CmsSeoSettingRequest request);

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

    ContactSettingResponse getContactSettings();
    ContactSettingResponse updateContactSettings(ContactSettingRequest request);
}
