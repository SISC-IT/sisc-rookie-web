package com.sisc_it.sisc_rookie_web.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EventErrorCode implements ErrorCode{

    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "EVT-001", "해당 행사를 찾을 수 없습니다."),

    CAPACITY_EXCEEDED(HttpStatus.BAD_REQUEST,  "EVT-002", "행사 모집 정원이 초과되었습니다."),

    INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "EVT-003", "종료된 행사는 상태를 변경할 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
