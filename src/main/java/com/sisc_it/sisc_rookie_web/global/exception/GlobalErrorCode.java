package com.sisc_it.sisc_rookie_web.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {

    // 1. DTO 유효성 검사 및 JSON 파싱 에러용 (400)
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "GLB-400", "요청 데이터가 올바르지 않습니다."),

    // 2. HTTP 메서드 오류용 (405)
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "GLB-405", "지원하지 않는 HTTP 메서드입니다."),

    // 3. 최후의 보루 500 에러용
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLB-500", "서버 내부에 알 수 없는 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}