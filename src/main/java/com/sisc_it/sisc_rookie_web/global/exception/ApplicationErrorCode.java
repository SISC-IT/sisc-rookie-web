package com.sisc_it.sisc_rookie_web.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApplicationErrorCode implements ErrorCode{

    ALREADY_APPLIED(HttpStatus.CONFLICT,"APP-001",  "이미 신청이 완료된 행사입니다."),

    APPLICATION_PERIOD_EXPIRED(HttpStatus.BAD_REQUEST,"APP-002",  "행사 신청 기간이 아닙니다."),

    CANCEL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "APP-003", "이미 승인된 신청은 취소할 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;


}
