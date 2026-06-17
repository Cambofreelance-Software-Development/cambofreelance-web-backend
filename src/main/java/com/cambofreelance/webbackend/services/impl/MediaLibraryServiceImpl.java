package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.MediaLibraryAddRequest;
import com.cambofreelance.webbackend.dto.request.MediaLibraryUpdateRequest;
import com.cambofreelance.webbackend.dto.response.MediaLibraryResponse;
import com.cambofreelance.webbackend.entities.MediaLibraryEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.MediaLibraryRepository;
import com.cambofreelance.webbackend.repository.MediaRepository;
import com.cambofreelance.webbackend.services.MediaLibraryService;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MediaLibraryServiceImpl implements MediaLibraryService {

    private final MediaLibraryRepository mediaLibraryRepository;
    private final MediaRepository mediaRepository;

    @Override
    @Transactional
    @Auditable(action = "ADD", module = "MEDIA_LIBRARY")
    public MediaLibraryResponse add(MediaLibraryAddRequest request, String userId) {
        var mediaFile = mediaRepository.findById(request.getMediaId())
            .filter(e -> Constants.STATUS_ACTIVE.equals(e.getStatus()))
            .orElseThrow(() -> {
                var ex = new AppException("MEDIA_NOT_FOUND",
                    "Active media file not found: " + request.getMediaId());
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });

        if (mediaLibraryRepository.existsByMediaId(request.getMediaId())) {
            throw new AppException("MEDIA_ALREADY_IN_LIBRARY",
                "This media file is already in the library");
        }

        var entity = new MediaLibraryEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setMediaId(mediaFile.getId());
        entity.setFolder(request.getFolder());
        entity.setTitle(request.getTitle());
        entity.setAltText(request.getAltText());
        entity.setDescription(request.getDescription());
        entity.setTags(request.getTags());
        entity.setUploadedBy(userId);
        entity.setCreatedBy(StringUtils.hasText(userId) ? userId : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);

        var saved = mediaLibraryRepository.save(entity);
        saved.setMediaFile(mediaFile);
        return MediaLibraryResponse.from(saved);
    }

    @Override
    public Page<MediaLibraryResponse> list(String folder, String mediaType, String search, int page, int size) {
        String folderFilter     = StringUtils.hasText(folder)    ? folder.trim()                           : null;
        String mediaTypeFilter  = StringUtils.hasText(mediaType) ? mediaType.toUpperCase()                 : null;
        String searchFilter     = StringUtils.hasText(search)    ? "%" + search.trim().toLowerCase() + "%" : null;

        return mediaLibraryRepository
            .findAllActive(folderFilter, mediaTypeFilter, searchFilter, PageRequest.of(page, size))
            .map(MediaLibraryResponse::from);
    }

    @Override
    public MediaLibraryResponse getById(String id) {
        return mediaLibraryRepository.findByIdAndStatus(id, Constants.STATUS_ACTIVE)
            .map(MediaLibraryResponse::from)
            .orElseThrow(() -> {
                var ex = new AppException("MEDIA_LIBRARY_NOT_FOUND", "Library item not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "MEDIA_LIBRARY")
    public MediaLibraryResponse update(String id, MediaLibraryUpdateRequest request, String userId) {
        var entity = findActiveOrThrow(id);

        entity.setFolder(request.getFolder());
        entity.setTitle(request.getTitle());
        entity.setAltText(request.getAltText());
        entity.setDescription(request.getDescription());
        entity.setTags(request.getTags());
        entity.setUpdatedBy(userId);

        return MediaLibraryResponse.from(mediaLibraryRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "MEDIA_LIBRARY")
    public void delete(String id, String userId) {
        var entity = findActiveOrThrow(id);
        entity.setStatus(Constants.STATUS_DELETE);
        entity.setUpdatedBy(userId);
        mediaLibraryRepository.save(entity);
    }

    private MediaLibraryEntity findActiveOrThrow(String id) {
        return mediaLibraryRepository.findByIdAndStatus(id, Constants.STATUS_ACTIVE)
            .orElseThrow(() -> {
                var ex = new AppException("MEDIA_LIBRARY_NOT_FOUND", "Library item not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
