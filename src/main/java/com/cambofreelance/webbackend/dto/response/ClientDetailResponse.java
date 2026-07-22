package com.cambofreelance.webbackend.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDetailResponse {

    private ClientResponse client;
    private List<SubscriptionResponse> subscriptions;
    private List<PaymentTransactionResponse> payments;
    private List<InvoiceResponse> invoices;
    /** Recent payment workflow events — serves as the client activity feed */
    private List<PaymentEventResponse> activity;
}
