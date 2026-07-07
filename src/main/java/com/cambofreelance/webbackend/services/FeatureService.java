package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.FeatureRequest;
import com.cambofreelance.webbackend.dto.response.FeatureResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface FeatureService {

    List<FeatureResponse> listAll();

    Page<FeatureResponse> search(String search, int page, int size);

    FeatureResponse getById(String id);

    FeatureResponse create(FeatureRequest request, String createdBy);

    FeatureResponse update(String id, FeatureRequest request, String updatedBy);

    void delete(String id);
}
