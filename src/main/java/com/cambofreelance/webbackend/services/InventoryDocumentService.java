package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.InventoryDocumentUploadRequest;
import com.cambofreelance.webbackend.dto.response.InventoryDocumentResponse;
import java.util.List;

public interface InventoryDocumentService {

    InventoryDocumentResponse upload(InventoryDocumentUploadRequest request, String userId);

    List<InventoryDocumentResponse> getByOwner(String ownerType, String ownerId);

    InventoryDocumentResponse verify(String documentId, String userId);

    void delete(String documentId, String userId);
}
