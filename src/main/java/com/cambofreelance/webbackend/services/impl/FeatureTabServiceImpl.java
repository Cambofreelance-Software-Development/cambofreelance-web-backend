package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.FeatureTabRequest;
import com.cambofreelance.webbackend.dto.request.FeatureTabRequest.FeatureTabBulletRequest;
import com.cambofreelance.webbackend.dto.response.FeatureTabResponse;
import com.cambofreelance.webbackend.entities.FeatureTabBulletEntity;
import com.cambofreelance.webbackend.entities.FeatureTabEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.FeatureTabRepository;
import com.cambofreelance.webbackend.services.FeatureTabService;
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
public class FeatureTabServiceImpl implements FeatureTabService {

    private final FeatureTabRepository repository;

    @Override
    @Transactional
    public List<FeatureTabResponse> listAll() {
        return repository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(FeatureTabResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FeatureTabResponse getById(String id) {
        return FeatureTabResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "FEATURE_TAB")
    public FeatureTabResponse create(FeatureTabRequest request, String createdBy) {
        FeatureTabEntity tab = new FeatureTabEntity();
        tab.setId(UUID.randomUUID().toString());
        applyTab(tab, request);
        tab.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        tab.setStatus(Constants.STATUS_ACTIVE);
        syncBullets(tab, request.getBullets(), createdBy);
        return FeatureTabResponse.from(repository.save(tab));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "FEATURE_TAB", entityClass = FeatureTabEntity.class)
    public FeatureTabResponse update(String id, FeatureTabRequest request, String updatedBy) {
        FeatureTabEntity tab = requireById(id);
        applyTab(tab, request);
        tab.setUpdatedBy(updatedBy);
        tab.setUpdatedAt(new Date());
        syncBullets(tab, request.getBullets(), updatedBy);
        return FeatureTabResponse.from(repository.save(tab));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "FEATURE_TAB", entityClass = FeatureTabEntity.class)
    public void delete(String id) {
        FeatureTabEntity tab = requireById(id);
        tab.setStatus(Constants.STATUS_DELETE);
        repository.save(tab);
    }

    private void applyTab(FeatureTabEntity tab, FeatureTabRequest request) {
        tab.setTabLabel(request.getTabLabel().trim());
        tab.setTabLabelKh(request.getTabLabelKh());
        tab.setTitle(request.getTitle().trim());
        tab.setTitleKh(request.getTitleKh());
        tab.setSubtitle(request.getSubtitle());
        tab.setSubtitleKh(request.getSubtitleKh());
        tab.setCtaLabel(request.getCtaLabel());
        tab.setCtaLabelKh(request.getCtaLabelKh());
        tab.setCtaHref(request.getCtaHref());
        tab.setCtaButton(Boolean.TRUE.equals(request.getCtaButton()));
        tab.setImageUrl(request.getImageUrl());
        tab.setImageSide(StringUtils.hasText(request.getImageSide()) ? request.getImageSide() : "right");
        tab.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
    }

    private void syncBullets(FeatureTabEntity tab, List<FeatureTabBulletRequest> incoming, String actor) {
        List<FeatureTabBulletRequest> safeIncoming = incoming != null ? incoming : new ArrayList<>();

        Map<String, FeatureTabBulletEntity> existingById = new HashMap<>();
        for (FeatureTabBulletEntity existing : new ArrayList<>(tab.getBullets())) {
            if (existing.getId() != null) {
                existingById.put(existing.getId(), existing);
            }
        }

        List<FeatureTabBulletEntity> nextBullets = new ArrayList<>();
        for (FeatureTabBulletRequest bulletReq : safeIncoming) {
            FeatureTabBulletEntity bullet;
            if (StringUtils.hasText(bulletReq.getId()) && existingById.containsKey(bulletReq.getId())) {
                bullet = existingById.remove(bulletReq.getId());
                bullet.setUpdatedBy(actor);
                bullet.setUpdatedAt(new Date());
            } else {
                bullet = new FeatureTabBulletEntity();
                bullet.setId(UUID.randomUUID().toString());
                bullet.setTab(tab);
                bullet.setCreatedBy(StringUtils.hasText(actor) ? actor : Constants.SYSTEM);
                bullet.setStatus(Constants.STATUS_ACTIVE);
            }
            bullet.setIcon(StringUtils.hasText(bulletReq.getIcon()) ? bulletReq.getIcon() : "check");
            bullet.setLabel(bulletReq.getLabel().trim());
            bullet.setLabelKh(bulletReq.getLabelKh());
            bullet.setSubLabel(bulletReq.getSubLabel());
            bullet.setSubLabelKh(bulletReq.getSubLabelKh());
            bullet.setSortOrder(bulletReq.getSortOrder() != null ? bulletReq.getSortOrder() : 0);
            nextBullets.add(bullet);
        }

        tab.getBullets().clear();
        tab.getBullets().addAll(nextBullets);
    }

    private FeatureTabEntity requireById(String id) {
        return repository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("FEATURE_TAB_NOT_FOUND", "Feature tab not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
