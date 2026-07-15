package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.TestimonialRequest;
import com.cambofreelance.webbackend.dto.response.TestimonialResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface TestimonialService {

    List<TestimonialResponse> listAll();

    Page<TestimonialResponse> search(String search, int page, int size);

    TestimonialResponse getById(String id);

    TestimonialResponse create(TestimonialRequest request, String createdBy);

    TestimonialResponse update(String id, TestimonialRequest request, String updatedBy);

    void delete(String id);
}
