package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.InventoryItemStatus;
import com.cambofreelance.webbackend.constants.MovementType;
import com.cambofreelance.webbackend.constants.ProductCatalogStatus;
import com.cambofreelance.webbackend.constants.TrackingType;
import com.cambofreelance.webbackend.dto.request.InventoryItemCreateRequest;
import com.cambofreelance.webbackend.dto.request.ProductAttributeCreateDto;
import com.cambofreelance.webbackend.dto.request.ProductCreateRequest;
import com.cambofreelance.webbackend.dto.request.ProductVariantCreateDto;
import com.cambofreelance.webbackend.dto.response.InventoryItemResponse;
import com.cambofreelance.webbackend.dto.response.ProductAttributeResponse;
import com.cambofreelance.webbackend.dto.response.ProductDetailResponse;
import com.cambofreelance.webbackend.dto.response.ProductResponse;
import com.cambofreelance.webbackend.dto.response.ProductVariantResponse;
import com.cambofreelance.webbackend.entities.InventoryItemEntity;
import com.cambofreelance.webbackend.entities.InventoryMovementEntity;
import com.cambofreelance.webbackend.entities.ProductAttributeEntity;
import com.cambofreelance.webbackend.entities.ProductEntity;
import com.cambofreelance.webbackend.entities.ProductVariantEntity;
import com.cambofreelance.webbackend.entities.VariantAttributeEntity;
import com.cambofreelance.webbackend.entities.WarehouseEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.InventoryItemRepository;
import com.cambofreelance.webbackend.repository.InventoryMovementRepository;
import com.cambofreelance.webbackend.repository.ProductAttributeRepository;
import com.cambofreelance.webbackend.repository.ProductRepository;
import com.cambofreelance.webbackend.repository.ProductVariantRepository;
import com.cambofreelance.webbackend.repository.VariantAttributeRepository;
import com.cambofreelance.webbackend.repository.WarehouseRepository;
import com.cambofreelance.webbackend.services.ProductService;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final VariantAttributeRepository variantAttributeRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    @Transactional
    @Auditable(action = "CREATE", module = "INVENTORY_PRODUCT")
    public ProductResponse create(ProductCreateRequest request, String userId) {
        String actor = StringUtils.hasText(userId) ? userId : Constants.SYSTEM;

        assertNoDuplicateProduct(request.getSku(), request.getBarcode(), null);

        ProductEntity entity = new ProductEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setProductCode(generateProductCode());
        applyProductFields(entity, request);
        entity.setCreatedBy(actor);
        entity.setCreatedAt(new Date());
        entity.setStatus(Constants.STATUS_ACTIVE);
        productRepository.save(entity);

        // 1. Save additional attributes
        if (request.getAdditionalAttributes() != null && !request.getAdditionalAttributes().isEmpty()) {
            for (ProductAttributeCreateDto attrDto : request.getAdditionalAttributes()) {
                if (StringUtils.hasText(attrDto.getAttributeName()) && StringUtils.hasText(attrDto.getAttributeValue())) {
                    ProductAttributeEntity attrEntity = new ProductAttributeEntity();
                    attrEntity.setId(UUID.randomUUID().toString());
                    attrEntity.setProductId(entity.getId());
                    attrEntity.setAttributeId(attrDto.getAttributeId());
                    attrEntity.setAttributeName(attrDto.getAttributeName().trim());
                    attrEntity.setAttributeValue(attrDto.getAttributeValue().trim());
                    attrEntity.setCreatedBy(actor);
                    productAttributeRepository.save(attrEntity);
                }
            }
        }

        // 2. Save variants
        Map<String, ProductVariantEntity> variantMapByName = new HashMap<>();
        if (Boolean.TRUE.equals(request.getHasVariants()) && request.getVariants() != null && !request.getVariants().isEmpty()) {
            for (ProductVariantCreateDto vDto : request.getVariants()) {
                assertNoDuplicateVariant(vDto.getSku(), vDto.getBarcode(), null);

                ProductVariantEntity vEntity = new ProductVariantEntity();
                vEntity.setId(UUID.randomUUID().toString());
                vEntity.setProductId(entity.getId());
                vEntity.setName(vDto.getName().trim());
                vEntity.setSku(vDto.getSku().trim().toUpperCase());
                vEntity.setBarcode(StringUtils.hasText(vDto.getBarcode()) ? vDto.getBarcode().trim() : null);
                vEntity.setCostPriceOverride(vDto.getCostPriceOverride());
                vEntity.setRetailPriceOverride(vDto.getRetailPriceOverride());
                vEntity.setWholesalePriceOverride(vDto.getWholesalePriceOverride());
                vEntity.setVipPriceOverride(vDto.getVipPriceOverride());
                vEntity.setImageUrl(vDto.getImageUrl());
                vEntity.setIsDefault(Boolean.TRUE.equals(vDto.getIsDefault()));
                vEntity.setVariantStatus("ACTIVE");
                vEntity.setCreatedBy(actor);
                productVariantRepository.save(vEntity);
                variantMapByName.put(vEntity.getName(), vEntity);

                // Variant Attributes
                if (vDto.getAttributes() != null) {
                    for (Map.Entry<String, String> entry : vDto.getAttributes().entrySet()) {
                        VariantAttributeEntity va = new VariantAttributeEntity();
                        va.setId(UUID.randomUUID().toString());
                        va.setVariantId(vEntity.getId());
                        va.setAttributeName(entry.getKey().trim());
                        va.setAttributeValue(entry.getValue().trim());
                        va.setCreatedBy(actor);
                        variantAttributeRepository.save(va);
                    }
                }
            }
        } else {
            // Implicit default single variant for uniform downstream FK references
            ProductVariantEntity defaultVariant = new ProductVariantEntity();
            defaultVariant.setId(UUID.randomUUID().toString());
            defaultVariant.setProductId(entity.getId());
            defaultVariant.setName(entity.getName());
            defaultVariant.setSku(entity.getSku());
            defaultVariant.setBarcode(entity.getBarcode());
            defaultVariant.setCostPriceOverride(entity.getCostPrice());
            defaultVariant.setRetailPriceOverride(entity.getRetailPrice());
            defaultVariant.setIsDefault(true);
            defaultVariant.setVariantStatus("ACTIVE");
            defaultVariant.setCreatedBy(actor);
            productVariantRepository.save(defaultVariant);
            variantMapByName.put(defaultVariant.getName(), defaultVariant);
        }

        // 3. Save initial serialized items (if SERIALIZED tracking)
        if (TrackingType.SERIALIZED.equals(entity.getTrackingType()) && request.getInitialItems() != null) {
            String defaultWhId = resolveDefaultWarehouseId(entity.getDefaultWarehouseId());

            for (InventoryItemCreateRequest itemDto : request.getInitialItems()) {
                if (StringUtils.hasText(itemDto.getVin()) || StringUtils.hasText(itemDto.getSerialNo())) {
                    assertNoDuplicateItem(itemDto.getVin(), itemDto.getSerialNo(), itemDto.getEngineNo(), null);

                    ProductVariantEntity targetVariant = null;
                    if (StringUtils.hasText(itemDto.getVariantId())) {
                        targetVariant = productVariantRepository.findById(itemDto.getVariantId()).orElse(null);
                    }
                    if (targetVariant == null && StringUtils.hasText(itemDto.getVariantName())) {
                        targetVariant = variantMapByName.get(itemDto.getVariantName());
                    }
                    if (targetVariant == null && !variantMapByName.isEmpty()) {
                        targetVariant = variantMapByName.values().iterator().next();
                    }

                    if (targetVariant != null) {
                        InventoryItemEntity itemEntity = new InventoryItemEntity();
                        itemEntity.setId(UUID.randomUUID().toString());
                        itemEntity.setVariantId(targetVariant.getId());
                        itemEntity.setWarehouseId(StringUtils.hasText(itemDto.getWarehouseId()) ? itemDto.getWarehouseId() : defaultWhId);
                        itemEntity.setLocationId(itemDto.getLocationId());
                        itemEntity.setVin(StringUtils.hasText(itemDto.getVin()) ? itemDto.getVin().trim().toUpperCase() : null);
                        itemEntity.setSerialNo(StringUtils.hasText(itemDto.getSerialNo()) ? itemDto.getSerialNo().trim().toUpperCase() : null);
                        itemEntity.setEngineNo(StringUtils.hasText(itemDto.getEngineNo()) ? itemDto.getEngineNo().trim().toUpperCase() : null);
                        itemEntity.setColor(itemDto.getColor());
                        itemEntity.setPurchaseCost(itemDto.getPurchaseCost() != null ? itemDto.getPurchaseCost() :
                            (entity.getCostPrice() != null ? entity.getCostPrice() : BigDecimal.ZERO));
                        itemEntity.setItemStatus(StringUtils.hasText(itemDto.getItemStatus()) ? itemDto.getItemStatus().toUpperCase() : InventoryItemStatus.AVAILABLE);
                        itemEntity.setReceivedAt(itemDto.getReceivedAt() != null ? itemDto.getReceivedAt() : new Date());
                        itemEntity.setCreatedBy(actor);
                        inventoryItemRepository.save(itemEntity);

                        // Record opening stock movement
                        InventoryMovementEntity movement = new InventoryMovementEntity();
                        movement.setId(UUID.randomUUID().toString());
                        movement.setMovementType(MovementType.OPENING);
                        movement.setVariantId(targetVariant.getId());
                        movement.setInventoryItemId(itemEntity.getId());
                        movement.setToWarehouseId(itemEntity.getWarehouseId());
                        movement.setQuantity(BigDecimal.ONE);
                        movement.setUnitCost(itemEntity.getPurchaseCost());
                        movement.setTotalCost(itemEntity.getPurchaseCost());
                        movement.setReferenceType("INITIAL");
                        movement.setReferenceId(itemEntity.getId());
                        movement.setNote("Initial serialized item setup");
                        movement.setCreatedBy(actor);
                        inventoryMovementRepository.save(movement);
                    }
                }
            }
        }

        return toProductResponse(entity);
    }

    @Override
    @Transactional
    @Auditable(action = "UPDATE", module = "INVENTORY_PRODUCT")
    public ProductResponse update(String productId, ProductCreateRequest request, String userId) {
        ProductEntity entity = findProductOrThrow(productId);
        String actor = StringUtils.hasText(userId) ? userId : Constants.SYSTEM;

        assertNoDuplicateProduct(request.getSku(), request.getBarcode(), productId);
        applyProductFields(entity, request);
        entity.setUpdatedBy(actor);
        entity.setUpdatedAt(new Date());
        productRepository.save(entity);

        // Update additional attributes
        productAttributeRepository.deleteByProductId(productId);
        if (request.getAdditionalAttributes() != null) {
            for (ProductAttributeCreateDto attrDto : request.getAdditionalAttributes()) {
                if (StringUtils.hasText(attrDto.getAttributeName()) && StringUtils.hasText(attrDto.getAttributeValue())) {
                    ProductAttributeEntity attrEntity = new ProductAttributeEntity();
                    attrEntity.setId(UUID.randomUUID().toString());
                    attrEntity.setProductId(entity.getId());
                    attrEntity.setAttributeId(attrDto.getAttributeId());
                    attrEntity.setAttributeName(attrDto.getAttributeName().trim());
                    attrEntity.setAttributeValue(attrDto.getAttributeValue().trim());
                    attrEntity.setCreatedBy(actor);
                    productAttributeRepository.save(attrEntity);
                }
            }
        }

        return toProductResponse(entity);
    }

    @Override
    public ProductResponse getById(String productId) {
        return toProductResponse(findProductOrThrow(productId));
    }

    @Override
    public ProductDetailResponse getDetail(String productId) {
        ProductEntity entity = findProductOrThrow(productId);
        ProductResponse baseResponse = toProductResponse(entity);

        List<ProductVariantResponse> variants = getVariants(productId);
        List<ProductAttributeResponse> attrs = productAttributeRepository.findByProductId(productId)
            .stream().map(ProductAttributeResponse::from).toList();

        List<InventoryItemResponse> recentItems = new ArrayList<>();
        if (TrackingType.SERIALIZED.equals(entity.getTrackingType()) && !variants.isEmpty()) {
            for (ProductVariantResponse v : variants) {
                List<InventoryItemEntity> items = inventoryItemRepository.findByVariantId(v.getId());
                for (InventoryItemEntity item : items) {
                    recentItems.add(InventoryItemResponse.from(item, v.getName(), entity.getName(), null, null));
                }
            }
        }

        return ProductDetailResponse.from(entity, baseResponse, variants, attrs, recentItems);
    }

    @Override
    public Page<ProductResponse> search(
        String search,
        String categoryId,
        String productType,
        String trackingType,
        String catalogStatus,
        int page,
        int size
    ) {
        Specification<ProductEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("status"), Constants.STATUS_ACTIVE));

            if (StringUtils.hasText(categoryId)) {
                predicates.add(cb.equal(root.get("categoryId"), categoryId.trim()));
            }
            if (StringUtils.hasText(productType)) {
                predicates.add(cb.equal(root.get("productType"), productType.trim().toUpperCase()));
            }
            if (StringUtils.hasText(trackingType)) {
                predicates.add(cb.equal(root.get("trackingType"), trackingType.trim().toUpperCase()));
            }
            if (StringUtils.hasText(catalogStatus)) {
                predicates.add(cb.equal(root.get("catalogStatus"), catalogStatus.trim().toUpperCase()));
            }
            if (StringUtils.hasText(search)) {
                String like = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("name")), like),
                    cb.like(cb.lower(root.get("sku")), like),
                    cb.like(cb.lower(root.get("barcode")), like),
                    cb.like(cb.lower(root.get("brand")), like),
                    cb.like(cb.lower(root.get("model")), like)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        return productRepository.findAll(spec, PageRequest.of(page, size, sort))
            .map(this::toProductResponse);
    }

    @Override
    public List<ProductVariantResponse> getVariants(String productId) {
        List<ProductVariantEntity> variants = productVariantRepository.findByProductId(productId);
        List<ProductVariantResponse> results = new ArrayList<>();

        for (ProductVariantEntity v : variants) {
            Map<String, String> attrs = new HashMap<>();
            variantAttributeRepository.findByVariantId(v.getId())
                .forEach(a -> attrs.put(a.getAttributeName(), a.getAttributeValue()));

            long inStock = inventoryItemRepository.countByVariantIdAndItemStatus(v.getId(), InventoryItemStatus.AVAILABLE);
            results.add(ProductVariantResponse.from(v, attrs, inStock));
        }
        return results;
    }

    @Override
    @Transactional
    public void delete(String productId, String userId) {
        ProductEntity entity = findProductOrThrow(productId);
        entity.setStatus(Constants.STATUS_DELETE);
        entity.setCatalogStatus(ProductCatalogStatus.INACTIVE);
        entity.setUpdatedBy(StringUtils.hasText(userId) ? userId : Constants.SYSTEM);
        entity.setUpdatedAt(new Date());
        productRepository.save(entity);
    }

    @Override
    public String generateSku(String categoryId, String brand, String model) {
        Map<String, String> prefixMap = Map.of(
            "motorcycle", "MC",
            "car", "CAR",
            "truck", "TRK",
            "parts", "SP",
            "tyres", "TY",
            "lubricants", "LUB",
            "accessories", "ACC",
            "phones", "PH",
            "electronics", "EL"
        );
        String catPrefix = prefixMap.getOrDefault(categoryId != null ? categoryId.toLowerCase() : "", "GEN");
        String brandPart = StringUtils.hasText(brand) ? brand.replaceAll("[^A-Za-z]", "").toUpperCase() : "GEN";
        if (brandPart.length() > 3) brandPart = brandPart.substring(0, 3);
        String modelPart = StringUtils.hasText(model) ? model.replaceAll("[^A-Za-z0-9]", "").toUpperCase() : "MOD";
        if (modelPart.length() > 4) modelPart = modelPart.substring(0, 4);

        int rand = (int) (1000 + Math.random() * 8999);
        return String.format("%s-%s-%s-%04d", catPrefix, brandPart, modelPart, rand);
    }

    // ──────────────────────── Private Helpers ────────────────────────
    private ProductEntity findProductOrThrow(String id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new AppException("PRODUCT_NOT_FOUND", "Product not found: " + id));
    }

    private ProductResponse toProductResponse(ProductEntity e) {
        List<ProductVariantEntity> variants = productVariantRepository.findByProductId(e.getId());
        long variantCount = variants.size();
        long totalStock = 0;
        long availableStock = 0;

        if (TrackingType.SERIALIZED.equals(e.getTrackingType())) {
            for (ProductVariantEntity v : variants) {
                totalStock += inventoryItemRepository.countByVariantIdAndItemStatus(v.getId(), InventoryItemStatus.AVAILABLE)
                    + inventoryItemRepository.countByVariantIdAndItemStatus(v.getId(), InventoryItemStatus.RESERVED)
                    + inventoryItemRepository.countByVariantIdAndItemStatus(v.getId(), InventoryItemStatus.LOAN_PENDING);
                availableStock += inventoryItemRepository.countByVariantIdAndItemStatus(v.getId(), InventoryItemStatus.AVAILABLE);
            }
        }

        return ProductResponse.from(e, variantCount, totalStock, availableStock);
    }

    private void applyProductFields(ProductEntity e, ProductCreateRequest req) {
        e.setName(req.getName().trim());
        e.setSku(req.getSku().trim().toUpperCase());
        e.setBarcode(StringUtils.hasText(req.getBarcode()) ? req.getBarcode().trim() : null);
        e.setProductType(req.getProductType().trim().toUpperCase());
        e.setCategoryId(req.getCategoryId().trim().toLowerCase());
        e.setBrand(req.getBrand());
        e.setModel(req.getModel());
        e.setModelYear(req.getModelYear());
        e.setUnit(req.getUnit());
        e.setPreferredSupplier(req.getPreferredSupplier());
        e.setDescription(req.getDescription());
        e.setImageUrl(req.getImageUrl());
        e.setTrackingType(req.getTrackingType().trim().toUpperCase());
        e.setCatalogStatus(StringUtils.hasText(req.getCatalogStatus()) ? req.getCatalogStatus().trim().toUpperCase() : ProductCatalogStatus.ACTIVE);
        e.setHasVariants(Boolean.TRUE.equals(req.getHasVariants()));

        // Pricing
        e.setCurrency(StringUtils.hasText(req.getCurrency()) ? req.getCurrency() : "USD");
        e.setCostPrice(req.getCostPrice());
        e.setRetailPrice(req.getRetailPrice());
        e.setWholesalePrice(req.getWholesalePrice());
        e.setVipPrice(req.getVipPrice());
        e.setDiscountValue(req.getDiscountValue());
        e.setDiscountType(req.getDiscountType());
        e.setTaxRate(req.getTaxRate());

        // Inventory thresholds
        e.setReorderLevel(req.getReorderLevel());
        e.setMinStock(req.getMinStock());
        e.setMaxStock(req.getMaxStock());
        e.setDefaultWarehouseId(req.getDefaultWarehouseId());
        e.setDefaultLocation(req.getDefaultLocation());

        // Packaging
        e.setCaseName(req.getCaseName());
        e.setCaseQty(req.getCaseQty());
        e.setCaseBarcode(req.getCaseBarcode());
        e.setBoxName(req.getBoxName());
        e.setBoxQty(req.getBoxQty());
        e.setBoxBarcode(req.getBoxBarcode());

        // Vehicle specifications
        e.setEngineCc(req.getEngineCc());
        e.setFuelType(req.getFuelType());
        e.setTransmission(req.getTransmission());
        e.setVehicleCondition(req.getVehicleCondition());
        e.setWarrantyPeriod(req.getWarrantyPeriod());
    }

    private void assertNoDuplicateProduct(String sku, String barcode, String excludeId) {
        if (StringUtils.hasText(sku)) {
            boolean exists = (excludeId == null)
                ? productRepository.existsBySku(sku.trim().toUpperCase())
                : productRepository.existsBySkuAndIdNot(sku.trim().toUpperCase(), excludeId);
            if (exists) throw new AppException("DUPLICATE_PRODUCT_SKU", "Product SKU already in use: " + sku);
        }
        if (StringUtils.hasText(barcode)) {
            boolean exists = (excludeId == null)
                ? productRepository.existsByBarcode(barcode.trim())
                : productRepository.existsByBarcodeAndIdNot(barcode.trim(), excludeId);
            if (exists) throw new AppException("DUPLICATE_PRODUCT_BARCODE", "Product barcode already in use: " + barcode);
        }
    }

    private void assertNoDuplicateVariant(String sku, String barcode, String excludeId) {
        if (StringUtils.hasText(sku)) {
            boolean exists = (excludeId == null)
                ? productVariantRepository.existsBySku(sku.trim().toUpperCase())
                : productVariantRepository.existsBySkuAndIdNot(sku.trim().toUpperCase(), excludeId);
            if (exists) throw new AppException("DUPLICATE_VARIANT_SKU", "Variant SKU already in use: " + sku);
        }
        if (StringUtils.hasText(barcode)) {
            boolean exists = (excludeId == null)
                ? productVariantRepository.existsByBarcode(barcode.trim())
                : productVariantRepository.existsByBarcodeAndIdNot(barcode.trim(), excludeId);
            if (exists) throw new AppException("DUPLICATE_VARIANT_BARCODE", "Variant barcode already in use: " + barcode);
        }
    }

    private void assertNoDuplicateItem(String vin, String serial, String engine, String excludeId) {
        if (StringUtils.hasText(vin)) {
            boolean exists = (excludeId == null)
                ? inventoryItemRepository.existsByVin(vin.trim().toUpperCase())
                : inventoryItemRepository.existsByVinAndIdNot(vin.trim().toUpperCase(), excludeId);
            if (exists) throw new AppException("DUPLICATE_VIN", "VIN already exists in stock: " + vin);
        }
        if (StringUtils.hasText(serial)) {
            boolean exists = (excludeId == null)
                ? inventoryItemRepository.existsBySerialNo(serial.trim().toUpperCase())
                : inventoryItemRepository.existsBySerialNoAndIdNot(serial.trim().toUpperCase(), excludeId);
            if (exists) throw new AppException("DUPLICATE_SERIAL_NUMBER", "Serial number already in stock: " + serial);
        }
        if (StringUtils.hasText(engine)) {
            boolean exists = (excludeId == null)
                ? inventoryItemRepository.existsByEngineNo(engine.trim().toUpperCase())
                : inventoryItemRepository.existsByEngineNoAndIdNot(engine.trim().toUpperCase(), excludeId);
            if (exists) throw new AppException("DUPLICATE_ENGINE_NUMBER", "Engine number already in stock: " + engine);
        }
    }

    private String generateProductCode() {
        try {
            long seq = productRepository.nextProductSequence();
            return String.format("PRD-%06d", seq);
        } catch (Exception ex) {
            return "PRD-" + System.currentTimeMillis();
        }
    }

    private String resolveDefaultWarehouseId(String preferredWhId) {
        if (StringUtils.hasText(preferredWhId)) return preferredWhId;
        Optional<WarehouseEntity> defaultWh = warehouseRepository.findByIsDefaultTrue();
        if (defaultWh.isPresent()) return defaultWh.get().getId();
        List<WarehouseEntity> all = warehouseRepository.findAll();
        return all.isEmpty() ? "wh-main" : all.get(0).getId();
    }
}
