package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.FinancingApprovalRequest;
import com.cambofreelance.webbackend.dto.request.FinancingSubmitRequest;
import com.cambofreelance.webbackend.dto.request.SaleCreateRequest;
import com.cambofreelance.webbackend.dto.response.FinancingApplicationResponse;
import com.cambofreelance.webbackend.dto.response.SaleDetailResponse;
import com.cambofreelance.webbackend.dto.response.SaleResponse;
import org.springframework.data.domain.Page;

public interface SaleService {

    SaleResponse create(SaleCreateRequest request, String userId);

    SaleResponse getById(String saleId);

    SaleDetailResponse getDetail(String saleId);

    Page<SaleResponse> search(
        String search,
        String saleStatus,
        String paymentType,
        String customerId,
        int page,
        int size
    );

    SaleResponse reserveUnit(String saleId, String inventoryItemId, String userId);

    FinancingApplicationResponse submitFinancing(String saleId, FinancingSubmitRequest request, String userId);

    FinancingApplicationResponse approveFinancing(String financingId, FinancingApprovalRequest request, String userId);

    FinancingApplicationResponse rejectFinancing(String financingId, String rejectionReason, String userId);

    SaleResponse confirmSale(String saleId, String userId);

    SaleResponse deliverUnit(String saleId, String userId);

    SaleResponse cancelSale(String saleId, String userId);
}
