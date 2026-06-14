package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.MediaPresignRequest;
import com.cambofreelance.webbackend.dto.response.MediaFileResponse;
import com.cambofreelance.webbackend.dto.response.MediaPresignResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface MediaService {

    /** Step 1 – generate a pre-signed PUT URL and create a pending record. */
    MediaPresignResponse presign(MediaPresignRequest request, String uploadedBy);

    /** Step 2 – called after the client finishes the direct PUT to Spaces. */
    MediaFileResponse confirmUpload(String mediaId);

    Page<MediaFileResponse> list(String mediaType, String search, int page, int size);

    MediaFileResponse getById(String id);

    void delete(String id);

    /** Proxy the raw file bytes from Spaces back to the HTTP client. */
    ResponseEntity<byte[]> viewFile(String id);
}
