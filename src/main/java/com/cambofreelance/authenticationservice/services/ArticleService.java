package com.cambofreelance.authenticationservice.services;

import com.cambofreelance.authenticationservice.dto.request.ArticleCreateRequest;
import com.cambofreelance.authenticationservice.dto.request.ArticleUpdateRequest;
import com.cambofreelance.authenticationservice.dto.response.ArticleResponse;
import org.springframework.data.domain.Page;

public interface ArticleService {

    ArticleResponse create(ArticleCreateRequest request, String createdBy);

    ArticleResponse update(String id, ArticleUpdateRequest request, String updatedBy);

    void delete(String id);

    ArticleResponse updateStatus(String id, String status, String updatedBy);

    Page<ArticleResponse> list(String type, String workflowStatus, String authorId, String search, int page, int size);

    ArticleResponse getById(String id);

    ArticleResponse getBySlug(String slug);

    ArticleResponse copy(String id, String createdBy);
}
