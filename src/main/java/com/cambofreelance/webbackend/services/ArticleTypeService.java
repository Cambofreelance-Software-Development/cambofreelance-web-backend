package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.ArticleTypeRequest;
import com.cambofreelance.webbackend.dto.response.ArticleTypeResponse;
import java.util.List;

public interface ArticleTypeService {

    List<ArticleTypeResponse> listActive();

    ArticleTypeResponse create(ArticleTypeRequest request);

    ArticleTypeResponse update(String id, ArticleTypeRequest request);

    void delete(String id);
}
