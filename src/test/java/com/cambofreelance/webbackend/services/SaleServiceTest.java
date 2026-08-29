package com.cambofreelance.webbackend.services;

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
import com.cambofreelance.webbackend.dto.response.SaleResponse;
import com.cambofreelance.webbackend.entities.CustomerEntity;
import com.cambofreelance.webbackend.entities.FinancingApplicationEntity;
import com.cambofreelance.webbackend.entities.InventoryItemEntity;
import com.cambofreelance.webbackend.entities.InventoryMovementEntity;
import com.cambofreelance.webbackend.entities.InventoryReservationEntity;
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
import com.cambofreelance.webbackend.services.impl.SaleServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;
    @Mock
    private SaleItemRepository saleItemRepository;
    @Mock
    private InventoryItemRepository inventoryItemRepository;
    @Mock
    private InventoryReservationRepository inventoryReservationRepository;
    @Mock
    private FinancingApplicationRepository financingApplicationRepository;
    @Mock
    private BankRepository bankRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ProductVariantRepository productVariantRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private InventoryMovementRepository inventoryMovementRepository;
    @Mock
    private InventoryDocumentRepository inventoryDocumentRepository;

    @InjectMocks
    private SaleServiceImpl saleService;

    private CustomerEntity customer;
    private ProductVariantEntity variant;
    private InventoryItemEntity inventoryItem;

    @BeforeEach
    void setUp() {
        customer = new CustomerEntity();
        customer.setId("cust-001");
        customer.setFirstName("Sok");
        customer.setLastName("Dara");
        customer.setPhoneNumber("+85512345678");

        variant = new ProductVariantEntity();
        variant.setId("var-001");
        variant.setName("Honda Dream 125 - Black");
        variant.setSku("MC-HON-DREA-0001-BLK");
        variant.setRetailPriceOverride(BigDecimal.valueOf(2200.00));
        variant.setCostPriceOverride(BigDecimal.valueOf(1400.00));

        inventoryItem = new InventoryItemEntity();
        inventoryItem.setId("item-001");
        inventoryItem.setVariantId("var-001");
        inventoryItem.setWarehouseId("wh-main");
        inventoryItem.setVin("1HGBH41JXMN109186");
        inventoryItem.setEngineNo("HONDA-ENG-8821");
        inventoryItem.setItemStatus(InventoryItemStatus.AVAILABLE);
        inventoryItem.setPurchaseCost(BigDecimal.valueOf(1400.00));
    }

    @Test
    @DisplayName("Should successfully create a bank financed sale and submit financing")
    void testCreateBankFinancedSale() {
        when(customerRepository.findById("cust-001")).thenReturn(Optional.of(customer));
        when(productVariantRepository.findById("var-001")).thenReturn(Optional.of(variant));
        when(inventoryItemRepository.findById("item-001")).thenReturn(Optional.of(inventoryItem));
        when(saleRepository.save(any(SaleEntity.class))).thenAnswer(i -> i.getArgument(0));

        SaleCreateRequest req = new SaleCreateRequest();
        req.setCustomerId("cust-001");
        req.setPaymentType("BANK_LOAN");
        req.setDownPayment(BigDecimal.valueOf(500.00));

        SaleItemRequest itemReq = new SaleItemRequest();
        itemReq.setVariantId("var-001");
        itemReq.setInventoryItemId("item-001");
        itemReq.setUnitPrice(BigDecimal.valueOf(2200.00));
        req.setItems(List.of(itemReq));

        FinancingSubmitRequest finReq = new FinancingSubmitRequest();
        finReq.setBankId("bank-aba");
        finReq.setRequestedAmount(BigDecimal.valueOf(1700.00));
        finReq.setTermMonths(24);
        req.setFinancing(finReq);

        SaleResponse resp = saleService.create(req, "salesperson-001");

        assertNotNull(resp);
        assertEquals(SaleStatus.LOAN_PENDING, resp.getSaleStatus());
        assertEquals("BANK_LOAN", resp.getPaymentType());
        assertEquals(BigDecimal.valueOf(2200.00), resp.getTotalAmount());
        assertEquals(BigDecimal.valueOf(1700.00), resp.getFinancedAmount());
        verify(saleRepository, atLeastOnce()).save(any(SaleEntity.class));
        verify(financingApplicationRepository, times(1)).save(any(FinancingApplicationEntity.class));
    }

    @Test
    @DisplayName("Should approve financing, mark unit SOLD and record SALE ledger movement")
    void testApproveFinancing() {
        SaleEntity sale = new SaleEntity();
        sale.setId("sale-001");
        sale.setSaleNo("SALE-000001");
        sale.setCustomerId("cust-001");
        sale.setSaleStatus(SaleStatus.LOAN_PENDING);

        FinancingApplicationEntity fin = new FinancingApplicationEntity();
        fin.setId("fin-001");
        fin.setApplicationNo("FIN-000001");
        fin.setSaleId("sale-001");
        fin.setCustomerId("cust-001");
        fin.setStatus(FinancingStatus.UNDER_REVIEW);
        fin.setRequestedAmount(BigDecimal.valueOf(1700.00));

        SaleItemEntity saleItem = new SaleItemEntity();
        saleItem.setId("sale-item-001");
        saleItem.setSaleId("sale-001");
        saleItem.setVariantId("var-001");
        saleItem.setInventoryItemId("item-001");

        when(financingApplicationRepository.findById("fin-001")).thenReturn(Optional.of(fin));
        when(saleRepository.findById("sale-001")).thenReturn(Optional.of(sale));
        when(saleItemRepository.findBySaleId("sale-001")).thenReturn(List.of(saleItem));
        when(inventoryItemRepository.findById("item-001")).thenReturn(Optional.of(inventoryItem));
        when(customerRepository.findById("cust-001")).thenReturn(Optional.of(customer));

        FinancingApprovalRequest approvalReq = new FinancingApprovalRequest();
        approvalReq.setApprovedAmount(BigDecimal.valueOf(1700.00));
        approvalReq.setExternalReference("ABA-LOAN-98765");

        FinancingApplicationResponse resp = saleService.approveFinancing("fin-001", approvalReq, "approver-001");

        assertNotNull(resp);
        assertEquals(FinancingStatus.APPROVED, resp.getStatus());
        assertEquals("ABA-LOAN-98765", resp.getExternalReference());
        assertEquals(SaleStatus.CONFIRMED, sale.getSaleStatus());
        assertEquals(InventoryItemStatus.SOLD, inventoryItem.getItemStatus());

        // Verify stock movement was recorded
        verify(inventoryMovementRepository, times(1)).save(argThat(m ->
            MovementType.SALE.equals(m.getMovementType()) &&
            "item-001".equals(m.getInventoryItemId())
        ));
    }

    @Test
    @DisplayName("Should reject financing, cancel sale, and release vehicle back to AVAILABLE")
    void testRejectFinancing() {
        SaleEntity sale = new SaleEntity();
        sale.setId("sale-001");
        sale.setSaleNo("SALE-000001");
        sale.setCustomerId("cust-001");
        sale.setSaleStatus(SaleStatus.LOAN_PENDING);

        FinancingApplicationEntity fin = new FinancingApplicationEntity();
        fin.setId("fin-001");
        fin.setApplicationNo("FIN-000001");
        fin.setSaleId("sale-001");
        fin.setCustomerId("cust-001");
        fin.setStatus(FinancingStatus.UNDER_REVIEW);

        SaleItemEntity saleItem = new SaleItemEntity();
        saleItem.setId("sale-item-001");
        saleItem.setSaleId("sale-001");
        saleItem.setVariantId("var-001");
        saleItem.setInventoryItemId("item-001");

        inventoryItem.setItemStatus(InventoryItemStatus.LOAN_PENDING);

        InventoryReservationEntity activeRes = new InventoryReservationEntity();
        activeRes.setId("res-001");
        activeRes.setInventoryItemId("item-001");
        activeRes.setStatus(ReservationStatus.ACTIVE);

        when(financingApplicationRepository.findById("fin-001")).thenReturn(Optional.of(fin));
        when(saleRepository.findById("sale-001")).thenReturn(Optional.of(sale));
        when(saleItemRepository.findBySaleId("sale-001")).thenReturn(List.of(saleItem));
        when(inventoryItemRepository.findById("item-001")).thenReturn(Optional.of(inventoryItem));
        when(inventoryReservationRepository.findByInventoryItemIdAndStatus("item-001", ReservationStatus.ACTIVE))
            .thenReturn(Optional.of(activeRes));
        when(customerRepository.findById("cust-001")).thenReturn(Optional.of(customer));

        FinancingApplicationResponse resp = saleService.rejectFinancing("fin-001", "Credit score insufficient", "bank-officer");

        assertNotNull(resp);
        assertEquals(FinancingStatus.REJECTED, resp.getStatus());
        assertEquals("Credit score insufficient", resp.getRejectionReason());
        assertEquals(SaleStatus.CANCELLED, sale.getSaleStatus());
        assertEquals(InventoryItemStatus.AVAILABLE, inventoryItem.getItemStatus());
        assertEquals(ReservationStatus.RELEASED, activeRes.getStatus());
    }

    @Test
    @DisplayName("Should prevent double booking if unit is already reserved")
    void testDoubleBookingPrevention() {
        SaleEntity sale = new SaleEntity();
        sale.setId("sale-002");
        sale.setCustomerId("cust-001");

        when(saleRepository.findById("sale-002")).thenReturn(Optional.of(sale));
        when(inventoryItemRepository.findById("item-001")).thenReturn(Optional.of(inventoryItem));
        when(inventoryReservationRepository.existsByInventoryItemIdAndStatus("item-001", ReservationStatus.ACTIVE))
            .thenReturn(true);

        AppException ex = assertThrows(AppException.class, () ->
            saleService.reserveUnit("sale-002", "item-001", "salesperson-002")
        );

        assertEquals("ITEM_ALREADY_RESERVED", ex.getErrorCode());
    }
}
