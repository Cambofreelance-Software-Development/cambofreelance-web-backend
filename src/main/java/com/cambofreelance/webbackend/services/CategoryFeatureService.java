package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.CategoryFeatureRequest;
import com.cambofreelance.webbackend.dto.response.CategoryFeatureResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CategoryFeatureService {

    List<CategoryFeatureResponse> listAll();

    Page<CategoryFeatureResponse> search(String search, int page, int size);

    CategoryFeatureResponse getById(String id);

    CategoryFeatureResponse create(CategoryFeatureRequest request, String createdBy);

    CategoryFeatureResponse update(String id, CategoryFeatureRequest request, String updatedBy);

    void delete(String id);
}
