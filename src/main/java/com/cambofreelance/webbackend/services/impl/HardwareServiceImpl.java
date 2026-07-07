package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.HardwareRequest;
import com.cambofreelance.webbackend.dto.response.HardwareResponse;
import com.cambofreelance.webbackend.entities.CategoryHardwareEntity;
import com.cambofreelance.webbackend.entities.HardwareEntity;
import com.cambofreelance.webbackend.entities.MediaFileEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.CategoryHardwareRepository;
import com.cambofreelance.webbackend.repository.HardwareRepository;
import com.cambofreelance.webbackend.repository.MediaRepository;
import com.cambofreelance.webbackend.services.HardwareService;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class HardwareServiceImpl implements HardwareService {

    private final HardwareRepository          hardwareRepository;
    private final MediaRepository             mediaRepository;
    private final CategoryHardwareRepository  categoryHardwareRepository;

    @Override
    public List<HardwareResponse> listAll() {
        return hardwareRepository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(HardwareResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public Page<HardwareResponse> search(String search, int page, int size) {
        String searchFilter = StringUtils.hasText(search)
            ? "%" + search.trim().toLowerCase() + "%"
            : null;
        return hardwareRepository.searchActive(searchFilter, PageRequest.of(page, size))
            .map(HardwareResponse::from);
    }

    @Override
    public HardwareResponse getById(String id) {
        return HardwareResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "HARDWARE")
    public HardwareResponse create(HardwareRequest request, String createdBy) {
        HardwareEntity entity = new HardwareEntity();
        entity.setId(UUID.randomUUID().toString());
        applyRequest(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        return HardwareResponse.from(hardwareRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "HARDWARE", entityClass = HardwareEntity.class)
    public HardwareResponse update(String id, HardwareRequest request, String updatedBy) {
        HardwareEntity entity = requireById(id);
        applyRequest(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : entity.getSortOrder());
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        return HardwareResponse.from(hardwareRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "HARDWARE", entityClass = HardwareEntity.class)
    public void delete(String id) {
        HardwareEntity entity = requireById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        hardwareRepository.save(entity);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void applyRequest(HardwareEntity entity, HardwareRequest request) {
        entity.setName(request.getName().trim());
        entity.setNameKh(request.getNameKh());
        entity.setBrand(request.getBrand());
        entity.setDescription(request.getDescription());
        entity.setDescriptionKh(request.getDescriptionKh());
        entity.setConnectivity(request.getConnectivity());
        entity.setPlatform(request.getPlatform());
        entity.setIcon(request.getIcon());
        entity.setLink(request.getLink());
        resolveImage(entity, request.getImageId());
        resolveCategory(entity, request.getCategoryId());
    }

    private void resolveCategory(HardwareEntity entity, String categoryId) {
        if (StringUtils.hasText(categoryId)) {
            CategoryHardwareEntity category = categoryHardwareRepository.findById(categoryId)
                .filter(c -> !Constants.STATUS_DELETE.equals(c.getStatus()))
                .orElse(null);
            entity.setCategory(category);
        } else {
            entity.setCategory(null);
        }
    }

    private void resolveImage(HardwareEntity entity, String imageId) {
        if (StringUtils.hasText(imageId)) {
            MediaFileEntity image = mediaRepository.findById(imageId)
                .filter(m -> Constants.STATUS_ACTIVE.equals(m.getStatus()))
                .orElse(null);
            entity.setImage(image);
        } else {
            entity.setImage(null);
        }
    }

    private HardwareEntity requireById(String id) {
        return hardwareRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("HARDWARE_NOT_FOUND", "Hardware not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
