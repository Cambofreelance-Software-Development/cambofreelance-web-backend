package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.LoanDisbursementContractRequest;
import com.cambofreelance.webbackend.dto.request.LoanDisbursementCreateRequest;
import com.cambofreelance.webbackend.dto.request.LoanDisbursementDisburseRequest;
import com.cambofreelance.webbackend.dto.response.LoanDisbursementResponse;
import org.springframework.data.domain.Page;

public interface LoanDisbursementService {

    /** Generate a disbursement record for an APPROVED loan (status = PENDING). */
    LoanDisbursementResponse create(LoanDisbursementCreateRequest request, String userId);

    LoanDisbursementResponse getById(String disbursementId);

    LoanDisbursementResponse getByLoanApplicationId(String loanApplicationId);

    Page<LoanDisbursementResponse> search(String search, String disbursementStatus, int page, int size);

    /** Upload the signed contract document (PDF / image). */
    LoanDisbursementResponse uploadContract(String disbursementId, LoanDisbursementContractRequest request, String userId);

    /** Mark disbursement as COMPLETED and record the actual disbursement date. */
    LoanDisbursementResponse disburse(String disbursementId, LoanDisbursementDisburseRequest request, String userId);
}
