package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.CategoryHardwareRequest;
import com.cambofreelance.webbackend.dto.response.CategoryHardwareResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CategoryHardwareService {

    List<CategoryHardwareResponse> listAll();

    Page<CategoryHardwareResponse> search(String search, int page, int size);

    CategoryHardwareResponse getById(String id);

    CategoryHardwareResponse create(CategoryHardwareRequest request, String createdBy);

    CategoryHardwareResponse update(String id, CategoryHardwareRequest request, String updatedBy);

    void delete(String id);
}
