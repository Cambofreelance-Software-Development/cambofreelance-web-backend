package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.FaqRequest;
import com.cambofreelance.webbackend.dto.response.FaqResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface FaqService {

    List<FaqResponse> listAll();

    Page<FaqResponse> search(String search, int page, int size);

    FaqResponse getById(String id);

    FaqResponse create(FaqRequest request, String createdBy);

    FaqResponse update(String id, FaqRequest request, String updatedBy);

    void delete(String id);
}
