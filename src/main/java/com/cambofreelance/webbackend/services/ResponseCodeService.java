package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.ResponseCodeRequest;
import com.cambofreelance.webbackend.dto.response.ResponseCodeResponse;
import org.springframework.data.domain.Page;

public interface ResponseCodeService {

    Page<ResponseCodeResponse> list(String search, String type, String status, String serviceType, int page, int size);

    ResponseCodeResponse getById(Long id);

    ResponseCodeResponse create(ResponseCodeRequest request);

    ResponseCodeResponse update(Long id, ResponseCodeRequest request);

    void delete(Long id);

    void reloadCache();
}
