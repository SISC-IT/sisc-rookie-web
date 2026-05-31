// 회원/인증 도메인 에러 (각 도메인 패키지 하위에 위치시켜도 됩니다)
package com.sisc_it.sisc_rookie_web.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "AUTH-001", "이미 사용 중인 이메일입니다."),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED,"AUT-002",  "이메일 또는 비밀번호가 올바르지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH-003", "회원을 찾을 수 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-004", "유효하지 않은 토큰입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-005", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH-006", "접근 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}