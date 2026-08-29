package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.constants.ProductCatalogStatus;
import com.cambofreelance.webbackend.constants.ProductType;
import com.cambofreelance.webbackend.constants.TrackingType;
import com.cambofreelance.webbackend.dto.request.ProductCreateRequest;
import com.cambofreelance.webbackend.dto.request.ProductVariantCreateDto;
import com.cambofreelance.webbackend.dto.response.ProductResponse;
import com.cambofreelance.webbackend.entities.ProductEntity;
import com.cambofreelance.webbackend.entities.ProductVariantEntity;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.cambofreelance.webbackend.repository.InventoryItemRepository;
import com.cambofreelance.webbackend.repository.InventoryMovementRepository;
import com.cambofreelance.webbackend.repository.ProductAttributeRepository;
import com.cambofreelance.webbackend.repository.ProductRepository;
import com.cambofreelance.webbackend.repository.ProductVariantRepository;
import com.cambofreelance.webbackend.repository.VariantAttributeRepository;
import com.cambofreelance.webbackend.repository.WarehouseRepository;
import com.cambofreelance.webbackend.services.impl.ProductServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductVariantRepository productVariantRepository;
    @Mock
    private ProductAttributeRepository productAttributeRepository;
    @Mock
    private VariantAttributeRepository variantAttributeRepository;
    @Mock
    private InventoryItemRepository inventoryItemRepository;
    @Mock
    private InventoryMovementRepository inventoryMovementRepository;
    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductCreateRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleRequest = new ProductCreateRequest();
        sampleRequest.setName("Honda Dream 125");
        sampleRequest.setSku("MC-HON-DREA-0001");
        sampleRequest.setBarcode("8850123456789");
        sampleRequest.setProductType(ProductType.VEHICLE);
        sampleRequest.setCategoryId("motorcycle");
        sampleRequest.setBrand("Honda");
        sampleRequest.setModel("Dream 125");
        sampleRequest.setModelYear(2026);
        sampleRequest.setUnit("Unit");
        sampleRequest.setTrackingType(TrackingType.SERIALIZED);
        sampleRequest.setCatalogStatus(ProductCatalogStatus.ACTIVE);
        sampleRequest.setCostPrice(BigDecimal.valueOf(1200.00));
        sampleRequest.setRetailPrice(BigDecimal.valueOf(2100.00));
        sampleRequest.setCurrency("USD");
    }

    @Test
    @DisplayName("Should successfully create product with implicit default variant")
    void testCreateProductSingleVariant() {
        when(productRepository.existsBySku("MC-HON-DREA-0001")).thenReturn(false);
        when(productRepository.existsByBarcode("8850123456789")).thenReturn(false);
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(productVariantRepository.save(any(ProductVariantEntity.class))).thenAnswer(i -> i.getArgument(0));

        ProductResponse response = productService.create(sampleRequest, "usr-001");

        assertNotNull(response);
        assertEquals("Honda Dream 125", response.getName());
        assertEquals("MC-HON-DREA-0001", response.getSku());
        assertEquals(BigDecimal.valueOf(900.00), response.getGrossMargin()); // 2100 - 1200
        verify(productRepository, times(1)).save(any(ProductEntity.class));
        verify(productVariantRepository, times(1)).save(any(ProductVariantEntity.class));
    }

    @Test
    @DisplayName("Should successfully create product with multi-variants")
    void testCreateProductWithVariants() {
        sampleRequest.setHasVariants(true);

        ProductVariantCreateDto v1 = new ProductVariantCreateDto();
        v1.setName("Black / 125cc");
        v1.setSku("MC-HON-DREA-0001-BLK-125");
        v1.setAttributes(Map.of("Colour", "Black", "Engine CC", "125"));

        ProductVariantCreateDto v2 = new ProductVariantCreateDto();
        v2.setName("Red / 125cc");
        v2.setSku("MC-HON-DREA-0001-RED-125");
        v2.setAttributes(Map.of("Colour", "Red", "Engine CC", "125"));

        sampleRequest.setVariants(List.of(v1, v2));

        when(productRepository.existsBySku(anyString())).thenReturn(false);
        when(productVariantRepository.existsBySku(anyString())).thenReturn(false);
        when(productRepository.save(any(ProductEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(productVariantRepository.save(any(ProductVariantEntity.class))).thenAnswer(i -> i.getArgument(0));

        ProductResponse response = productService.create(sampleRequest, "usr-001");

        assertNotNull(response);
        verify(productVariantRepository, times(2)).save(any(ProductVariantEntity.class));
    }

    @Test
    @DisplayName("Should throw AppException on duplicate SKU")
    void testDuplicateSkuThrowsException() {
        when(productRepository.existsBySku("MC-HON-DREA-0001")).thenReturn(true);

        assertThrows(AppException.class, () -> productService.create(sampleRequest, "usr-001"));
        verify(productRepository, never()).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Should generate valid SKU structure")
    void testGenerateSku() {
        String sku = productService.generateSku("motorcycle", "Honda", "Dream");
        assertNotNull(sku);
        assertTrue(sku.startsWith("MC-HON-DREA-"));
    }
}
