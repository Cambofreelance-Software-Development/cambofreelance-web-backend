package com.cambofreelance.webbackend.logger.exceptions;

import com.cambofreelance.webbackend.caches.ResponseCodeRedisCache;
import com.cambofreelance.webbackend.caches.ResponseManagerCache;
import com.cambofreelance.webbackend.dto.ResponseCodeDto;
import com.cambofreelance.webbackend.logger.contants.ErrorCode;
import com.cambofreelance.webbackend.logger.contants.LoggerConstant;
import com.cambofreelance.webbackend.logger.contants.LoggerErrorCode;
import com.cambofreelance.webbackend.logger.contants.enums.AcceptLanguage;
import com.cambofreelance.webbackend.logger.dto.AppLogger;
import com.cambofreelance.webbackend.logger.dto.BaseResponse;
import com.cambofreelance.webbackend.logger.dto.ErrorResponse;
import com.cambofreelance.webbackend.logger.utils.ExceptionUtils;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppLoggerResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final AppLogger appLogger;
    private final ResponseCodeRedisCache responseCodeRedisCache;

    private Map<String, String> resolveMessage(String errorCode) {
        var responseCode = responseCodeRedisCache.getRespCode(errorCode);
        if (Objects.isNull(responseCode)) {
            responseCode = ResponseManagerCache.getRespCode(errorCode);
        }
        return Optional.ofNullable(responseCode)
                .map(ResponseCodeDto::getErrorMessage)
                .orElse(ExceptionUtils.messageNotFound());
    }

    private String resolveI18Message(Map<String, String> message, WebRequest request) {
        String i18MessageKey = AcceptLanguage.fromValue(
                request.getHeader(LoggerConstant.ACCEPT_LANGUAGE_HEADER), AcceptLanguage.EN).getKey();
        return message.getOrDefault(i18MessageKey, LoggerConstant.NA);
    }

    private ErrorResponse buildErrorResponse(Map<String, String> message) {
        return ErrorResponse.builder()
                .messageEn(message.get(LoggerConstant.MESSAGE))
                .messageKm(message.get(LoggerConstant.MESSAGE_KH))
                .messageCh(message.get(LoggerConstant.MESSAGE_CN))
                .httpStatus(message.get(LoggerConstant.HTTP_STATUS))
                .build();
    }

    private BaseResponse<ErrorResponse> buildBaseResponse(String code, ErrorResponse errorResponse, String i18Message) {
        return BaseResponse.<ErrorResponse>builder()
                .code(code)
                .success(false)
                .timestamp(System.currentTimeMillis())
                .data(errorResponse)
                .message(i18Message)
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        appLogger.setException(ex);

        String errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.joining(", "));


        Map<String, String> message = resolveMessage(LoggerErrorCode.INVALID_FIELD);
        String i18Message = resolveI18Message(message, request);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .messageEn(formatErrorMessage(message.get(LoggerConstant.MESSAGE), errors))
                .messageKm(formatErrorMessage(message.get(LoggerConstant.MESSAGE_KH), errors))
                .messageCh(formatErrorMessage(message.get(LoggerConstant.MESSAGE_CN), errors))
                .httpStatus(formatErrorMessage(message.get(LoggerConstant.HTTP_STATUS), errors))
                .build();

        BaseResponse<ErrorResponse> baseResponse = buildBaseResponse(LoggerErrorCode.BAD_REQUEST, errorResponse,
                String.format("%s [%s]", i18Message, errors));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
    }

    @ExceptionHandler({CannotGetJdbcConnectionException.class, SQLException.class})
    public ResponseEntity<Object> handleDatabaseConnectionException(Exception ex, WebRequest request) {
        appLogger.setException(ex);

        Map<String, String> message = resolveMessage(LoggerErrorCode.DATABASE_CONNECTION_ERROR);
        String i18Message = resolveI18Message(message, request);

        ErrorResponse errorResponse = buildErrorResponse(message);
        BaseResponse<ErrorResponse> baseResponse =
                buildBaseResponse(LoggerErrorCode.DATABASE_CONNECTION_ERROR, errorResponse, i18Message);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<Object> handleWebClientRequestException(WebClientRequestException ex, WebRequest request) {
        appLogger.setException(ex);

        Map<String, String> message = resolveMessage(LoggerErrorCode.WEB_CLIENT_REQUEST_ERROR);
        String i18Message = resolveI18Message(message, request);

        ErrorResponse errorResponse = buildErrorResponse(message);
        BaseResponse<ErrorResponse> baseResponse =
                buildBaseResponse(LoggerErrorCode.WEB_CLIENT_REQUEST_ERROR, errorResponse, i18Message);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(baseResponse);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Object> handleServiceUnavailable(ServiceUnavailableException ex, WebRequest request) {
        appLogger.setException(ex);

        Map<String, String> message = resolveMessage(LoggerErrorCode.SERVICE_NOT_UNAVAILABLE);
        String i18Message = resolveI18Message(message, request);

        ErrorResponse errorResponse = buildErrorResponse(message);
        BaseResponse<ErrorResponse> baseResponse =
                buildBaseResponse(LoggerErrorCode.SERVICE_NOT_UNAVAILABLE, errorResponse, i18Message);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(baseResponse);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> handleAppException(AppException ex, WebRequest request) {
        appLogger.setException(ex);

        Map<String, String> message = resolveMessage(ex.getErrorCode());
        String i18Message = resolveI18Message(message, request);

        if (Objects.equals(i18Message, "Message not yet update in our system")) {
            i18Message = ex.getMessage();
        }

        ErrorResponse errorResponse = buildErrorResponse(message);
        BaseResponse<ErrorResponse> baseResponse =
                buildBaseResponse(ex.getErrorCode(), errorResponse, i18Message);
        // Only registered response codes carry an explicit httpStatus; ad-hoc AppException
        // codes (the common case) must fall back to the exception's own status (default 400),
        // never silently to 200 — otherwise the client treats a failed request as a success.
        HttpStatus httpStatus = Strings.isEmpty(errorResponse.getHttpStatus())
                ? ex.getHttpStatus()
                : HttpStatus.valueOf(Integer.parseInt(errorResponse.getHttpStatus()));
        return ResponseEntity.status(httpStatus).body(baseResponse);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        appLogger.setException(ex);

        Map<String, String> message = resolveMessage(LoggerErrorCode.INVALID_FIELD);
        String i18Message = resolveI18Message(message, request);

        ErrorResponse errorResponse = buildErrorResponse(message);
        BaseResponse<ErrorResponse> baseResponse =
                buildBaseResponse(LoggerErrorCode.INVALID_FIELD, errorResponse, i18Message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(baseResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        appLogger.setException(ex);

        Map<String, String> message = resolveMessage(ErrorCode.ACCESS_DENIED);
        String i18Message = resolveI18Message(message, request);

        if (Objects.equals(i18Message, "Message not yet update in our system") || Strings.isEmpty(i18Message)) {
            i18Message = "You do not have permission to access this resource";
        }

        ErrorResponse errorResponse = buildErrorResponse(message);
        BaseResponse<ErrorResponse> baseResponse =
                buildBaseResponse(ErrorCode.ACCESS_DENIED, errorResponse, i18Message);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(baseResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        appLogger.setException(ex);

        Map<String, String> message = resolveMessage(LoggerErrorCode.INTERNAL_SERVER_ERROR);
        String i18Message = resolveI18Message(message, request);

        ErrorResponse errorResponse = buildErrorResponse(message);
        BaseResponse<ErrorResponse> baseResponse =
                buildBaseResponse(LoggerErrorCode.INTERNAL_SERVER_ERROR, errorResponse, i18Message);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponse);
    }


    private String formatErrorMessage(String baseMessage, String errors) {
        if (baseMessage == null) {
            baseMessage = "";
        }
        return errors == null || errors.isBlank()
                ? baseMessage
                : String.format("%s [%s]", baseMessage, errors);
    }
}
