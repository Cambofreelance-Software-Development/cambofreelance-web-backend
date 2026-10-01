package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.AppReleaseRequest;
import com.cambofreelance.webbackend.dto.response.AppReleaseResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface AppReleaseService {

    /** Public list; blank product defaults to SOPPOS_POS. */
    List<AppReleaseResponse> listPublic(String product);

    /** CMS list; blank product means no product filter. */
    List<AppReleaseResponse> listAll(String product);

    /** Blank product defaults to SOPPOS_POS. */
    AppReleaseResponse latestByPlatform(String platform, String product);

    /** Blank product means no product filter. */
    Page<AppReleaseResponse> search(String search, String platform, String product, int page, int size);

    AppReleaseResponse getById(String id);

    AppReleaseResponse create(AppReleaseRequest request, String createdBy);

    AppReleaseResponse update(String id, AppReleaseRequest request, String updatedBy);

    void delete(String id);
}
