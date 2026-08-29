package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.FinancingStatus;
import com.cambofreelance.webbackend.constants.InventoryItemStatus;
import com.cambofreelance.webbackend.constants.MovementType;
import com.cambofreelance.webbackend.constants.SaleStatus;
import com.cambofreelance.webbackend.dto.request.InventoryItemCreateRequest;
import com.cambofreelance.webbackend.dto.response.InventoryItemResponse;
import com.cambofreelance.webbackend.dto.response.VehicleTimelineItemResponse;
import com.cambofreelance.webbackend.entities.CustomerEntity;
import com.cambofreelance.webbackend.entities.FinancingApplicationEntity;
import com.cambofreelance.webbackend.entities.InventoryDocumentEntity;
import com.cambofreelance.webbackend.entities.InventoryItemEntity;
import com.cambofreelance.webbackend.entities.InventoryMovementEntity;
import com.cambofreelance.webbackend.entities.InventoryReservationEntity;
import com.cambofreelance.webbackend.entities.ProductEntity;
import com.cambofreelance.webbackend.entities.ProductVariantEntity;
import com.cambofreelance.webbackend.entities.SaleEntity;
import com.cambofreelance.webbackend.entities.SaleItemEntity;
import com.cambofreelance.webbackend.entities.WarehouseEntity;
import com.cambofreelance.webbackend.entities.WarehouseLocationEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.CustomerRepository;
import com.cambofreelance.webbackend.repository.FinancingApplicationRepository;
import com.cambofreelance.webbackend.repository.InventoryDocumentRepository;
import com.cambofreelance.webbackend.repository.InventoryItemRepository;
import com.cambofreelance.webbackend.repository.InventoryMovementRepository;
import com.cambofreelance.webbackend.repository.InventoryReservationRepository;
import com.cambofreelance.webbackend.repository.ProductRepository;
import com.cambofreelance.webbackend.repository.ProductVariantRepository;
import com.cambofreelance.webbackend.repository.SaleItemRepository;
import com.cambofreelance.webbackend.repository.SaleRepository;
import com.cambofreelance.webbackend.repository.WarehouseLocationRepository;
import com.cambofreelance.webbackend.repository.WarehouseRepository;
import com.cambofreelance.webbackend.services.InventoryItemService;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseLocationRepository warehouseLocationRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final InventoryReservationRepository inventoryReservationRepository;
    private final SaleItemRepository saleItemRepository;
    private final SaleRepository saleRepository;
    private final FinancingApplicationRepository financingApplicationRepository;
    private final CustomerRepository customerRepository;
    private final InventoryDocumentRepository inventoryDocumentRepository;

    @Override
    @Transactional
    @Auditable(action = "RECEIVE", module = "INVENTORY_ITEM")
    public InventoryItemResponse create(InventoryItemCreateRequest request, String userId) {
        String actor = StringUtils.hasText(userId) ? userId : Constants.SYSTEM;

        ProductVariantEntity variant = productVariantRepository.findById(request.getVariantId())
            .orElseThrow(() -> new AppException("VARIANT_NOT_FOUND", "Variant not found: " + request.getVariantId()));

        assertNoDuplicate(request.getVin(), request.getSerialNo(), request.getEngineNo(), null);

        String warehouseId = StringUtils.hasText(request.getWarehouseId())
            ? request.getWarehouseId()
            : resolveDefaultWarehouseId();

        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setVariantId(variant.getId());
        entity.setWarehouseId(warehouseId);
        entity.setLocationId(request.getLocationId());
        entity.setVin(StringUtils.hasText(request.getVin()) ? request.getVin().trim().toUpperCase() : null);
        entity.setSerialNo(StringUtils.hasText(request.getSerialNo()) ? request.getSerialNo().trim().toUpperCase() : null);
        entity.setEngineNo(StringUtils.hasText(request.getEngineNo()) ? request.getEngineNo().trim().toUpperCase() : null);
        entity.setColor(request.getColor());
        entity.setPurchaseCost(request.getPurchaseCost() != null ? request.getPurchaseCost() : BigDecimal.ZERO);
        entity.setItemStatus(StringUtils.hasText(request.getItemStatus()) ? request.getItemStatus().toUpperCase() : InventoryItemStatus.AVAILABLE);
        entity.setSupplierName(request.getSupplierName());
        entity.setPoReference(request.getPoReference());
        entity.setReceivedAt(request.getReceivedAt() != null ? request.getReceivedAt() : new Date());
        entity.setCreatedBy(actor);
        entity.setCreatedAt(new Date());
        entity.setStatus(Constants.STATUS_ACTIVE);
        inventoryItemRepository.save(entity);

        // Record stock ledger movement
        InventoryMovementEntity movement = new InventoryMovementEntity();
        movement.setId(UUID.randomUUID().toString());
        movement.setMovementType(MovementType.PURCHASE);
        movement.setVariantId(variant.getId());
        movement.setInventoryItemId(entity.getId());
        movement.setToWarehouseId(warehouseId);
        movement.setQuantity(BigDecimal.ONE);
        movement.setUnitCost(entity.getPurchaseCost());
        movement.setTotalCost(entity.getPurchaseCost());
        movement.setReferenceType(StringUtils.hasText(entity.getPoReference()) ? "PO" : "INITIAL");
        movement.setReferenceId(StringUtils.hasText(entity.getPoReference()) ? entity.getPoReference() : entity.getId());
        movement.setNote("Goods receipt for serialized item: " + (entity.getVin() != null ? entity.getVin() : entity.getSerialNo()));
        movement.setCreatedBy(actor);
        inventoryMovementRepository.save(movement);

        return toResponse(entity);
    }

    @Override
    public InventoryItemResponse getById(String itemId) {
        return toResponse(findOrThrow(itemId));
    }

    @Override
    public Page<InventoryItemResponse> search(
        String search,
        String variantId,
        String warehouseId,
        String itemStatus,
        int page,
        int size
    ) {
        Specification<InventoryItemEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("status"), Constants.STATUS_ACTIVE));

            if (StringUtils.hasText(variantId)) {
                predicates.add(cb.equal(root.get("variantId"), variantId.trim()));
            }
            if (StringUtils.hasText(warehouseId)) {
                predicates.add(cb.equal(root.get("warehouseId"), warehouseId.trim()));
            }
            if (StringUtils.hasText(itemStatus)) {
                predicates.add(cb.equal(root.get("itemStatus"), itemStatus.trim().toUpperCase()));
            }
            if (StringUtils.hasText(search)) {
                String like = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                    cb.like(cb.lower(root.get("vin")), like),
                    cb.like(cb.lower(root.get("serialNo")), like),
                    cb.like(cb.lower(root.get("engineNo")), like),
                    cb.like(cb.lower(root.get("supplierName")), like)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        return inventoryItemRepository.findAll(spec, PageRequest.of(page, size, sort))
            .map(this::toResponse);
    }

    @Override
    public List<VehicleTimelineItemResponse> getTimeline(String itemId) {
        InventoryItemEntity item = findOrThrow(itemId);
        List<VehicleTimelineItemResponse> timeline = new ArrayList<>();

        // 1. Purchased / Received
        timeline.add(VehicleTimelineItemResponse.builder()
            .stage("RECEIVED")
            .title("Goods Received")
            .description(String.format("Unit received into stock (VIN: %s, Engine: %s) at warehouse.",
                item.getVin() != null ? item.getVin() : "N/A",
                item.getEngineNo() != null ? item.getEngineNo() : "N/A"))
            .status("RECEIVED")
            .referenceType("PO")
            .referenceId(item.getPoReference())
            .performedBy(item.getCreatedBy())
            .eventTime(item.getReceivedAt())
            .completed(true)
            .build());

        // 2. Available in showroom / warehouse
        timeline.add(VehicleTimelineItemResponse.builder()
            .stage("AVAILABLE")
            .title("Available for Sale")
            .description("Unit inspected and placed in available stock.")
            .status(InventoryItemStatus.AVAILABLE)
            .performedBy(item.getCreatedBy())
            .eventTime(item.getCreatedAt())
            .completed(true)
            .build());

        // 3. Reservations
        List<InventoryReservationEntity> reservations = inventoryReservationRepository.findByInventoryItemId(itemId);
        for (InventoryReservationEntity res : reservations) {
            timeline.add(VehicleTimelineItemResponse.builder()
                .stage("RESERVED")
                .title("Reserved by Salesperson")
                .description("Hold placed on unit for customer.")
                .status(res.getStatus())
                .referenceType("RESERVATION")
                .referenceId(res.getId())
                .performedBy(res.getReservedBy())
                .eventTime(res.getReservedAt())
                .completed(true)
                .build());
        }

        // 4. Sales & Financing
        Optional<SaleItemEntity> saleItemOpt = saleItemRepository.findByInventoryItemId(itemId);
        if (saleItemOpt.isPresent()) {
            SaleItemEntity saleItem = saleItemOpt.get();
            Optional<SaleEntity> saleOpt = saleRepository.findById(saleItem.getSaleId());

            if (saleOpt.isPresent()) {
                SaleEntity sale = saleOpt.get();
                CustomerEntity customer = customerRepository.findById(sale.getCustomerId()).orElse(null);
                String custName = customer != null ? customer.getFirstName() + " " + customer.getLastName() : "Customer";

                timeline.add(VehicleTimelineItemResponse.builder()
                    .stage("SALE_QUOTED")
                    .title("Customer Confirmed Sale")
                    .description(String.format("Sale contract agreed with %s for %s %s.", custName, sale.getCurrency(), sale.getTotalAmount()))
                    .status(sale.getSaleStatus())
                    .referenceType("SALE")
                    .referenceId(sale.getSaleNo())
                    .performedBy(sale.getSalespersonId())
                    .eventTime(sale.getCreatedAt())
                    .completed(true)
                    .build());

                // Financing application
                Optional<FinancingApplicationEntity> finOpt = financingApplicationRepository.findBySaleId(sale.getId());
                if (finOpt.isPresent()) {
                    FinancingApplicationEntity fin = finOpt.get();

                    timeline.add(VehicleTimelineItemResponse.builder()
                        .stage("LOAN_SUBMITTED")
                        .title("Loan Application Submitted")
                        .description(String.format("Financing submitted for %s %s.", sale.getCurrency(), fin.getRequestedAmount()))
                        .status(fin.getStatus())
                        .referenceType("FINANCING")
                        .referenceId(fin.getApplicationNo())
                        .performedBy(fin.getCreatedBy())
                        .eventTime(fin.getSubmittedAt() != null ? fin.getSubmittedAt() : fin.getCreatedAt())
                        .completed(true)
                        .build());

                    if (FinancingStatus.APPROVED.equals(fin.getStatus()) || sale.getSaleStatus().equals(SaleStatus.CONFIRMED)) {
                        timeline.add(VehicleTimelineItemResponse.builder()
                            .stage("LOAN_APPROVED")
                            .title("Loan Approved & Sale Confirmed")
                            .description(String.format("Financing approved by partner bank. External ref: %s.", fin.getExternalReference()))
                            .status(FinancingStatus.APPROVED)
                            .referenceType("FINANCING")
                            .referenceId(fin.getApplicationNo())
                            .eventTime(fin.getApprovedAt())
                            .completed(true)
                            .build());
                    } else if (FinancingStatus.REJECTED.equals(fin.getStatus())) {
                        timeline.add(VehicleTimelineItemResponse.builder()
                            .stage("LOAN_REJECTED")
                            .title("Loan Rejected by Bank")
                            .description("Reason: " + fin.getRejectionReason())
                            .status(FinancingStatus.REJECTED)
                            .referenceType("FINANCING")
                            .referenceId(fin.getApplicationNo())
                            .eventTime(fin.getRejectedAt())
                            .completed(true)
                            .build());
                    }
                }

                // Delivered
                if (SaleStatus.DELIVERED.equals(sale.getSaleStatus()) || InventoryItemStatus.DELIVERED.equals(item.getItemStatus())) {
                    timeline.add(VehicleTimelineItemResponse.builder()
                        .stage("DELIVERED")
                        .title("Vehicle Handover & Delivered")
                        .description("Vehicle signed over to customer and warranty clock started.")
                        .status(InventoryItemStatus.DELIVERED)
                        .referenceType("SALE")
                        .referenceId(sale.getSaleNo())
                        .eventTime(sale.getDeliveryDate() != null ? sale.getDeliveryDate() : new Date())
                        .completed(true)
                        .build());
                }
            }
        }

        return timeline;
    }

    // ──────────────────────── Private Helpers ────────────────────────
    private InventoryItemEntity findOrThrow(String id) {
        return inventoryItemRepository.findById(id)
            .orElseThrow(() -> new AppException("INVENTORY_ITEM_NOT_FOUND", "Inventory item not found: " + id));
    }

    private InventoryItemResponse toResponse(InventoryItemEntity e) {
        String variantName = null;
        String productName = null;
        Optional<ProductVariantEntity> v = productVariantRepository.findById(e.getVariantId());
        if (v.isPresent()) {
            variantName = v.get().getName();
            Optional<ProductEntity> p = productRepository.findById(v.get().getProductId());
            if (p.isPresent()) productName = p.get().getName();
        }

        String whName = null;
        Optional<WarehouseEntity> wh = warehouseRepository.findById(e.getWarehouseId());
        if (wh.isPresent()) whName = wh.get().getName();

        String locCode = null;
        if (StringUtils.hasText(e.getLocationId())) {
            Optional<WarehouseLocationEntity> loc = warehouseLocationRepository.findById(e.getLocationId());
            if (loc.isPresent()) locCode = loc.get().getCode();
        }

        return InventoryItemResponse.from(e, variantName, productName, whName, locCode);
    }

    private void assertNoDuplicate(String vin, String serial, String engine, String excludeId) {
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

    private String resolveDefaultWarehouseId() {
        Optional<WarehouseEntity> defaultWh = warehouseRepository.findByIsDefaultTrue();
        if (defaultWh.isPresent()) return defaultWh.get().getId();
        List<WarehouseEntity> all = warehouseRepository.findAll();
        return all.isEmpty() ? "wh-main" : all.get(0).getId();
    }
}
