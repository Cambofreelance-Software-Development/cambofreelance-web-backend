package com.cambofreelance.webbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentTransactionResponse {

    private String tranId;
    private String subscriptionId;
    private String userId;
    private String username;
    private BigDecimal amount;
    private String currency;
    private String paymentOption;
    private String paymentStatus;
    private String apv;
    /** USER or AUTO_RENEW */
    private String initiatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date verifiedAt;
}
