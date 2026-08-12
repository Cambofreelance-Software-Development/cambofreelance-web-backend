package com.cambofreelance.webbackend.payway;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/** Normalized result of PayWay's transaction-detail API. */
@Data
@Builder
public class PaywayTransactionStatus {

    /** PayWay payment_status_code: 0 APPROVED, 2 PENDING, 3 DECLINED, 4 REFUNDED, 7 CANCELLED. -1 = not found / unknown */
    private int paymentStatusCode;
    private String paymentStatus;
    private String apv;
    private String bankRef;
    private BigDecimal amount;
    private String currency;
    /** Raw JSON body from PayWay, kept for audit */
    private String raw;

    public boolean isApproved()  { return paymentStatusCode == 0; }
    public boolean isDeclined()  { return paymentStatusCode == 3; }
    public boolean isRefunded()  { return paymentStatusCode == 4; }
    public boolean isCancelled() { return paymentStatusCode == 7; }
}
