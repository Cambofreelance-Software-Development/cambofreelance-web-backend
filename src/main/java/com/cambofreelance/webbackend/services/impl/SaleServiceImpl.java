package com.cambofreelance.webbackend.services.impl;

import com.cambofreelance.webbackend.audit.Auditable;
import com.cambofreelance.webbackend.constants.Constants;
import com.cambofreelance.webbackend.constants.FinancingStatus;
import com.cambofreelance.webbackend.constants.InventoryItemStatus;
import com.cambofreelance.webbackend.constants.MovementType;
import com.cambofreelance.webbackend.constants.ReservationStatus;
import com.cambofreelance.webbackend.constants.SaleStatus;
import com.cambofreelance.webbackend.dto.request.FinancingApprovalRequest;
import com.cambofreelance.webbackend.dto.request.FinancingSubmitRequest;
import com.cambofreelance.webbackend.dto.request.SaleCreateRequest;
import com.cambofreelance.webbackend.dto.request.SaleItemRequest;
import com.cambofreelance.webbackend.dto.response.FinancingApplicationResponse;
import com.cambofreelance.webbackend.dto.response.InventoryDocumentResponse;
import com.cambofreelance.webbackend.dto.response.SaleDetailResponse;
import com.cambofreelance.webbackend.dto.response.SaleResponse;
import com.cambofreelance.webbackend.entities.BankEntity;
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
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.BankRepository;
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
import com.cambofreelance.webbackend.services.SaleService;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryReservationRepository inventoryReservationRepository;
    private final FinancingApplicationRepository financingApplicationRepository;
    private final BankRepository bankRepository;
    private final CustomerRepository customerRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final InventoryDocumentRepository inventoryDocumentRepository;

    @Override
    @Transactional
    @Auditable(action = "CREATE_SALE", module = "INVENTORY_SALE")
    public SaleResponse create(SaleCreateRequest request, String userId) {
        String actor = StringUtils.hasText(userId) ? userId : Constants.SYSTEM;

        CustomerEntity customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new AppException("CUSTOMER_NOT_FOUND", "Customer not found: " + request.getCustomerId()));

        SaleEntity sale = new SaleEntity();
        sale.setId(UUID.randomUUID().toString());
        sale.setSaleNo(generateSaleNo());
        sale.setCustomerId(customer.getId());
        sale.setSalespersonId(StringUtils.hasText(request.getSalespersonId()) ? request.getSalespersonId() : actor);
        sale.setWarehouseId(request.getWarehouseId());
        sale.setPaymentType(StringUtils.hasText(request.getPaymentType()) ? request.getPaymentType().toUpperCase() : "CASH");
        sale.setCurrency(StringUtils.hasText(request.getCurrency()) ? request.getCurrency() : "USD");
        sale.setNotes(request.getNotes());
        sale.setCreatedBy(actor);
        sale.setCreatedAt(new Date());
        sale.setStatus(Constants.STATUS_ACTIVE);

        BigDecimal subtotal = BigDecimal.ZERO;
        List<SaleItemEntity> itemEntities = new ArrayList<>();

        for (SaleItemRequest itemReq : request.getItems()) {
            ProductVariantEntity variant = productVariantRepository.findById(itemReq.getVariantId())
                .orElseThrow(() -> new AppException("VARIANT_NOT_FOUND", "Variant not found: " + itemReq.getVariantId()));

            SaleItemEntity itemEntity = new SaleItemEntity();
            itemEntity.setId(UUID.randomUUID().toString());
            itemEntity.setSaleId(sale.getId());
            itemEntity.setVariantId(variant.getId());
            itemEntity.setQuantity(itemReq.getQuantity() != null ? itemReq.getQuantity() : BigDecimal.ONE);
            itemEntity.setUnitPrice(itemReq.getUnitPrice() != null ? itemReq.getUnitPrice() :
                (variant.getRetailPriceOverride() != null ? variant.getRetailPriceOverride() : BigDecimal.ZERO));
            itemEntity.setDiscountAmount(itemReq.getDiscountAmount() != null ? itemReq.getDiscountAmount() : BigDecimal.ZERO);
            itemEntity.setCostPrice(variant.getCostPriceOverride() != null ? variant.getCostPriceOverride() : BigDecimal.ZERO);

            BigDecimal lineTotal = itemEntity.getUnitPrice().multiply(itemEntity.getQuantity())
                .subtract(itemEntity.getDiscountAmount());
            itemEntity.setTotalAmount(lineTotal.max(BigDecimal.ZERO));
            subtotal = subtotal.add(itemEntity.getTotalAmount());

            // If a physical serialized unit (VIN) is specified
            if (StringUtils.hasText(itemReq.getInventoryItemId())) {
                InventoryItemEntity invItem = inventoryItemRepository.findById(itemReq.getInventoryItemId())
                    .orElseThrow(() -> new AppException("INVENTORY_ITEM_NOT_FOUND", "Item not found: " + itemReq.getInventoryItemId()));

                if (!InventoryItemStatus.AVAILABLE.equals(invItem.getItemStatus()) && !InventoryItemStatus.RESERVED.equals(invItem.getItemStatus())) {
                    throw new AppException("ITEM_NOT_AVAILABLE", "Serialized unit is not available: " + invItem.getVin());
                }

                itemEntity.setInventoryItemId(invItem.getId());
                itemEntity.setCostPrice(invItem.getPurchaseCost());
            }

            itemEntity.setCreatedBy(actor);
            itemEntities.add(itemEntity);
        }

        sale.setSubtotal(subtotal);
        sale.setDiscountAmount(request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO);
        sale.setTaxAmount(request.getTaxAmount() != null ? request.getTaxAmount() : BigDecimal.ZERO);
        sale.setTotalAmount(subtotal.subtract(sale.getDiscountAmount()).add(sale.getTaxAmount()).max(BigDecimal.ZERO));
        sale.setDownPayment(request.getDownPayment() != null ? request.getDownPayment() : BigDecimal.ZERO);
        sale.setFinancedAmount(sale.getTotalAmount().subtract(sale.getDownPayment()).max(BigDecimal.ZERO));

        // Set initial status based on payment type
        if ("BANK_LOAN".equals(sale.getPaymentType())) {
            sale.setSaleStatus(SaleStatus.LOAN_PENDING);
        } else {
            sale.setSaleStatus(SaleStatus.DRAFT);
        }

        saleRepository.save(sale);
        saleItemRepository.saveAll(itemEntities);

        // If BANK_LOAN with financing requested, create initial financing application and reserve serialized units
        if ("BANK_LOAN".equals(sale.getPaymentType()) && request.getFinancing() != null) {
            submitFinancingInternal(sale, request.getFinancing(), actor);
        }

        return toSaleResponse(sale);
    }

    @Override
    public SaleResponse getById(String saleId) {
        return toSaleResponse(findSaleOrThrow(saleId));
    }

    @Override
    public SaleDetailResponse getDetail(String saleId) {
        SaleEntity sale = findSaleOrThrow(saleId);
        SaleResponse baseResp = toSaleResponse(sale);

        List<SaleItemEntity> items = saleItemRepository.findBySaleId(saleId);
        List<SaleDetailResponse.SaleItemResponse> itemResponses = new ArrayList<>();

        for (SaleItemEntity item : items) {
            String variantName = null;
            String productName = null;
            Optional<ProductVariantEntity> v = productVariantRepository.findById(item.getVariantId());
            if (v.isPresent()) {
                variantName = v.get().getName();
                Optional<ProductEntity> p = productRepository.findById(v.get().getProductId());
                if (p.isPresent()) productName = p.get().getName();
            }

            String vin = null;
            String engineNo = null;
            String color = null;
            if (StringUtils.hasText(item.getInventoryItemId())) {
                Optional<InventoryItemEntity> inv = inventoryItemRepository.findById(item.getInventoryItemId());
                if (inv.isPresent()) {
                    vin = inv.get().getVin();
                    engineNo = inv.get().getEngineNo();
                    color = inv.get().getColor();
                }
            }

            itemResponses.add(SaleDetailResponse.SaleItemResponse.builder()
                .id(item.getId())
                .variantId(item.getVariantId())
                .variantName(variantName)
                .productName(productName)
                .inventoryItemId(item.getInventoryItemId())
                .vin(vin)
                .engineNo(engineNo)
                .color(color)
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .discountAmount(item.getDiscountAmount())
                .costPrice(item.getCostPrice())
                .totalAmount(item.getTotalAmount())
                .build());
        }

        FinancingApplicationResponse finResp = null;
        Optional<FinancingApplicationEntity> fin = financingApplicationRepository.findBySaleId(saleId);
        if (fin.isPresent()) {
            finResp = toFinancingResponse(fin.get(), sale.getSaleNo());
        }

        List<InventoryDocumentResponse> docs = inventoryDocumentRepository.findByOwnerTypeAndOwnerIdOrderByCreatedAtDesc("SALE", saleId)
            .stream().map(InventoryDocumentResponse::from).toList();

        return SaleDetailResponse.builder()
            .sale(baseResp)
            .items(itemResponses)
            .financing(finResp)
            .documents(docs)
            .build();
    }

    @Override
    public Page<SaleResponse> search(
        String search,
        String saleStatus,
        String paymentType,
        String customerId,
        int page,
        int size
    ) {
        Specification<SaleEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("status"), Constants.STATUS_ACTIVE));

            if (StringUtils.hasText(saleStatus)) {
                predicates.add(cb.equal(root.get("saleStatus"), saleStatus.trim().toUpperCase()));
            }
            if (StringUtils.hasText(paymentType)) {
                predicates.add(cb.equal(root.get("paymentType"), paymentType.trim().toUpperCase()));
            }
            if (StringUtils.hasText(customerId)) {
                predicates.add(cb.equal(root.get("customerId"), customerId.trim()));
            }
            if (StringUtils.hasText(search)) {
                String like = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("saleNo")), like));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        return saleRepository.findAll(spec, PageRequest.of(page, size, sort))
            .map(this::toSaleResponse);
    }

    @Override
    @Transactional
    @Auditable(action = "RESERVE_UNIT", module = "INVENTORY_RESERVATION")
    public SaleResponse reserveUnit(String saleId, String inventoryItemId, String userId) {
        String actor = StringUtils.hasText(userId) ? userId : Constants.SYSTEM;
        SaleEntity sale = findSaleOrThrow(saleId);
        InventoryItemEntity item = inventoryItemRepository.findById(inventoryItemId)
            .orElseThrow(() -> new AppException("INVENTORY_ITEM_NOT_FOUND", "Item not found: " + inventoryItemId));

        if (inventoryReservationRepository.existsByInventoryItemIdAndStatus(inventoryItemId, ReservationStatus.ACTIVE)) {
            throw new AppException("ITEM_ALREADY_RESERVED", "Unit is already reserved by another salesperson: " + item.getVin());
        }

        if (!InventoryItemStatus.AVAILABLE.equals(item.getItemStatus())) {
            throw new AppException("ITEM_NOT_AVAILABLE", "Item is currently not available: " + item.getItemStatus());
        }

        // Create reservation
        InventoryReservationEntity reservation = new InventoryReservationEntity();
        reservation.setId(UUID.randomUUID().toString());
        reservation.setInventoryItemId(item.getId());
        reservation.setSaleId(sale.getId());
        reservation.setCustomerId(sale.getCustomerId());
        reservation.setReservedBy(actor);
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setReservedAt(new Date());
        reservation.setCreatedBy(actor);
        inventoryReservationRepository.save(reservation);

        // Update item & sale status
        item.setItemStatus(InventoryItemStatus.RESERVED);
        item.setReservedBy(actor);
        item.setReservedUntil(new Date(System.currentTimeMillis() + 48 * 3600 * 1000L)); // 48h hold
        item.setUpdatedAt(new Date());
        inventoryItemRepository.save(item);

        sale.setSaleStatus(SaleStatus.RESERVED);
        sale.setUpdatedAt(new Date());
        saleRepository.save(sale);

        return toSaleResponse(sale);
    }

    @Override
    @Transactional
    @Auditable(action = "SUBMIT_FINANCING", module = "INVENTORY_FINANCING")
    public FinancingApplicationResponse submitFinancing(String saleId, FinancingSubmitRequest request, String userId) {
        String actor = StringUtils.hasText(userId) ? userId : Constants.SYSTEM;
        SaleEntity sale = findSaleOrThrow(saleId);
        return submitFinancingInternal(sale, request, actor);
    }

    @Override
    @Transactional
    @Auditable(action = "APPROVE_FINANCING", module = "INVENTORY_FINANCING")
    public FinancingApplicationResponse approveFinancing(String financingId, FinancingApprovalRequest request, String userId) {
        String actor = StringUtils.hasText(userId) ? userId : Constants.SYSTEM;
        FinancingApplicationEntity fin = financingApplicationRepository.findById(financingId)
            .orElseThrow(() -> new AppException("FINANCING_NOT_FOUND", "Financing application not found: " + financingId));

        if (FinancingStatus.APPROVED.equals(fin.getStatus())) {
            // Idempotent safety check
            return toFinancingResponse(fin, null);
        }

        fin.setStatus(FinancingStatus.APPROVED);
        if (request != null && request.getApprovedAmount() != null) fin.setApprovedAmount(request.getApprovedAmount());
        if (request != null && StringUtils.hasText(request.getExternalReference())) fin.setExternalReference(request.getExternalReference());
        fin.setApprovedAt(new Date());
        fin.setUpdatedBy(actor);
        fin.setUpdatedAt(new Date());
        financingApplicationRepository.save(fin);

        // Transition Sale -> CONFIRMED, Serialized Units -> SOLD
        SaleEntity sale = findSaleOrThrow(fin.getSaleId());
        sale.setSaleStatus(SaleStatus.CONFIRMED);
        sale.setContractDate(new Date());
        sale.setUpdatedAt(new Date());
        saleRepository.save(sale);

        // Convert reservations & decrease stock via sale movement
        List<SaleItemEntity> items = saleItemRepository.findBySaleId(sale.getId());
        for (SaleItemEntity item : items) {
            if (StringUtils.hasText(item.getInventoryItemId())) {
                inventoryItemRepository.findById(item.getInventoryItemId()).ifPresent(invItem -> {
                    invItem.setItemStatus(InventoryItemStatus.SOLD);
                    invItem.setUpdatedAt(new Date());
                    inventoryItemRepository.save(invItem);

                    // Convert active reservation
                    inventoryReservationRepository.findByInventoryItemIdAndStatus(invItem.getId(), ReservationStatus.ACTIVE)
                        .ifPresent(res -> {
                            res.setStatus(ReservationStatus.CONVERTED);
                            res.setUpdatedAt(new Date());
                            inventoryReservationRepository.save(res);
                        });

                    // Create sale ledger movement
                    InventoryMovementEntity movement = new InventoryMovementEntity();
                    movement.setId(UUID.randomUUID().toString());
                    movement.setMovementType(MovementType.SALE);
                    movement.setVariantId(item.getVariantId());
                    movement.setInventoryItemId(invItem.getId());
                    movement.setFromWarehouseId(invItem.getWarehouseId());
                    movement.setQuantity(BigDecimal.ONE.negate());
                    movement.setUnitCost(invItem.getPurchaseCost());
                    movement.setTotalCost(invItem.getPurchaseCost());
                    movement.setReferenceType("SALE");
                    movement.setReferenceId(sale.getSaleNo());
                    movement.setNote("Sale confirmed via financing approval: " + fin.getApplicationNo());
                    movement.setCreatedBy(actor);
                    inventoryMovementRepository.save(movement);
                });
            }
        }

        return toFinancingResponse(fin, sale.getSaleNo());
    }

    @Override
    @Transactional
    @Auditable(action = "REJECT_FINANCING", module = "INVENTORY_FINANCING")
    public FinancingApplicationResponse rejectFinancing(String financingId, String rejectionReason, String userId) {
        String actor = StringUtils.hasText(userId) ? userId : Constants.SYSTEM;
        FinancingApplicationEntity fin = financingApplicationRepository.findById(financingId)
            .orElseThrow(() -> new AppException("FINANCING_NOT_FOUND", "Financing application not found: " + financingId));

        fin.setStatus(FinancingStatus.REJECTED);
        fin.setRejectionReason(rejectionReason);
        fin.setRejectedAt(new Date());
        fin.setUpdatedBy(actor);
        fin.setUpdatedAt(new Date());
        financingApplicationRepository.save(fin);

        SaleEntity sale = findSaleOrThrow(fin.getSaleId());
        sale.setSaleStatus(SaleStatus.CANCELLED);
        sale.setUpdatedAt(new Date());
        saleRepository.save(sale);

        // Release all reserved units back to AVAILABLE
        List<SaleItemEntity> items = saleItemRepository.findBySaleId(sale.getId());
        for (SaleItemEntity item : items) {
            if (StringUtils.hasText(item.getInventoryItemId())) {
                inventoryItemRepository.findById(item.getInventoryItemId()).ifPresent(invItem -> {
                    invItem.setItemStatus(InventoryItemStatus.AVAILABLE);
                    invItem.setReservedBy(null);
                    invItem.setReservedUntil(null);
                    invItem.setUpdatedAt(new Date());
                    inventoryItemRepository.save(invItem);

                    inventoryReservationRepository.findByInventoryItemIdAndStatus(invItem.getId(), ReservationStatus.ACTIVE)
                        .ifPresent(res -> {
                            res.setStatus(ReservationStatus.RELEASED);
                            res.setReleasedAt(new Date());
                            res.setUpdatedAt(new Date());
                            inventoryReservationRepository.save(res);
                        });
                });
            }
        }

        return toFinancingResponse(fin, sale.getSaleNo());
    }

    @Override
    @Transactional
    @Auditable(action = "CONFIRM_SALE", module = "INVENTORY_SALE")
    public SaleResponse confirmSale(String saleId, String userId) {
        String actor = StringUtils.hasText(userId) ? userId : Constants.SYSTEM;
        SaleEntity sale = findSaleOrThrow(saleId);

        sale.setSaleStatus(SaleStatus.CONFIRMED);
        sale.setContractDate(new Date());
        sale.setUpdatedAt(new Date());
        saleRepository.save(sale);

        List<SaleItemEntity> items = saleItemRepository.findBySaleId(sale.getId());
        for (SaleItemEntity item : items) {
            if (StringUtils.hasText(item.getInventoryItemId())) {
                inventoryItemRepository.findById(item.getInventoryItemId()).ifPresent(invItem -> {
                    invItem.setItemStatus(InventoryItemStatus.SOLD);
                    invItem.setUpdatedAt(new Date());
                    inventoryItemRepository.save(invItem);

                    inventoryReservationRepository.findByInventoryItemIdAndStatus(invItem.getId(), ReservationStatus.ACTIVE)
                        .ifPresent(res -> {
                            res.setStatus(ReservationStatus.CONVERTED);
                            res.setUpdatedAt(new Date());
                            inventoryReservationRepository.save(res);
                        });

                    InventoryMovementEntity movement = new InventoryMovementEntity();
                    movement.setId(UUID.randomUUID().toString());
                    movement.setMovementType(MovementType.SALE);
                    movement.setVariantId(item.getVariantId());
                    movement.setInventoryItemId(invItem.getId());
                    movement.setFromWarehouseId(invItem.getWarehouseId());
                    movement.setQuantity(BigDecimal.ONE.negate());
                    movement.setUnitCost(invItem.getPurchaseCost());
                    movement.setTotalCost(invItem.getPurchaseCost());
                    movement.setReferenceType("SALE");
                    movement.setReferenceId(sale.getSaleNo());
                    movement.setNote("Direct cash sale confirmed");
                    movement.setCreatedBy(actor);
                    inventoryMovementRepository.save(movement);
                });
            }
        }

        return toSaleResponse(sale);
    }

    @Override
    @Transactional
    @Auditable(action = "DELIVER_UNIT", module = "INVENTORY_SALE")
    public SaleResponse deliverUnit(String saleId, String userId) {
        String actor = StringUtils.hasText(userId) ? userId : Constants.SYSTEM;
        SaleEntity sale = findSaleOrThrow(saleId);

        sale.setSaleStatus(SaleStatus.DELIVERED);
        sale.setDeliveryDate(new Date());
        sale.setUpdatedAt(new Date());
        saleRepository.save(sale);

        List<SaleItemEntity> items = saleItemRepository.findBySaleId(sale.getId());
        for (SaleItemEntity item : items) {
            if (StringUtils.hasText(item.getInventoryItemId())) {
                inventoryItemRepository.findById(item.getInventoryItemId()).ifPresent(invItem -> {
                    invItem.setItemStatus(InventoryItemStatus.DELIVERED);
                    invItem.setUpdatedAt(new Date());
                    inventoryItemRepository.save(invItem);
                });
            }
        }

        return toSaleResponse(sale);
    }

    @Override
    @Transactional
    @Auditable(action = "CANCEL_SALE", module = "INVENTORY_SALE")
    public SaleResponse cancelSale(String saleId, String userId) {
        String actor = StringUtils.hasText(userId) ? userId : Constants.SYSTEM;
        SaleEntity sale = findSaleOrThrow(saleId);

        sale.setSaleStatus(SaleStatus.CANCELLED);
        sale.setUpdatedAt(new Date());
        saleRepository.save(sale);

        List<SaleItemEntity> items = saleItemRepository.findBySaleId(sale.getId());
        for (SaleItemEntity item : items) {
            if (StringUtils.hasText(item.getInventoryItemId())) {
                inventoryItemRepository.findById(item.getInventoryItemId()).ifPresent(invItem -> {
                    invItem.setItemStatus(InventoryItemStatus.AVAILABLE);
                    invItem.setReservedBy(null);
                    invItem.setReservedUntil(null);
                    invItem.setUpdatedAt(new Date());
                    inventoryItemRepository.save(invItem);

                    inventoryReservationRepository.findByInventoryItemIdAndStatus(invItem.getId(), ReservationStatus.ACTIVE)
                        .ifPresent(res -> {
                            res.setStatus(ReservationStatus.RELEASED);
                            res.setReleasedAt(new Date());
                            res.setUpdatedAt(new Date());
                            inventoryReservationRepository.save(res);
                        });
                });
            }
        }

        return toSaleResponse(sale);
    }

    // ──────────────────────── Private Helpers ────────────────────────
    private FinancingApplicationResponse submitFinancingInternal(SaleEntity sale, FinancingSubmitRequest req, String actor) {
        FinancingApplicationEntity fin = new FinancingApplicationEntity();
        fin.setId(UUID.randomUUID().toString());
        fin.setApplicationNo(generateFinancingAppNo());
        fin.setSaleId(sale.getId());
        fin.setCustomerId(sale.getCustomerId());
        fin.setBankId(req.getBankId());
        fin.setRequestedAmount(req.getRequestedAmount() != null ? req.getRequestedAmount() : sale.getFinancedAmount());
        fin.setTermMonths(req.getTermMonths());
        fin.setInterestRate(req.getInterestRate());
        fin.setMonthlyInstallment(req.getMonthlyInstallment());
        fin.setStatus(FinancingStatus.UNDER_REVIEW);
        fin.setExternalReference(req.getExternalReference());
        fin.setGuarantorCustomerId(req.getGuarantorCustomerId());
        fin.setSubmittedAt(new Date());
        fin.setCreatedBy(actor);
        fin.setCreatedAt(new Date());
        financingApplicationRepository.save(fin);

        // Ensure units are marked LOAN_PENDING
        sale.setSaleStatus(SaleStatus.LOAN_PENDING);
        sale.setUpdatedAt(new Date());
        saleRepository.save(sale);

        List<SaleItemEntity> items = saleItemRepository.findBySaleId(sale.getId());
        for (SaleItemEntity item : items) {
            if (StringUtils.hasText(item.getInventoryItemId())) {
                inventoryItemRepository.findById(item.getInventoryItemId()).ifPresent(invItem -> {
                    invItem.setItemStatus(InventoryItemStatus.LOAN_PENDING);
                    invItem.setUpdatedAt(new Date());
                    inventoryItemRepository.save(invItem);
                });
            }
        }

        return toFinancingResponse(fin, sale.getSaleNo());
    }

    private SaleEntity findSaleOrThrow(String id) {
        return saleRepository.findById(id)
            .orElseThrow(() -> new AppException("SALE_NOT_FOUND", "Sale not found: " + id));
    }

    private SaleResponse toSaleResponse(SaleEntity e) {
        String custName = null;
        String custPhone = null;
        Optional<CustomerEntity> cust = customerRepository.findById(e.getCustomerId());
        if (cust.isPresent()) {
            custName = cust.get().getFirstName() + " " + cust.get().getLastName();
            custPhone = cust.get().getPhoneNumber();
        }

        return SaleResponse.from(e, custName, custPhone);
    }

    private FinancingApplicationResponse toFinancingResponse(FinancingApplicationEntity e, String saleNo) {
        if (!StringUtils.hasText(saleNo)) {
            saleRepository.findById(e.getSaleId()).ifPresent(s -> {});
        }

        String custName = null;
        Optional<CustomerEntity> cust = customerRepository.findById(e.getCustomerId());
        if (cust.isPresent()) custName = cust.get().getFirstName() + " " + cust.get().getLastName();

        String bankName = null;
        String bankLogo = null;
        if (StringUtils.hasText(e.getBankId())) {
            Optional<BankEntity> b = bankRepository.findById(e.getBankId());
            if (b.isPresent()) {
                bankName = b.get().getName();
                bankLogo = b.get().getLogoUrl();
            }
        }

        String guarantorName = null;
        if (StringUtils.hasText(e.getGuarantorCustomerId())) {
            Optional<CustomerEntity> g = customerRepository.findById(e.getGuarantorCustomerId());
            if (g.isPresent()) guarantorName = g.get().getFirstName() + " " + g.get().getLastName();
        }

        return FinancingApplicationResponse.from(e, saleNo, custName, bankName, bankLogo, guarantorName);
    }

    private String generateSaleNo() {
        try {
            long seq = saleRepository.nextSaleSequence();
            return String.format("SALE-%06d", seq);
        } catch (Exception ex) {
            return "SALE-" + System.currentTimeMillis();
        }
    }

    private String generateFinancingAppNo() {
        try {
            long seq = financingApplicationRepository.nextFinancingSequence();
            return String.format("FIN-%06d", seq);
        } catch (Exception ex) {
            return "FIN-" + System.currentTimeMillis();
        }
    }
}
