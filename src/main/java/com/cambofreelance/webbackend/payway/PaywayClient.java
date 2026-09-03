package com.cambofreelance.webbackend.payway;

import com.cambofreelance.webbackend.constants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Thin client for ABA PayWay.
 *
 * The `sopposstore` merchant profile uses ABA's dedicated "QR API"
 * ({@code POST /api/payment-gateway/v1/payments/generate-qr}) — a synchronous, server-to-server
 * call that answers directly with an embedded KHQR payload (no hosted-page redirect), so the
 * backend hands the frontend a QR image to render in-page. This is a different endpoint/hash
 * spec than ABA's hosted-checkout "Purchase API" (which takes return_url/cancel_url/
 * continue_success_url) — don't conflate the two. Status verification (transaction-detail) is
 * separately server-to-server and remains the only source of truth for settlement.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PaywayClient {

    public static final String GENERATE_QR_PATH        = "/api/payment-gateway/v1/payments/generate-qr";
    public static final String TRANSACTION_DETAIL_PATH = "/api/payment-gateway/v1/payments/transaction-detail";

    /** Selects ABA's branded KHQR image design (logo + colored ribbon baked into {@code qrImage}). */
    private static final String QR_IMAGE_TEMPLATE = "template3_color";

    /** Default payment network when the caller doesn't request a specific one. */
    private static final String DEFAULT_PAYMENT_OPTION = "abapay_khqr";

    /** QR validity window sent to PayWay. Spec allows 3 to 43200 minutes (30 days). */
    private static final int QR_LIFETIME_MINUTES = 30;

    /** Bounds how long a checkout request waits on PayWay before failing fast with a clear error. */
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${payway.base-url:https://checkout-sandbox.payway.com.kh}")
    private String baseUrl;

    @Value("${payway.merchant-id:}")
    private String merchantId;

    @Value("${payway.api-key:}")
    private String apiKey;

    /** Fails fast with a clear message when PayWay credentials are not configured. */
    private void requireConfig() {
        if (merchantId == null || merchantId.isBlank() || apiKey == null || apiKey.isBlank()) {
            AppException ex = new AppException(ErrorCode.GENERAL_ERROR,
                "Payment gateway is not configured (payway.merchant-id / payway.api-key)");
            ex.setHttpStatus(HttpStatus.SERVICE_UNAVAILABLE);
            throw ex;
        }
    }

    /**
     * Calls PayWay's QR API server-to-server and returns the embedded KHQR payload.
     * Field order in the hash is fixed by PayWay's spec — do not reorder.
     *
     * @param callbackUrl  PayWay's webhook target for this transaction (never a browser
     *                     redirect — the QR API has no return_url/cancel_url of its own; the
     *                     frontend renders the QR in-page and polls/receives the callback).
     */
    public PaywayPurchaseResult purchase(String tranId, BigDecimal amount, String currency,
                                          String firstname, String lastname, String email, String phone,
                                          String itemsJson, String paymentOption,
                                          String callbackUrl, String returnParams) {
        Map<String, Object> fields = buildPurchaseFields(tranId, amount, currency, firstname, lastname, email, phone,
            itemsJson, paymentOption, callbackUrl, returnParams);

        String response;
        try {
            response = webClient.post()
                .uri(baseUrl + GENERATE_QR_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(fields)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();
        } catch (WebClientResponseException e) {
            log.warn("[PayWay] purchase HTTP {} for tran_id={}: {}",
                e.getStatusCode(), tranId, e.getResponseBodyAsString());
            response = e.getResponseBodyAsString();
        } catch (Exception e) {
            boolean isTimeout = e instanceof java.util.concurrent.TimeoutException
                || e.getCause() instanceof java.util.concurrent.TimeoutException;
            log.error("[PayWay] purchase call failed for tran_id={}", tranId, e);
            AppException ex = new AppException(ErrorCode.GENERAL_ERROR,
                isTimeout ? "Payment gateway did not respond in time — please try again" : "Payment gateway is unreachable");
            ex.setHttpStatus(isTimeout ? HttpStatus.GATEWAY_TIMEOUT : HttpStatus.SERVICE_UNAVAILABLE);
            throw ex;
        }

        log.info("[PayWay] purchase tran_id={} response={}", tranId,
            response != null && response.length() > 300 ? response.substring(0, 300) + "..." : response);
        return parsePurchaseResponse(response);
    }

    private PaywayPurchaseResult parsePurchaseResponse(String response) {
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode status = root.path("status");
            return PaywayPurchaseResult.builder()
                .qrString(root.path("qrString").asText(null))
                .qrImage(root.path("qrImage").asText(null))
                .abapayDeeplink(root.path("abapay_deeplink").asText(null))
                .statusCode(status.path("code").asText(null))
                .statusMessage(status.path("message").asText(null))
                .raw(response)
                .build();
        } catch (Exception e) {
            log.error("[PayWay] failed to parse purchase response", e);
            return PaywayPurchaseResult.builder().statusCode("-1").statusMessage("Unparseable response").raw(response).build();
        }
    }

    /**
     * Builds the signed field set for PayWay's QR API ({@code generate-qr}).
     * Field order in the hash is fixed by PayWay's spec — do not reorder.
     */
    private Map<String, Object> buildPurchaseFields(String tranId, BigDecimal amount, String currency,
                                                   String firstname, String lastname, String email, String phone,
                                                   String itemsJson, String paymentOption,
                                                   String callbackUrl, String returnParams) {
        requireConfig();
        String reqTime      = utcNow();
        String amountStr    = amount.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
        String items        = itemsJson == null ? "" : b64(itemsJson);
        String callbackB64  = callbackUrl == null ? "" : b64(callbackUrl);
        String purchaseType = "purchase";
        String lifetime     = String.valueOf(QR_LIFETIME_MINUTES);

        String fn  = nz(firstname), ln = nz(lastname), em = nz(email), ph = nz(phone);
        String po  = (paymentOption == null || paymentOption.isBlank()) ? DEFAULT_PAYMENT_OPTION : paymentOption;
        String rp  = nz(returnParams);
        String cur = nz(currency);

        // Spec order (ABA "QR API", POST /generate-qr): req_time + merchant_id + tran_id + amount
        // + items + first_name + last_name + email + phone + purchase_type + payment_option
        // + callback_url + return_deeplink + currency + custom_fields + return_params + payout
        // + lifetime + qr_image_template. This is a different endpoint/hash spec than ABA's
        // hosted-checkout "Purchase API" — don't merge the two field orders.
        String toSign = reqTime + merchantId + tranId + amountStr + items
            + fn + ln + em + ph + purchaseType + po + callbackB64 + /*return_deeplink*/ ""
            + cur + /*custom_fields*/ "" + rp + /*payout*/ "" + lifetime + QR_IMAGE_TEMPLATE;
        // If ABA ever responds "Wrong Hash" again, diff this against ABA's sandbox hash-generator
        // tool — this line never logs the api-key, only the plaintext that gets signed with it.
        if (log.isDebugEnabled()) {
            log.debug("[PayWay] tran_id={} string-to-sign=\"{}\"", tranId, toSign);
        }

        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("req_time", reqTime);
        fields.put("merchant_id", merchantId);
        fields.put("tran_id", tranId);
        fields.put("amount", amountStr);
        fields.put("currency", cur);
        fields.put("payment_option", po);
        fields.put("purchase_type", purchaseType);
        fields.put("lifetime", QR_LIFETIME_MINUTES);
        fields.put("qr_image_template", QR_IMAGE_TEMPLATE);
        if (!items.isEmpty())      fields.put("items", items);
        if (!fn.isEmpty())         fields.put("first_name", fn);
        if (!ln.isEmpty())         fields.put("last_name", ln);
        if (!em.isEmpty())         fields.put("email", em);
        if (!ph.isEmpty())         fields.put("phone", ph);
        if (!callbackB64.isEmpty()) fields.put("callback_url", callbackB64);
        if (!rp.isEmpty())         fields.put("return_params", rp);
        fields.put("hash", hmacSha512B64(toSign));
        return fields;
    }

    /**
     * STUB — ABA has NOT enabled Card-on-File / recurring charge on the {@code sopposstore}
     * merchant account, and no official token-charge field spec has been shared. This method
     * intentionally makes no HTTP request: guessing at field names for a money-moving API is
     * worse than failing loudly.
     *
     * TODO: verify against ABA's real COF spec once available — replace this body with the
     * actual server-to-server token-charge request (endpoint path, required fields, hash field
     * order, response shape) before {@code SubscriptionServiceImpl#attemptAutoRenewals} can ever
     * succeed.
     */
    public PaywayPurchaseResult chargeStoredToken(String tranId, String paymentToken, BigDecimal amount, String currency) {
        log.error("[PayWay] chargeStoredToken called but ABA Card-on-File is not enabled/spec'd — "
            + "refusing to guess at field names for tran_id={}", tranId);
        AppException ex = new AppException(ErrorCode.AUTO_RENEW_NOT_AVAILABLE,
            "Card-on-file auto-renewal is not available yet (ABA has not enabled recurring charge for this merchant)");
        ex.setHttpStatus(HttpStatus.NOT_IMPLEMENTED);
        throw ex;
    }

    /**
     * STUB — ABA's refund API is not documented anywhere in this codebase (no doc bundle, no
     * prior integration, and full-repo search turns up nothing). Guessing at the endpoint path,
     * required fields, or hash field order for a money-moving API is worse than failing loudly
     * (same reasoning as {@link #chargeStoredToken}).
     *
     * TODO: once ABA shares its real refund API spec, replace this body with the actual
     * server-to-server refund request (endpoint path, required fields, hash field order,
     * response shape) — everything upstream (SubscriptionServiceImpl#refundPayment, the admin
     * endpoint, and the frontend Refund button) already calls this method and only needs this
     * one method to start working end-to-end.
     */
    public void refund(String tranId, BigDecimal amount, String reason) {
        log.error("[PayWay] refund called but ABA's refund API is not implemented/spec'd — "
            + "refusing to guess at field names for tran_id={}", tranId);
        AppException ex = new AppException(ErrorCode.GENERAL_ERROR,
            "Automatic refund via ABA PayWay is not available yet — process the refund in ABA's "
                + "merchant portal, then this action can be extended to record it");
        ex.setHttpStatus(HttpStatus.NOT_IMPLEMENTED);
        throw ex;
    }

    /** Server-to-server status check — the source of truth for a transaction. */
    public PaywayTransactionStatus fetchTransactionDetail(String tranId) {
        requireConfig();
        String reqTime = utcNow();
        String hash = hmacSha512B64(reqTime + merchantId + tranId);

        Map<String, String> body = new LinkedHashMap<>();
        body.put("req_time", reqTime);
        body.put("merchant_id", merchantId);
        body.put("tran_id", tranId);
        body.put("hash", hash);

        // A PayWay error/outage must not break callers — an unknown status keeps the
        // transaction PENDING and reconciliation/polling settles it later.
        String response;
        try {
            response = webClient.post()
                .uri(baseUrl + TRANSACTION_DETAIL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .block();
        } catch (WebClientResponseException e) {
            log.warn("[PayWay] transaction-detail HTTP {} for tran_id={}: {}",
                e.getStatusCode(), tranId, e.getResponseBodyAsString());
            response = e.getResponseBodyAsString();
        } catch (Exception e) {
            log.error("[PayWay] transaction-detail call failed for tran_id={}", tranId, e);
            return PaywayTransactionStatus.builder()
                .paymentStatusCode(-1)
                .raw(e.getMessage())
                .build();
        }

        log.info("[PayWay] transaction-detail tran_id={} response={}", tranId, response);
        return parseTransactionDetail(response);
    }

    private PaywayTransactionStatus parseTransactionDetail(String response) {
        int code = -1;
        String paymentStatus = null, apv = null, bankRef = null, currency = null;
        BigDecimal amount = null;
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode data = root.has("data") ? root.get("data") : root;
            if (data.hasNonNull("payment_status_code")) code = data.get("payment_status_code").asInt(-1);
            if (data.hasNonNull("payment_status"))      paymentStatus = data.get("payment_status").asText();
            if (data.hasNonNull("apv"))                 apv = data.get("apv").asText();
            if (data.hasNonNull("bank_ref"))            bankRef = data.get("bank_ref").asText();
            if (data.hasNonNull("payment_currency"))    currency = data.get("payment_currency").asText();
            if (data.hasNonNull("payment_amount"))      amount = new BigDecimal(data.get("payment_amount").asText());
            // Fallback: some responses only carry the textual status
            if (code == -1 && paymentStatus != null) {
                code = switch (paymentStatus.toUpperCase()) {
                    case "APPROVED", "PRE-AUTH" -> 0;
                    case "PENDING"              -> 2;
                    case "DECLINED"             -> 3;
                    case "REFUNDED"             -> 4;
                    case "CANCELLED"            -> 7;
                    default                     -> -1;
                };
            }
        } catch (Exception e) {
            log.error("[PayWay] failed to parse transaction-detail response", e);
        }
        return PaywayTransactionStatus.builder()
            .paymentStatusCode(code)
            .paymentStatus(paymentStatus)
            .apv(apv)
            .bankRef(bankRef)
            .amount(amount)
            .currency(currency)
            .raw(response)
            .build();
    }

    private String hmacSha512B64(String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(apiKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to compute PayWay hash", e);
        }
    }

    private static String utcNow() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        fmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return fmt.format(new Date());
    }

    private static String b64(String v) {
        return Base64.getEncoder().encodeToString(v.getBytes(StandardCharsets.UTF_8));
    }

    private static String nz(String v) {
        return v == null ? "" : v;
    }
}
