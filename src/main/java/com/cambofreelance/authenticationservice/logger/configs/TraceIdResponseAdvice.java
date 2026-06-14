package com.cambofreelance.authenticationservice.logger.configs;

import com.cambofreelance.authenticationservice.caches.ResponseCodeRedisCache;
import com.cambofreelance.authenticationservice.caches.ResponseManagerCache;
import com.cambofreelance.authenticationservice.logger.contants.Constants;
import com.cambofreelance.authenticationservice.logger.contants.enums.AcceptLanguage;
import com.cambofreelance.authenticationservice.logger.dto.BaseResponse;
import com.cambofreelance.authenticationservice.logger.exceptions.MessageResponse;
//import io.opentelemetry.api.trace.Span;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@RequiredArgsConstructor
public class TraceIdResponseAdvice implements ResponseBodyAdvice<Object> {

    private final ResponseCodeRedisCache responseCodeRedisCache;

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {

//        String traceId = getCurrentTraceId();
        long timestamp = Instant.now().toEpochMilli();

        if (body instanceof BaseResponse<?> apiResponse) {
            return setTraceAndTimestamp(apiResponse, UUID.randomUUID().toString(), timestamp);
        } else if (body instanceof MessageResponse messageResponse) {
            return processMessageResponse(messageResponse, request, UUID.randomUUID().toString(), timestamp);
        }

        return body;
    }

//    private String getCurrentTraceId() {
//        Span span = Span.current();
//        return span.getSpanContext().isValid() ? span.getSpanContext().getTraceId() : null;
//    }

    private BaseResponse<?> setTraceAndTimestamp(BaseResponse<?> response, String traceId, long timestamp) {
        response.setTraceId(traceId);
        response.setTimestamp(timestamp);
        return response;
    }

    private MessageResponse processMessageResponse(MessageResponse messageResponse,
                                                   ServerHttpRequest request,
                                                   String traceId,
                                                   long timestamp) {
        var responseCode = responseCodeRedisCache.getRespCode(messageResponse.getCode());
        if (Objects.isNull(responseCode)) {
            responseCode = ResponseManagerCache.getRespCode(messageResponse.getCode());
        }
        String acceptLangHeader = request.getHeaders().getFirst(Constants.CLIENT_LANG);
        AcceptLanguage lang = AcceptLanguage.fromValue(
                acceptLangHeader != null ? acceptLangHeader : Constants.LANG_EN,
                AcceptLanguage.EN
        );
        switch (lang) {
            case CN -> messageResponse.setMessage(responseCode.getMessageCn());
            case KM -> messageResponse.setMessage(responseCode.getMessageKm());
            default -> messageResponse.setMessage(responseCode.getMessage());
        }

        messageResponse.setTraceId(traceId);
        messageResponse.setTimestamp(timestamp);
        return messageResponse;
    }
}
