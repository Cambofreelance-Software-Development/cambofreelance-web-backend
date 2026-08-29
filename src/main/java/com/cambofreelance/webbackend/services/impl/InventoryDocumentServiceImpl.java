package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.InventoryDocumentUploadRequest;
import com.cambofreelance.webbackend.dto.response.InventoryDocumentResponse;
import com.cambofreelance.webbackend.entities.InventoryDocumentEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.InventoryDocumentRepository;
import com.cambofreelance.webbackend.services.InventoryDocumentService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class InventoryDocumentServiceImpl implements InventoryDocumentService {

    private final InventoryDocumentRepository inventoryDocumentRepository;

    @Override
    @Transactional
    @Auditable(action = "UPLOAD_DOCUMENT", module = "INVENTORY_DOCUMENTS")
    public InventoryDocumentResponse upload(InventoryDocumentUploadRequest request, String userId) {
        String actor = StringUtils.hasText(userId) ? userId : Constants.SYSTEM;

        long existingCount = inventoryDocumentRepository.countByOwnerTypeAndOwnerId(
            request.getOwnerType().toUpperCase(), request.getOwnerId());

        InventoryDocumentEntity entity = new InventoryDocumentEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setOwnerType(request.getOwnerType().toUpperCase());
        entity.setOwnerId(request.getOwnerId());
        entity.setMediaId(request.getMediaId());
        entity.setDocumentType(request.getDocumentType().toUpperCase());
        entity.setDocumentName(request.getDocumentName().trim());
        entity.setFileUrl(request.getFileUrl());
        entity.setMimeType(request.getMimeType());
        entity.setFileSize(request.getFileSize());
        entity.setDocumentStatus("UPLOADED");
        entity.setVersion((int) existingCount + 1);
        entity.setExpiresAt(request.getExpiresAt());
        entity.setCreatedBy(actor);
        entity.setCreatedAt(new Date());
        entity.setStatus(Constants.STATUS_ACTIVE);
        inventoryDocumentRepository.save(entity);

        return InventoryDocumentResponse.from(entity);
    }

    @Override
    public List<InventoryDocumentResponse> getByOwner(String ownerType, String ownerId) {
        return inventoryDocumentRepository.findByOwnerTypeAndOwnerIdOrderByCreatedAtDesc(
            ownerType.toUpperCase(), ownerId)
            .stream()
            .map(InventoryDocumentResponse::from)
            .toList();
    }

    @Override
    @Transactional
    @Auditable(action = "VERIFY_DOCUMENT", module = "INVENTORY_DOCUMENTS")
    public InventoryDocumentResponse verify(String documentId, String userId) {
        InventoryDocumentEntity doc = inventoryDocumentRepository.findById(documentId)
            .orElseThrow(() -> new AppException("DOCUMENT_NOT_FOUND", "Document not found: " + documentId));

        doc.setDocumentStatus("VERIFIED");
        doc.setVerifiedBy(StringUtils.hasText(userId) ? userId : Constants.SYSTEM);
        doc.setVerifiedAt(new Date());
        doc.setUpdatedAt(new Date());
        inventoryDocumentRepository.save(doc);

        return InventoryDocumentResponse.from(doc);
    }

    @Override
    @Transactional
    public void delete(String documentId, String userId) {
        InventoryDocumentEntity doc = inventoryDocumentRepository.findById(documentId)
            .orElseThrow(() -> new AppException("DOCUMENT_NOT_FOUND", "Document not found: " + documentId));

        doc.setStatus(Constants.STATUS_DELETE);
        doc.setUpdatedBy(StringUtils.hasText(userId) ? userId : Constants.SYSTEM);
        doc.setUpdatedAt(new Date());
        inventoryDocumentRepository.save(doc);
    }
}
