package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.CollectionAssignRequest;
import com.cambofreelance.webbackend.dto.request.CollectionNoteCreateRequest;
import com.cambofreelance.webbackend.dto.request.CollectionStatusUpdateRequest;
import com.cambofreelance.webbackend.dto.request.PromiseToPayCreateRequest;
import com.cambofreelance.webbackend.dto.request.PtpStatusUpdateRequest;
import com.cambofreelance.webbackend.dto.response.CollectionCaseResponse;
import com.cambofreelance.webbackend.dto.response.CollectionNoteResponse;
import com.cambofreelance.webbackend.dto.response.PromiseToPayResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CollectionService {

    int syncOverdueCases(String userId);

    Page<CollectionCaseResponse> search(String collectionStatus, String assignedOfficerId,
                                        String currency, int page, int size);

    CollectionCaseResponse getById(String id);

    CollectionCaseResponse refreshCase(String id, String userId);

    CollectionCaseResponse updateStatus(String id, CollectionStatusUpdateRequest request, String userId);

    CollectionCaseResponse assign(String id, CollectionAssignRequest request, String userId);

    CollectionNoteResponse addNote(String id, CollectionNoteCreateRequest request, String userId);

    List<CollectionNoteResponse> getNotes(String id);

    PromiseToPayResponse addPromise(String id, PromiseToPayCreateRequest request, String userId);

    List<PromiseToPayResponse> getPromises(String id);

    PromiseToPayResponse updatePtpStatus(String ptpId, PtpStatusUpdateRequest request, String userId);
}
