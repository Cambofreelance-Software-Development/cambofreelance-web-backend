package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.AuthorRequest;
import com.cambofreelance.webbackend.dto.response.AuthorResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface AuthorService {

    List<AuthorResponse> listAll();

    Page<AuthorResponse> search(String search, int page, int size);

    AuthorResponse getById(String id);

    AuthorResponse create(AuthorRequest request, String createdBy);

    AuthorResponse update(String id, AuthorRequest request, String updatedBy);

    void delete(String id);
}
