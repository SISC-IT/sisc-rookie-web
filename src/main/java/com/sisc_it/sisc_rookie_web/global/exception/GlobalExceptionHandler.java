package com.sisc_it.sisc_rookie_web.global.exception;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sisc_it.sisc_rookie_web.global.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getStatus().value(), errorCode.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining(", "));

        if (message.isBlank()) {
            message = GlobalErrorCode.VALIDATION_ERROR.getMessage(); // 수정: GlobalErrorCode 사용
        }

        // TODO: 현재는 필드 에러들을 하나의 문자열로 이어 붙여서 반환하고 있음.
        // 추후 프론트엔드와 협의하여 Map<String, String> 형태로 에러를 구조화해서 내려주는 방식(ApiResponse<Object> 도입 등) 논의 필요.
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(GlobalErrorCode.VALIDATION_ERROR.getStatus().value(), message)); // 수정: GlobalErrorCode 사용
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {
        ErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR; // 수정: GlobalErrorCode 사용
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getStatus().value(), errorCode.getMessage()));
    }

    private String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }
}