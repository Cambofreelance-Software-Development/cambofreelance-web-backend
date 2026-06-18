package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.LoanApplicationCreateRequest;
import com.cambofreelance.webbackend.dto.request.LoanApplicationStatusActionRequest;
import com.cambofreelance.webbackend.dto.request.LoanApplicationUpdateRequest;
import com.cambofreelance.webbackend.dto.request.LoanDocumentUploadRequest;
import com.cambofreelance.webbackend.dto.response.LoanApplicationDocumentResponse;
import com.cambofreelance.webbackend.dto.response.LoanApplicationResponse;
import com.cambofreelance.webbackend.dto.response.RepaymentScheduleResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface LoanApplicationService {

    LoanApplicationResponse create(LoanApplicationCreateRequest request, String createdByUserId);

    LoanApplicationResponse update(String loanApplicationId, LoanApplicationUpdateRequest request, String updatedByUserId);

    LoanApplicationResponse getById(String loanApplicationId);

    Page<LoanApplicationResponse> search(String search, String applicationStatus, String customerId, int page, int size);

    LoanApplicationResponse submit(String loanApplicationId, String userId);

    LoanApplicationResponse approve(String loanApplicationId, LoanApplicationStatusActionRequest request, String userId);

    LoanApplicationResponse reject(String loanApplicationId, LoanApplicationStatusActionRequest request, String userId);

    // ── Supporting documents ──────────────────────────────────────────────────

    LoanApplicationDocumentResponse uploadDocument(String loanApplicationId, LoanDocumentUploadRequest request, String userId);

    List<LoanApplicationDocumentResponse> listDocuments(String loanApplicationId);

    void deleteDocument(String loanApplicationId, String documentId, String userId);

    // ── Repayment schedule ────────────────────────────────────────────────────

    RepaymentScheduleResponse calculateRepaymentSchedule(String loanApplicationId);
}
