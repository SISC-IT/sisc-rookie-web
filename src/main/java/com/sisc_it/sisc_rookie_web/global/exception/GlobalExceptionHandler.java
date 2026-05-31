package com.sisc_it.sisc_rookie_web.global.exception;

import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sisc_it.sisc_rookie_web.global.response.ApiResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponse<Void>> makeErrorResponse(ErrorCode errorCode, String customMessage) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(
                        errorCode.getStatus().value(),
                        errorCode.getCode(),
                        customMessage != null ? customMessage : errorCode.getMessage()
                ));
    }
    //Response 조립 메서드
    //CustomMessage : ErrorCode.getMessage() 말고 다른 Message가 필요한 경우

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException exception) {
        return makeErrorResponse(exception.getErrorCode(), null);
    }

    /**
     * [HttpMessageNotReadableException 발생 시나리오]
     * 프론트엔드가 JSON 데이터를 서버로 보낼 때, 형식이 완전히 망가져서 스프링(Jackson 라이브러리)이 아예 읽지 못할 때 발생합니다.
     * * 예시 1) 타입 불일치 (가장 흔함)
     * - 백엔드 DTO: { "age": Integer }
     * - 프론트 요청: { "age": "스무살" } -> 숫자가 들어올 자리에 문자가 들어와서 파싱(역직렬화) 실패!
     * * 예시 2) JSON 문법 오류 (오타)
     * - 쉼표 누락: { "name": "홍길동" "age": 20 }
     * - 따옴표 누락: { name: "홍길동" }
     * - 괄호 누락: { "name": "홍길동"
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.warn("JSON Parse Error: {}", exception.getMessage());

        return makeErrorResponse(GlobalErrorCode.VALIDATION_ERROR, "요청 JSON 형식이 올바르지 않습니다. (타입 불일치 또는 문법 오류)");
    }

    /**
     * 3. 잘못된 HTTP 메서드 요청 방어
     * POST 로 와야 하는데, GET 혹은 다른 요청이 온 경우
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        log.warn("Method Not Supported: {}", exception.getMessage());
        return makeErrorResponse(GlobalErrorCode.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드 요청입니다.");
    }

    /**
     * 4. 유효성 검사 에러 (@Valid 방어)
     * 잘못된 형식으로 회원가입 시도
     * 1. email에 @가 없는 경우
     * 2. password가 8자리 이하인 경우
     * AuthController-signup @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        if (message.isBlank()) {
            message = GlobalErrorCode.VALIDATION_ERROR.getMessage();
        }

        return makeErrorResponse(GlobalErrorCode.VALIDATION_ERROR, message);
    }

    /**
     * 5. 최후의 방어선 (예측하지 못한 서버 에러)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {
        // 🚨 원인 파악을 위해 반드시 Stack Trace 전체를 Error 레벨로 남겨야 합니다.
        log.error("Unhandled Exception caught: ", exception);
        return makeErrorResponse(GlobalErrorCode.INTERNAL_SERVER_ERROR, null);
    }

    private String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }
    //이건 어디에 쓰는 것?
}