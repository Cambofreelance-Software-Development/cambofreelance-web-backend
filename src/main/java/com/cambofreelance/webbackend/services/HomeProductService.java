package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.HomeProductRequest;
import com.cambofreelance.webbackend.dto.response.HomeProductResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface HomeProductService {

    List<HomeProductResponse> listAll();

    Page<HomeProductResponse> search(String search, int page, int size);

    HomeProductResponse getById(String id);

    HomeProductResponse create(HomeProductRequest request, String createdBy);

    HomeProductResponse update(String id, HomeProductRequest request, String updatedBy);

    void delete(String id);
}
