package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.MediaType;
import com.cambofreelance.webbackend.dto.request.MediaPresignRequest;
import com.cambofreelance.webbackend.dto.response.MediaFileResponse;
import com.cambofreelance.webbackend.dto.response.MediaPresignResponse;
import com.cambofreelance.webbackend.entities.CmsSettingEntity;
import com.cambofreelance.webbackend.entities.MediaFileEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.CmsSettingRepository;
import com.cambofreelance.webbackend.repository.MediaRepository;
import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.services.MediaService;
import com.cambofreelance.webbackend.services.SpacesService;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private static final Duration PRESIGN_EXPIRY = Duration.ofMinutes(15);

    private final MediaRepository    mediaRepository;
    private final CmsSettingRepository settingRepository;
    private final SpacesService      spacesService;

    // ── Presign ───────────────────────────────────────────────────────────────

    @Override
    @Transactional
    @Auditable(action = "UPLOAD", module = "MEDIA")
    public MediaPresignResponse presign(MediaPresignRequest request, String uploadedBy) {
        String originalName = request.getFileName();
        String ext = originalName.contains(".")
            ? originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase()
            : "";

        String detectedType = MediaType.detect(ext);
        if (detectedType == null) {
            throw new AppException("UNSUPPORTED_MEDIA_TYPE",
                "File type '." + ext + "' is not supported");
        }

        Map<String, String> cfg = loadStorageConfig();
        validateStorageConfig(cfg);

        String rawBucket = cfg.get("storage_bucket");
        String accessKey = cfg.get("storage_access_key");
        String secretKey = cfg.get("storage_secret_key");
        String endpoint  = cfg.get("storage_endpoint");
        String region    = cfg.get("storage_region");
        String publicUrl = cfg.get("storage_public_url");

        // Support "bucketName/folder-prefix" notation — split so the SDK gets the real bucket name
        String actualBucket = rawBucket.contains("/") ? rawBucket.substring(0, rawBucket.indexOf('/')) : rawBucket;
        String keyPrefix    = rawBucket.contains("/") ? rawBucket.substring(rawBucket.indexOf('/') + 1) + "/" : "";

        String objectKey  = keyPrefix + detectedType.toLowerCase() + "/" + UUID.randomUUID() + "." + ext;
        String mimeType   = StringUtils.hasText(request.getMimeType()) ? request.getMimeType() : "application/octet-stream";
        String filePublicUrl = buildPublicUrl(publicUrl, actualBucket, region, endpoint, objectKey);

        String presignEndpoint = StringUtils.hasText(publicUrl) ? publicUrl : endpoint;

        String presignedUrl = spacesService.presignPut(
            presignEndpoint, region, actualBucket, accessKey, secretKey, objectKey, mimeType, PRESIGN_EXPIRY
        );

        String mediaId = UUID.randomUUID().toString();
        MediaFileEntity entity = new MediaFileEntity();
        entity.setId(mediaId);
        entity.setFileName(objectKey.substring(objectKey.lastIndexOf('/') + 1));
        entity.setOriginalName(originalName);
        entity.setMediaType(detectedType);
        entity.setMimeType(mimeType);
        entity.setFileSize(request.getFileSize());
        entity.setFilePath(objectKey);
        entity.setFileUrl(filePublicUrl);
        entity.setUploadedBy(uploadedBy);
        entity.setCreatedBy(StringUtils.hasText(uploadedBy) ? uploadedBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_INIT);

        mediaRepository.save(entity);

        return MediaPresignResponse.builder()
            .mediaId(mediaId)
            .presignedUrl(presignedUrl)
            .publicUrl(filePublicUrl)
            .objectKey(objectKey)
            .build();
    }

    // ── Confirm ───────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public MediaFileResponse confirmUpload(String mediaId) {
        MediaFileEntity entity = mediaRepository.findById(mediaId)
            .filter(e -> Constants.STATUS_INIT.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("MEDIA_NOT_FOUND",
                    "Pending upload not found: " + mediaId);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });

        entity.setStatus(Constants.STATUS_ACTIVE);
        return MediaFileResponse.from(mediaRepository.save(entity));
    }

    // ── List / get / delete ───────────────────────────────────────────────────

    @Override
    public Page<MediaFileResponse> list(String mediaType, String search, int page, int size) {
        String type   = StringUtils.hasText(mediaType) ? mediaType.toUpperCase()                   : null;
        String filter = StringUtils.hasText(search)    ? "%" + search.trim().toLowerCase() + "%" : null;

        return mediaRepository
            .findAllActive(type, filter, PageRequest.of(page, size))
            .map(MediaFileResponse::from);
    }

    @Override
    public MediaFileResponse getById(String id) {
        return mediaRepository.findById(id)
            .filter(e -> Constants.STATUS_ACTIVE.equals(e.getStatus()))
            .map(MediaFileResponse::from)
            .orElseThrow(() -> {
                AppException ex = new AppException("MEDIA_NOT_FOUND", "Media not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "MEDIA")
    public void delete(String id) {
        MediaFileEntity entity = mediaRepository.findById(id)
            .filter(e -> Constants.STATUS_ACTIVE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("MEDIA_NOT_FOUND", "Media not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
        entity.setStatus(Constants.STATUS_DELETE);
        mediaRepository.save(entity);
    }

    // ── View (proxy) ──────────────────────────────────────────────────────────

    @Override
    public ResponseEntity<byte[]> viewFile(String id) {
        MediaFileEntity entity = mediaRepository.findById(id)
            .filter(e -> Constants.STATUS_ACTIVE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("MEDIA_NOT_FOUND", "Media not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });

        Map<String, String> cfg = loadStorageConfig();
        validateStorageConfig(cfg);

        String rawBucket  = cfg.get("storage_bucket");
        String accessKey  = cfg.get("storage_access_key");
        String secretKey  = cfg.get("storage_secret_key");
        String endpoint   = cfg.get("storage_endpoint");
        String region     = cfg.get("storage_region");
        String actualBucket = rawBucket.contains("/") ? rawBucket.substring(0, rawBucket.indexOf('/')) : rawBucket;

        byte[] data = spacesService.getObject(endpoint, region, actualBucket, accessKey, secretKey, entity.getFilePath());

        String contentType = StringUtils.hasText(entity.getMimeType()) ? entity.getMimeType() : "application/octet-stream";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, contentType);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + entity.getOriginalName() + "\"");
        headers.setContentLength(data.length);

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Map<String, String> loadStorageConfig() {
        return settingRepository.findAllBySettingGroup("STORAGE").stream()
            .collect(Collectors.toMap(
                CmsSettingEntity::getSettingKey,
                e -> e.getSettingValue() != null ? e.getSettingValue() : ""
            ));
    }

    private void validateStorageConfig(Map<String, String> cfg) {
        if (!StringUtils.hasText(cfg.get("storage_bucket")))
            throw new AppException("STORAGE_NOT_CONFIGURED", "Storage bucket is not configured");
        if (!StringUtils.hasText(cfg.get("storage_access_key")))
            throw new AppException("STORAGE_NOT_CONFIGURED", "Storage access key is not configured");
        if (!StringUtils.hasText(cfg.get("storage_secret_key")))
            throw new AppException("STORAGE_NOT_CONFIGURED", "Storage secret key is not configured");
        if (!StringUtils.hasText(cfg.get("storage_endpoint")))
            throw new AppException("STORAGE_NOT_CONFIGURED", "Storage endpoint is not configured");
        if (!StringUtils.hasText(cfg.get("storage_region")))
            throw new AppException("STORAGE_NOT_CONFIGURED", "Storage region is not configured");
    }

    /**
     * Builds the public-facing URL for a stored object.
     * If publicUrl is configured use it; otherwise derive from endpoint + bucket.
     */
    private String buildPublicUrl(String publicUrl, String bucket, String region, String endpoint, String objectKey) {
        if (StringUtils.hasText(publicUrl)) {
            String base = publicUrl.endsWith("/") ? publicUrl : publicUrl + "/";
            return base + objectKey;
        }
        // Use path-style fallback — virtual-hosted style (bucket.host) fails SSL when bucket name contains dots
        String base = endpoint.endsWith("/") ? endpoint : endpoint + "/";
        return base + bucket + "/" + objectKey;
    }
}
