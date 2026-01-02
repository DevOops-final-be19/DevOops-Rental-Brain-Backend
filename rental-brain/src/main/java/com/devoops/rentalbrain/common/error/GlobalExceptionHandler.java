//package com.devoops.rentalbrain.common.error;
//
//import com.devoops.rentalbrain.common.error.exception.BusinessException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@Slf4j
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    /**
//     * BusinessException 처리
//     */
//    @ExceptionHandler(BusinessException.class)
//    public ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
//        ErrorCode errorCode = e.getErrorCode();
//
//        log.warn("[BusinessException] {} - {}", errorCode.getCode(), e.getMessage());
//
//        // BusinessException 은 커스텀 메시지가 있을 수 있음
//        ErrorResponse response = ErrorResponse.of(errorCode.getCode(), e.getMessage());
//
//        return ResponseEntity.status(errorCode.getStatus())
//                .body(response);
//    }
//
//
//    /**
//     * @Valid 검증 오류 처리 (RequestBody 검증)
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidationException(final MethodArgumentNotValidException e) {
//        ErrorCode errorCode = ErrorCode.INVALID_INPUT_VALUE;
//
//        String message = e.getBindingResult().getFieldError() != null
//                ? e.getBindingResult().getFieldError().getDefaultMessage()
//                : errorCode.getMessage();
//
//        log.warn("[ValidationError] {}", message);
//
//        ErrorResponse response = ErrorResponse.of(errorCode.getCode(), message);
//
//        return ResponseEntity.status(errorCode.getStatus())
//                .body(response);
//    }
//
//
//    /**
//     * 지원하지 않는 HTTP Method 예외 처리
//     */
//    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
//    public ResponseEntity<ErrorResponse> handleMethodNotSupported(final HttpRequestMethodNotSupportedException e) {
//        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
//
//        log.warn("[MethodNotSupported] {}", e.getMessage());
//
//        ErrorResponse response = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage());
//
//        return ResponseEntity.status(errorCode.getStatus())
//                .body(response);
//    }
//
//
//    /**
//     * 처리되지 않은 모든 예외 처리
//     */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleUnhandledException(final Exception e) {
//        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
//
//        log.error("[UnhandledException]", e);
//
//        ErrorResponse response = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage());
//
//        return ResponseEntity.status(errorCode.getStatus())
//                .body(response);
//    }
//}
