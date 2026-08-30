package com.cambofreelance.webbackend.controllers;

import com.cambofreelance.webbackend.constants.ErrorCode;
import com.cambofreelance.webbackend.dto.request.CdnSettingRequest;
import com.cambofreelance.webbackend.dto.request.SiteStatsRequest;
import com.cambofreelance.webbackend.dto.request.CmsGeneralSettingRequest;
import com.cambofreelance.webbackend.dto.request.CmsSeoSettingRequest;
import com.cambofreelance.webbackend.dto.request.CmsSocialSettingRequest;
import com.cambofreelance.webbackend.dto.request.HardwarePageSettingRequest;
import com.cambofreelance.webbackend.dto.request.HomepagePageSettingRequest;
import com.cambofreelance.webbackend.dto.request.IpWhitelistRequest;
import com.cambofreelance.webbackend.dto.request.PageCtasRequest;
import com.cambofreelance.webbackend.dto.request.PageHeroesRequest;
import com.cambofreelance.webbackend.dto.request.PartnerCtaSettingRequest;
import com.cambofreelance.webbackend.dto.request.SmtpSettingRequest;
import com.cambofreelance.webbackend.dto.request.StorageSettingRequest;
import com.cambofreelance.webbackend.logger.exceptions.MessageResponse;
import com.cambofreelance.webbackend.services.CmsSettingService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/cms/settings")
@RequiredArgsConstructor
public class CmsSettingController {

    private final CmsSettingService cmsSettingService;

    // ── General ──────────────────────────────────────────────────────────────

