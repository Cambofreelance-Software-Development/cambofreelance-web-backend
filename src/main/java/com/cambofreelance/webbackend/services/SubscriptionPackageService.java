package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.PackageCreateRequest;
import com.cambofreelance.webbackend.dto.request.PackageFeatureToggleRequest;
import com.cambofreelance.webbackend.dto.request.PackageUpdateRequest;
import com.cambofreelance.webbackend.dto.response.PackageResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface SubscriptionPackageService {

    Page<PackageResponse> list(String search, String status, int page, int size);

    List<PackageResponse> listActive();

    PackageResponse getById(String id);

    PackageResponse create(PackageCreateRequest request);

    PackageResponse update(String id, PackageUpdateRequest request);

    void delete(String id);

    PackageResponse updateFeatures(String id, PackageFeatureToggleRequest request);
}
