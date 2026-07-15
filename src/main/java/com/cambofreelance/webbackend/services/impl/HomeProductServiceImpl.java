package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.HomeProductRequest;
import com.cambofreelance.webbackend.dto.response.HomeProductResponse;
import com.cambofreelance.webbackend.entities.HomeProductEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.HomeProductRepository;
import com.cambofreelance.webbackend.services.HomeProductService;
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
public class HomeProductServiceImpl implements HomeProductService {

    private final HomeProductRepository repository;

    @Override
    public List<HomeProductResponse> listAll() {
        return repository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(HomeProductResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public Page<HomeProductResponse> search(String search, int page, int size) {
        String searchFilter = StringUtils.hasText(search)
            ? "%" + search.trim().toLowerCase() + "%"
            : null;
        return repository.searchActive(searchFilter, PageRequest.of(page, size))
            .map(HomeProductResponse::from);
    }

    @Override
    public HomeProductResponse getById(String id) {
        return HomeProductResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "HOME_PRODUCT")
    public HomeProductResponse create(HomeProductRequest request, String createdBy) {
        HomeProductEntity entity = new HomeProductEntity();
        entity.setId(UUID.randomUUID().toString());
        apply(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        return HomeProductResponse.from(repository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "HOME_PRODUCT", entityClass = HomeProductEntity.class)
    public HomeProductResponse update(String id, HomeProductRequest request, String updatedBy) {
        HomeProductEntity entity = requireById(id);
        apply(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : entity.getSortOrder());
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        return HomeProductResponse.from(repository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "HOME_PRODUCT", entityClass = HomeProductEntity.class)
    public void delete(String id) {
        HomeProductEntity entity = requireById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        repository.save(entity);
    }

    private void apply(HomeProductEntity entity, HomeProductRequest request) {
        entity.setName(request.getName().trim());
        entity.setNameKh(request.getNameKh());
        entity.setDescription(request.getDescription());
        entity.setDescriptionKh(request.getDescriptionKh());
        entity.setIcon(request.getIcon());
        entity.setIconBg(request.getIconBg());
        entity.setHref(request.getHref());
    }

    private HomeProductEntity requireById(String id) {
        return repository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("HOME_PRODUCT_NOT_FOUND", "Home product not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