    @GetMapping("/general")
    @PreAuthorize("hasAuthority('settings.view')")
    public ResponseEntity<Object> getGeneralSettings() {
        var result = cmsSettingService.getGeneralSettings();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/general")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> updateGeneralSettings(
        @Valid @RequestBody CmsGeneralSettingRequest request
    ) {
        var result = cmsSettingService.updateGeneralSettings(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/general/test-email")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> sendTestEmail(@RequestParam String to) {
        cmsSettingService.sendTestEmail(to);
        return new ResponseEntity<>(new MessageResponse("Test email sent.", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── SMTP ─────────────────────────────────────────────────────────────────

    @GetMapping("/smtp")
    @PreAuthorize("hasAuthority('settings.view')")
    public ResponseEntity<Object> getSmtpSettings() {
        var result = cmsSettingService.getSmtpSettings();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/smtp")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> updateSmtpSettings(
        @Valid @RequestBody SmtpSettingRequest request
    ) {
        var result = cmsSettingService.updateSmtpSettings(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/smtp/test-email")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> sendSmtpTestEmail(@RequestParam String to) {
        cmsSettingService.sendTestEmail(to);
        return new ResponseEntity<>(new MessageResponse("Test email sent.", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Social ────────────────────────────────────────────────────────────────

    @GetMapping("/social")
    @PreAuthorize("hasAuthority('settings.view')")
    public ResponseEntity<Object> getSocialSettings() {
        var result = cmsSettingService.getSocialSettings();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/social")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> updateSocialSettings(
        @Valid @RequestBody CmsSocialSettingRequest request
    ) {
        var result = cmsSettingService.updateSocialSettings(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Logo upload ───────────────────────────────────────────────────────────

    @PostMapping(value = "/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> uploadLogo(@RequestParam("file") MultipartFile file) {
        String logoUrl = cmsSettingService.uploadLogo(file);
        return new ResponseEntity<>(
            new MessageResponse(Map.of("logoUrl", logoUrl), ErrorCode.SUCCESS),
            HttpStatus.OK
        );
    }

    // ── SEO ───────────────────────────────────────────────────────────────────

    @GetMapping("/seo")
    @PreAuthorize("hasAuthority('settings.view')")
    public ResponseEntity<Object> getSeoSettings() {
        var result = cmsSettingService.getSeoSettings();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/seo")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> updateSeoSettings(
        @Valid @RequestBody CmsSeoSettingRequest request
    ) {
        var result = cmsSettingService.updateSeoSettings(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── CDN ───────────────────────────────────────────────────────────────────

    @GetMapping("/cdn")
    @PreAuthorize("hasAuthority('settings.view')")
    public ResponseEntity<Object> getCdnSettings() {
        var result = cmsSettingService.getCdnSettings();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/cdn")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> updateCdnSettings(
        @Valid @RequestBody CdnSettingRequest request
    ) {
        var result = cmsSettingService.updateCdnSettings(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Storage ───────────────────────────────────────────────────────────────

    @GetMapping("/storage")
    @PreAuthorize("hasAuthority('settings.view')")
    public ResponseEntity<Object> getStorageSettings() {
        var result = cmsSettingService.getStorageSettings();
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/storage")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> updateStorageSettings(
        @Valid @RequestBody StorageSettingRequest request
    ) {
        var result = cmsSettingService.updateStorageSettings(request);
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PostMapping("/storage/setup-cors")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> setupStorageCors(
        @RequestParam(defaultValue = "*") List<String> origins
    ) {
        cmsSettingService.setupStorageCors(origins);
        return new ResponseEntity<>(
            new MessageResponse("CORS rules applied to bucket", ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Stats (public read, protected write) ──────────────────────────────────

    /** No auth — site name, description, and social links for the public footer. */
    @GetMapping("/public")
    public ResponseEntity<Object> getSitePublicConfig() {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.getSitePublicConfig(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    /** No auth — consumed by the public home page. */
    @GetMapping("/stats")
    public ResponseEntity<Object> getStats() {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.getStatsSettings(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/stats")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> updateStats(@Valid @RequestBody SiteStatsRequest request) {
        var result = cmsSettingService.updateStatsSettings(
            request.getProjectsCompleted(),
            request.getHappyClients(),
            request.getClientSatisfaction()
        );
        return new ResponseEntity<>(new MessageResponse(result, ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Hardware page ─────────────────────────────────────────────────────────

    /** No auth — consumed by the public /en/hardware page. */
    @GetMapping("/hardware/public")
    public ResponseEntity<Object> getHardwarePagePublic() {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.getHardwarePageSettings(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/hardware")
    @PreAuthorize("hasAuthority('settings.view')")
    public ResponseEntity<Object> getHardwarePageSettings() {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.getHardwarePageSettings(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/hardware")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> updateHardwarePageSettings(
        @Valid @RequestBody HardwarePageSettingRequest request
    ) {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.updateHardwarePageSettings(request), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Homepage ──────────────────────────────────────────────────────────────

    /** No auth — consumed by the public home page. */
    @GetMapping("/homepage/public")
    public ResponseEntity<Object> getHomepagePagePublic() {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.getHomepagePageSettings(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/homepage")
    @PreAuthorize("hasAuthority('settings.view')")
    public ResponseEntity<Object> getHomepagePageSettings() {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.getHomepagePageSettings(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/homepage")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> updateHomepagePageSettings(
        @Valid @RequestBody HomepagePageSettingRequest request
    ) {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.updateHomepagePageSettings(request), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Partner CTA ───────────────────────────────────────────────────────────

    /** No auth — consumed by the public home page Partner CTA section. */
    @GetMapping("/partner-cta/public")
    public ResponseEntity<Object> getPartnerCtaPublic() {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.getPartnerCtaSettings(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/partner-cta")
    @PreAuthorize("hasAuthority('settings.view')")
    public ResponseEntity<Object> getPartnerCtaSettings() {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.getPartnerCtaSettings(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/partner-cta")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> updatePartnerCtaSettings(
        @Valid @RequestBody PartnerCtaSettingRequest request
    ) {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.updatePartnerCtaSettings(request), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Page Heroes ───────────────────────────────────────────────────────────

    /** No auth — consumed by public page hero components. */
    @GetMapping("/page-heroes/public")
    public ResponseEntity<Object> getPageHeroesPublic() {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.getPageHeroes(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/page-heroes")
    @PreAuthorize("hasAuthority('settings.view')")
    public ResponseEntity<Object> getPageHeroes() {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.getPageHeroes(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/page-heroes")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> updatePageHeroes(
        @Valid @RequestBody PageHeroesRequest request
    ) {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.updatePageHeroes(request), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── Page CTAs ─────────────────────────────────────────────────────────────

    /** No auth — consumed by public page bottom-CTA components. */
    @GetMapping("/page-ctas/public")
    public ResponseEntity<Object> getPageCtasPublic() {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.getPageCtas(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("/page-ctas")
    @PreAuthorize("hasAuthority('settings.view')")
    public ResponseEntity<Object> getPageCtas() {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.getPageCtas(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/page-ctas")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> updatePageCtas(
        @Valid @RequestBody PageCtasRequest request
    ) {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.updatePageCtas(request), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    // ── IP Whitelist ──────────────────────────────────────────────────────────

    @GetMapping("/ip-whitelist")
    @PreAuthorize("hasAuthority('settings.view')")
    public ResponseEntity<Object> getIpWhitelist() {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.getIpWhitelistSettings(), ErrorCode.SUCCESS), HttpStatus.OK);
    }

    @PutMapping("/ip-whitelist")
    @PreAuthorize("hasAuthority('settings.update')")
    public ResponseEntity<Object> updateIpWhitelist(@RequestBody IpWhitelistRequest request) {
        return new ResponseEntity<>(
            new MessageResponse(cmsSettingService.updateIpWhitelistSettings(request), ErrorCode.SUCCESS), HttpStatus.OK);
    }
}
