package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.BusinessTypeGroupRequest;
import com.cambofreelance.webbackend.dto.request.BusinessTypeGroupRequest.BusinessTypeTagRequest;
import com.cambofreelance.webbackend.dto.response.BusinessTypeGroupResponse;
import com.cambofreelance.webbackend.entities.BusinessTypeGroupEntity;
import com.cambofreelance.webbackend.entities.BusinessTypeTagEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.BusinessTypeGroupRepository;
import com.cambofreelance.webbackend.services.BusinessTypeService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BusinessTypeServiceImpl implements BusinessTypeService {

    private final BusinessTypeGroupRepository repository;

    @Override
    @Transactional
    public List<BusinessTypeGroupResponse> listAll() {
        return repository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(BusinessTypeGroupResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BusinessTypeGroupResponse getById(String id) {
        return BusinessTypeGroupResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "BUSINESS_TYPE")
    public BusinessTypeGroupResponse create(BusinessTypeGroupRequest request, String createdBy) {
        BusinessTypeGroupEntity group = new BusinessTypeGroupEntity();
        group.setId(UUID.randomUUID().toString());
        applyGroup(group, request);
        group.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        group.setStatus(Constants.STATUS_ACTIVE);
        syncTags(group, request.getTags(), createdBy);
        return BusinessTypeGroupResponse.from(repository.save(group));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "BUSINESS_TYPE", entityClass = BusinessTypeGroupEntity.class)
    public BusinessTypeGroupResponse update(String id, BusinessTypeGroupRequest request, String updatedBy) {
        BusinessTypeGroupEntity group = requireById(id);
        applyGroup(group, request);
        group.setUpdatedBy(updatedBy);
        group.setUpdatedAt(new Date());
        syncTags(group, request.getTags(), updatedBy);
        return BusinessTypeGroupResponse.from(repository.save(group));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "BUSINESS_TYPE", entityClass = BusinessTypeGroupEntity.class)
    public void delete(String id) {
        BusinessTypeGroupEntity group = requireById(id);
        group.setStatus(Constants.STATUS_DELETE);
        repository.save(group);
    }

    private void applyGroup(BusinessTypeGroupEntity group, BusinessTypeGroupRequest request) {
        group.setTitle(request.getTitle().trim());
        group.setTitleKh(request.getTitleKh());
        group.setIcon(request.getIcon());
        group.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
    }

    private void syncTags(BusinessTypeGroupEntity group, List<BusinessTypeTagRequest> incoming, String actor) {
        List<BusinessTypeTagRequest> safeIncoming = incoming != null ? incoming : new ArrayList<>();

        Map<String, BusinessTypeTagEntity> existingById = new HashMap<>();
        for (BusinessTypeTagEntity existing : new ArrayList<>(group.getTags())) {
            if (existing.getId() != null) {
                existingById.put(existing.getId(), existing);
            }
        }

        List<BusinessTypeTagEntity> nextTags = new ArrayList<>();
        for (BusinessTypeTagRequest tagReq : safeIncoming) {
            BusinessTypeTagEntity tag;
            if (StringUtils.hasText(tagReq.getId()) && existingById.containsKey(tagReq.getId())) {
                tag = existingById.remove(tagReq.getId());
                tag.setUpdatedBy(actor);
                tag.setUpdatedAt(new Date());
            } else {
                tag = new BusinessTypeTagEntity();
                tag.setId(UUID.randomUUID().toString());
                tag.setGroup(group);
                tag.setCreatedBy(StringUtils.hasText(actor) ? actor : Constants.SYSTEM);
                tag.setStatus(Constants.STATUS_ACTIVE);
            }
            tag.setLabel(tagReq.getLabel().trim());
            tag.setLabelKh(tagReq.getLabelKh());
            tag.setSortOrder(tagReq.getSortOrder() != null ? tagReq.getSortOrder() : 0);
            nextTags.add(tag);
        }

        group.getTags().clear();
        group.getTags().addAll(nextTags);
    }

    private BusinessTypeGroupEntity requireById(String id) {
        return repository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("BUSINESS_TYPE_NOT_FOUND", "Business type not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
