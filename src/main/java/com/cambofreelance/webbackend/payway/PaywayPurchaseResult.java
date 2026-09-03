package com.cambofreelance.webbackend.payway;

import lombok.Builder;
import lombok.Data;

/**
 * Result of PayWay's purchase API for this merchant profile, which always answers with an
 * embedded KHQR payload (qrImage/qrString) instead of redirecting to a hosted page — the
 * frontend renders the QR in-page and the payment is confirmed later via
 * {@link PaywayClient#fetchTransactionDetail} (callback or polling), same as any other flow.
 */
@Data
@Builder
public class PaywayPurchaseResult {

    /** KHQR payload string (also encoded into {@link #qrImage}) */
    private String qrString;
    /** data:image/png;base64,... ready to drop into an <img src> */
    private String qrImage;
    /** Deep link to open the QR directly in the ABA Mobile app, when provided */
    private String abapayDeeplink;
    /** "0" on success */
    private String statusCode;
    private String statusMessage;
    /** Raw JSON body from PayWay, kept for audit */
    private String raw;

    public boolean isSuccess() {
        return "0".equals(statusCode);
    }
}
