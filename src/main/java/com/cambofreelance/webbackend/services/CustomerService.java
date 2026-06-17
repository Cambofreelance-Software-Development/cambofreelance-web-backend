package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.CustomerCreateRequest;
import com.cambofreelance.webbackend.dto.request.CustomerDocumentUploadRequest;
import com.cambofreelance.webbackend.dto.request.CustomerStatusActionRequest;
import com.cambofreelance.webbackend.dto.request.CustomerUpdateRequest;
import com.cambofreelance.webbackend.dto.response.CustomerDocumentResponse;
import com.cambofreelance.webbackend.dto.response.CustomerResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CustomerService {

    CustomerResponse create(CustomerCreateRequest request, String createdByUserId);

    CustomerResponse update(String customerId, CustomerUpdateRequest request, String updatedByUserId);

    CustomerResponse getById(String customerId);

    Page<CustomerResponse> search(String search, String onboardingStatus, int page, int size);

    CustomerDocumentResponse uploadDocument(String customerId, CustomerDocumentUploadRequest request, String uploadedByUserId);

    List<CustomerDocumentResponse> listDocuments(String customerId);

    void deleteDocument(String customerId, String documentId, String userId);

    CustomerResponse submitForVerification(String customerId, String userId);

    CustomerResponse verify(String customerId, CustomerStatusActionRequest request, String userId);

    CustomerResponse approve(String customerId, CustomerStatusActionRequest request, String userId);

    CustomerResponse reject(String customerId, CustomerStatusActionRequest request, String userId);

    CustomerResponse activate(String customerId, String userId);

    CustomerResponse deactivate(String customerId, String userId);
}
