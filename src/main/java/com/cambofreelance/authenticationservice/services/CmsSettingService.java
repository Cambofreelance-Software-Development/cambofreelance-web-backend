package com.cambofreelance.authenticationservice.services;

import com.cambofreelance.authenticationservice.dto.request.CdnSettingRequest;
import com.cambofreelance.authenticationservice.dto.request.IpWhitelistRequest;
import java.util.List;
import com.cambofreelance.authenticationservice.dto.request.CmsGeneralSettingRequest;
import com.cambofreelance.authenticationservice.dto.request.CmsSeoSettingRequest;
import com.cambofreelance.authenticationservice.dto.request.StorageSettingRequest;
import com.cambofreelance.authenticationservice.dto.response.CdnSettingResponse;
import com.cambofreelance.authenticationservice.dto.response.CmsGeneralSettingResponse;
import com.cambofreelance.authenticationservice.dto.response.CmsSeoSettingResponse;
import com.cambofreelance.authenticationservice.dto.response.IpWhitelistResponse;
import com.cambofreelance.authenticationservice.dto.response.SitePublicConfigResponse;
import com.cambofreelance.authenticationservice.dto.response.SiteStatsResponse;
import com.cambofreelance.authenticationservice.dto.response.StorageSettingResponse;
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
}
