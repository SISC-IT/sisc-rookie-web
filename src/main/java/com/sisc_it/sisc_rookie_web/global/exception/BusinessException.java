package com.sisc_it.sisc_rookie_web.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode; // 다형성: 어떤 Enum이든 다 받을 수 있음!

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}