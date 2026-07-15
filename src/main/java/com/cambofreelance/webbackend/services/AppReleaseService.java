package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.AppReleaseRequest;
import com.cambofreelance.webbackend.dto.response.AppReleaseResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface AppReleaseService {

    List<AppReleaseResponse> listAll();

    AppReleaseResponse latestByPlatform(String platform);

    Page<AppReleaseResponse> search(String search, String platform, int page, int size);

    AppReleaseResponse getById(String id);

    AppReleaseResponse create(AppReleaseRequest request, String createdBy);

    AppReleaseResponse update(String id, AppReleaseRequest request, String updatedBy);

    void delete(String id);
}
