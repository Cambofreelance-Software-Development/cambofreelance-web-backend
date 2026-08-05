package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.HelpCenterCategoryRequest;
import com.cambofreelance.webbackend.dto.response.HelpCenterCategoryResponse;
import java.util.List;

public interface HelpCenterCategoryService {

    List<HelpCenterCategoryResponse> listActive(String articleTypeId);

    /** Public convenience lookup — frontend knows the type's code (e.g. HELP_CENTER), not its id. */
    List<HelpCenterCategoryResponse> listActiveByArticleTypeCode(String articleTypeCode);

    HelpCenterCategoryResponse create(HelpCenterCategoryRequest request);

    HelpCenterCategoryResponse update(String id, HelpCenterCategoryRequest request);

    void delete(String id);
}
