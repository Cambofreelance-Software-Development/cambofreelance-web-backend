package com.cambofreelance.webbackend.payway;

import com.cambofreelance.webbackend.constants.ErrorCode;
import com.cambofreelance.webbackend.logger.exceptions.AppException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
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
 * The purchase is NOT called server-to-server: we sign the field set here and the
 * frontend form-posts it to PayWay's hosted checkout, so the API key never leaves
 * the backend and PayWay renders its own payment page. Status verification
 * (transaction-detail) IS server-to-server and is the only source of truth.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PaywayClient {

    public static final String PURCHASE_PATH           = "/api/payment-gateway/v1/payments/purchase";
    public static final String TRANSACTION_DETAIL_PATH = "/api/payment-gateway/v1/payments/transaction-detail";

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${payway.base-url:https://checkout-sandbox.payway.com.kh}")
    private String baseUrl;

    @Value("${payway.merchant-id:}")
    private String merchantId;

    @Value("${payway.api-key:}")
    private String apiKey;

    public String checkoutUrl() {
        return baseUrl + PURCHASE_PATH;
    }

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
     * Builds the signed multipart/form field set for PayWay's purchase API.
     * The frontend submits these verbatim as a form POST to {@link #checkoutUrl()}.
     * Field order in the hash is fixed by PayWay's spec — do not reorder.
     */
    public Map<String, String> buildPurchaseFields(String tranId, BigDecimal amount, String currency,
                                                   String firstname, String lastname, String email, String phone,
                                                   String itemsJson, String paymentOption,
                                                   String returnUrl, String cancelUrl, String continueSuccessUrl,
                                                   String returnParams) {
        requireConfig();
        String reqTime   = utcNow();
        String amountStr = amount.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
        String items     = itemsJson == null ? "" : b64(itemsJson);
        String returnB64 = returnUrl == null ? "" : b64(returnUrl);
        String type      = "purchase";

        String fn  = nz(firstname), ln = nz(lastname), em = nz(email), ph = nz(phone);
        String po  = nz(paymentOption), cu = nz(cancelUrl), csu = nz(continueSuccessUrl), rp = nz(returnParams);
        String cur = nz(currency);

        // Spec order: req_time + merchant_id + tran_id + amount + items + shipping + firstname
        // + lastname + email + phone + type + payment_option + return_url + cancel_url
        // + continue_success_url + return_deeplink + currency + custom_fields + return_params
        // + payout + lifetime + additional_params + google_pay_token + skip_success_page
        String toSign = reqTime + merchantId + tranId + amountStr + items + /*shipping*/ ""
            + fn + ln + em + ph + type + po + returnB64 + cu + csu + /*return_deeplink*/ ""
            + cur + /*custom_fields*/ "" + rp + /*payout*/ "" + /*lifetime*/ ""
            + /*additional_params*/ "" + /*google_pay_token*/ "" + /*skip_success_page*/ "";

        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("req_time", reqTime);
        fields.put("merchant_id", merchantId);
        fields.put("tran_id", tranId);
        fields.put("amount", amountStr);
        if (!items.isEmpty())     fields.put("items", items);
        if (!fn.isEmpty())        fields.put("firstname", fn);
        if (!ln.isEmpty())        fields.put("lastname", ln);
        if (!em.isEmpty())        fields.put("email", em);
        if (!ph.isEmpty())        fields.put("phone", ph);
        fields.put("type", type);
        if (!po.isEmpty())        fields.put("payment_option", po);
        if (!returnB64.isEmpty()) fields.put("return_url", returnB64);
        if (!cu.isEmpty())        fields.put("cancel_url", cu);
        if (!csu.isEmpty())       fields.put("continue_success_url", csu);
        if (!cur.isEmpty())       fields.put("currency", cur);
        if (!rp.isEmpty())        fields.put("return_params", rp);
        fields.put("hash", hmacSha512B64(toSign));
        return fields;
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
