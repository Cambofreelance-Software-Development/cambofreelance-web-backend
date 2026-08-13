package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.dto.request.ProductRequest;
import com.cambofreelance.webbackend.dto.response.ProductResponse;
import com.cambofreelance.webbackend.entities.CategoryProductEntity;
import com.cambofreelance.webbackend.entities.MediaFileEntity;
import com.cambofreelance.webbackend.entities.ProductEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.CategoryProductRepository;
import com.cambofreelance.webbackend.repository.MediaRepository;
import com.cambofreelance.webbackend.repository.ProductRepository;
import com.cambofreelance.webbackend.services.ProductService;
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
public class ProductServiceImpl implements ProductService {

    private final ProductRepository         productRepository;
    private final MediaRepository           mediaRepository;
    private final CategoryProductRepository categoryProductRepository;

    @Override
    public List<ProductResponse> listAll() {
        return productRepository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE)
            .stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> listPublic(String categoryId) {
        List<ProductEntity> entities = StringUtils.hasText(categoryId)
            ? productRepository.findByStatusAndCategory_IdOrderBySortOrderAsc(Constants.STATUS_ACTIVE, categoryId)
            : productRepository.findByStatusOrderBySortOrderAsc(Constants.STATUS_ACTIVE);
        return entities.stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public Page<ProductResponse> search(String search, int page, int size) {
        String searchFilter = StringUtils.hasText(search)
            ? "%" + search.trim().toLowerCase() + "%"
            : null;
        return productRepository.searchActive(searchFilter, PageRequest.of(page, size))
            .map(ProductResponse::from);
    }

    @Override
    public ProductResponse getById(String id) {
        return ProductResponse.from(requireById(id));
    }

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "PRODUCT")
    public ProductResponse create(ProductRequest request, String createdBy) {
        ProductEntity entity = new ProductEntity();
        entity.setId(UUID.randomUUID().toString());
        applyRequest(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        entity.setCreatedBy(StringUtils.hasText(createdBy) ? createdBy : Constants.SYSTEM);
        entity.setStatus(Constants.STATUS_ACTIVE);
        return ProductResponse.from(productRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "PRODUCT", entityClass = ProductEntity.class)
    public ProductResponse update(String id, ProductRequest request, String updatedBy) {
        ProductEntity entity = requireById(id);
        applyRequest(entity, request);
        entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : entity.getSortOrder());
        entity.setUpdatedBy(updatedBy);
        entity.setUpdatedAt(new Date());
        return ProductResponse.from(productRepository.save(entity));
    }

    @Override
    @Transactional
    @Auditable(action = "DELETE", module = "PRODUCT", entityClass = ProductEntity.class)
    public void delete(String id) {
        ProductEntity entity = requireById(id);
        entity.setStatus(Constants.STATUS_DELETE);
        productRepository.save(entity);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void applyRequest(ProductEntity entity, ProductRequest request) {
        entity.setName(request.getName().trim());
        entity.setNameKh(request.getNameKh());
        entity.setDescription(request.getDescription());
        entity.setDescriptionKh(request.getDescriptionKh());
        entity.setPrice(request.getPrice());
        entity.setIcon(request.getIcon());
        entity.setLink(request.getLink());
        resolveImage(entity, request.getImageId());
        resolveCategory(entity, request.getCategoryId());
    }

    private void resolveCategory(ProductEntity entity, String categoryId) {
        if (StringUtils.hasText(categoryId)) {
            CategoryProductEntity category = categoryProductRepository.findById(categoryId)
                .filter(c -> !Constants.STATUS_DELETE.equals(c.getStatus()))
                .orElse(null);
            entity.setCategory(category);
        } else {
            entity.setCategory(null);
        }
    }

    private void resolveImage(ProductEntity entity, String imageId) {
        if (StringUtils.hasText(imageId)) {
            MediaFileEntity image = mediaRepository.findById(imageId)
                .filter(m -> Constants.STATUS_ACTIVE.equals(m.getStatus()))
                .orElse(null);
            entity.setImage(image);
        } else {
            entity.setImage(null);
        }
    }

    private ProductEntity requireById(String id) {
        return productRepository.findById(id)
            .filter(e -> !Constants.STATUS_DELETE.equals(e.getStatus()))
            .orElseThrow(() -> {
                AppException ex = new AppException("PRODUCT_NOT_FOUND", "Product not found: " + id);
                ex.setHttpStatus(HttpStatus.NOT_FOUND);
                return ex;
            });
    }
}
