package com.cambofreelance.authenticationservice.services;

import com.cambofreelance.authenticationservice.dto.request.ArticleTypeRequest;
import com.cambofreelance.authenticationservice.dto.response.ArticleTypeResponse;
import java.util.List;

public interface ArticleTypeService {

    List<ArticleTypeResponse> listActive();

    ArticleTypeResponse create(ArticleTypeRequest request);

    ArticleTypeResponse update(String id, ArticleTypeRequest request);

    void delete(String id);
}
