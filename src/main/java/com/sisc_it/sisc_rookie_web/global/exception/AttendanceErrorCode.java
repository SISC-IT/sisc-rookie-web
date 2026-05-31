package com.sisc_it.sisc_rookie_web.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AttendanceErrorCode implements ErrorCode {

    INVALID_ATTENDANCE_CODE(HttpStatus.BAD_REQUEST,"EVT-001", "출석 코드가 올바르지 않습니다."),

    ATTENDANCE_TIME_EXPIRED(HttpStatus.BAD_REQUEST,"EVT-002",  "출석 가능 시간이 지났습니다."),

    NOT_APPROVED_MEMBER(HttpStatus.FORBIDDEN, "EVT-003", "참여 승인된 부원만 출석할 수 있습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
