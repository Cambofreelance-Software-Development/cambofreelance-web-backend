package com.cambofreelance.webbackend.services;

import com.cambofreelance.webbackend.dto.request.PaymentCreateRequest;
import com.cambofreelance.webbackend.dto.request.PaymentReverseRequest;
import com.cambofreelance.webbackend.dto.response.PaymentResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface PaymentService {

    PaymentResponse receivePayment(PaymentCreateRequest request, String userId);

    PaymentResponse getById(String id);

    Page<PaymentResponse> search(String loanApplicationId, String paymentStatus, String paymentType, String currency, int page, int size);

    List<PaymentResponse> getByLoan(String loanApplicationId);

    PaymentResponse reversePayment(String id, PaymentReverseRequest request, String userId);
}
