package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.MediaLibraryAddRequest;
import com.cambofreelance.webbackend.dto.request.MediaLibraryUpdateRequest;
import com.cambofreelance.webbackend.dto.response.MediaLibraryResponse;
import org.springframework.data.domain.Page;

public interface MediaLibraryService {

    MediaLibraryResponse add(MediaLibraryAddRequest request, String userId);

    Page<MediaLibraryResponse> list(String folder, String mediaType, String search, int page, int size);

    MediaLibraryResponse getById(String id);

    MediaLibraryResponse update(String id, MediaLibraryUpdateRequest request, String userId);

    void delete(String id, String userId);
}
