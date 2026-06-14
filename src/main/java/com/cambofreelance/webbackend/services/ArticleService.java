package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.ArticleCreateRequest;
import com.cambofreelance.webbackend.dto.request.ArticleUpdateRequest;
import com.cambofreelance.webbackend.dto.response.ArticleResponse;
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
