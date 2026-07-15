package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.FeatureTabRequest;
import com.cambofreelance.webbackend.dto.response.FeatureTabResponse;
import java.util.List;

public interface FeatureTabService {

    List<FeatureTabResponse> listAll();

    FeatureTabResponse getById(String id);

    FeatureTabResponse create(FeatureTabRequest request, String createdBy);

    FeatureTabResponse update(String id, FeatureTabRequest request, String updatedBy);

    void delete(String id);
}
