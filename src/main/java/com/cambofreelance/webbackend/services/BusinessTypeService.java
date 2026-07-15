package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.BusinessTypeGroupRequest;
import com.cambofreelance.webbackend.dto.response.BusinessTypeGroupResponse;
import java.util.List;

public interface BusinessTypeService {

    List<BusinessTypeGroupResponse> listAll();

    BusinessTypeGroupResponse getById(String id);

    BusinessTypeGroupResponse create(BusinessTypeGroupRequest request, String createdBy);

    BusinessTypeGroupResponse update(String id, BusinessTypeGroupRequest request, String updatedBy);

    void delete(String id);
}
