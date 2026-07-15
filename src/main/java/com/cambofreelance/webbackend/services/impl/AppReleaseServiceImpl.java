package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.AppReleaseRequest;
import com.cambofreelance.webbackend.dto.response.AppReleaseResponse;
import com.cambofreelance.webbackend.entities.AppReleaseEntity;
import com.cambofreelance.webbackend.entities.MediaFileEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.AppReleaseRepository;
import com.cambofreelance.webbackend.repository.MediaRepository;
import com.cambofreelance.webbackend.services.AppReleaseService;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AppReleaseServiceImpl implements AppReleaseService {

    private final AppReleaseRepository appReleaseRepository;
    private final MediaRepository      mediaRepository;

    @Override
    public List<AppReleaseResponse> listAll() {
        return appReleaseRepository.findAllActive()
            .stream()
            .map(AppReleaseResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public AppReleaseResponse latestByPlatform(String platform) {
        return appReleaseRepository.findActiveByPlatform(platform)
            .stream()
            .findFirst()
            .map(AppReleaseResponse::from)
            .orElseThrow(() -> notFound(platform));
    }

    @Override
    public Page<AppReleaseResponse> search(String search, String platform, int page, int size) {
        String searchFilter = StringUtils.hasText(search)
            ? "%" + search.trim().toLowerCase() + "%"
            : null;
        String platformFilter = StringUtils.hasText(platform)
            ? platform.trim().toUpperCase()
            : null;
        return appReleaseRepository.searchActive(searchFilter, platformFilter, PageRequest.of(page, size))
            .map(AppReleaseResponse::from);
    }

    @Override
    public AppReleaseResponse getById(String id) {
        return AppReleaseResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "APP_RELEASE")
    public AppReleaseResponse create(AppReleaseRequest request, String createdBy) {
        AppReleaseEntity entity = new AppReleaseEntity();
        entity.setId(UUID.randomUUID().toString());
        applyRequest(entity, request);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        return AppReleaseResponse.from(appReleaseRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "APP_RELEASE", entityClass = AppReleaseEntity.class)
    public AppReleaseResponse update(String id, AppReleaseRequest request, String updatedBy) {
        AppReleaseEntity entity = requireById(id);
        applyRequest(entity, request);
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        return AppReleaseResponse.from(appReleaseRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "APP_RELEASE", entityClass = AppReleaseEntity.class)
    public void delete(String id) {
        AppReleaseEntity entity = requireById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        appReleaseRepository.save(entity);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void applyRequest(AppReleaseEntity entity, AppReleaseRequest request) {
        entity.setAppName(request.getAppName().trim());
        entity.setPlatform(request.getPlatform().trim().toUpperCase());
        entity.setVersionName(request.getVersionName().trim());
        entity.setVersionCode(request.getVersionCode());
        entity.setDownloadUrl(request.getDownloadUrl());
        entity.setFileSize(request.getFileSize());
        entity.setMinOsVersion(request.getMinOsVersion());
        entity.setReleaseNotes(request.getReleaseNotes());
        entity.setReleaseNotesKh(request.getReleaseNotesKh());
        entity.setForceUpdate(Boolean.TRUE.equals(request.getForceUpdate()));
        entity.setReleaseDate(request.getReleaseDate());
        resolveFile(entity, request.getFileId());
    }

    private void resolveFile(AppReleaseEntity entity, String fileId) {
        if (StringUtils.hasText(fileId)) {
            MediaFileEntity file = mediaRepository.findById(fileId)
                .filter(m -> Constants.STATUS_ACTIVE.equals(m.getStatus()))
                .orElse(null);
            entity.setFile(file);
        } else {
            entity.setFile(null);
        }
    }

    private AppReleaseEntity requireById(String id) {
        return appReleaseRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> notFound(id));
    }

    private AppException notFound(String ref) {
        AppException ex = new AppException("APP_RELEASE_NOT_FOUND", "App release not found: " + ref);
        ex.setHttpStatus(HttpStatus.NOT_FOUND);
        return ex;
    }
}
