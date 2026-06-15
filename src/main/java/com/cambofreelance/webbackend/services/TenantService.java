package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.TenantCreateRequest;
import com.cambofreelance.webbackend.dto.request.TenantUpdateRequest;
import com.cambofreelance.webbackend.dto.response.TenantResponse;
import org.springframework.data.domain.Page;

public interface TenantService {

    Page<TenantResponse> list(String search, String status, String tenantType, int page, int size);

    TenantResponse getById(String id);

    TenantResponse create(TenantCreateRequest request);

    TenantResponse update(String id, TenantUpdateRequest request);

    void delete(String id);

    TenantResponse updateStatus(String id, String status);
}
