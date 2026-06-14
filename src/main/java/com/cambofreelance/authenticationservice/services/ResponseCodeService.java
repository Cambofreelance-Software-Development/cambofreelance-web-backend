package com.cambofreelance.authenticationservice.services;

import com.cambofreelance.authenticationservice.dto.request.ResponseCodeRequest;
import com.cambofreelance.authenticationservice.dto.response.ResponseCodeResponse;
import org.springframework.data.domain.Page;

public interface ResponseCodeService {

    Page<ResponseCodeResponse> list(String search, String type, String status, String serviceType, int page, int size);

    ResponseCodeResponse getById(Long id);

    ResponseCodeResponse create(ResponseCodeRequest request);

    ResponseCodeResponse update(Long id, ResponseCodeRequest request);

    void delete(Long id);

    void reloadCache();
}
