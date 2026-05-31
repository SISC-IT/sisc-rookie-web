package com.sisc_it.sisc_rookie_web.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    int status,
    String message,
    String code,
    T data
) {

    // 성공 시에는 커스텀 에러 코드가 필요 없으므로 code 자리에 null을 넣습니다.
    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(status, null, message, data);
    }

    // 데이터(data)는 없고, 상태코드, 커스텀코드, 메시지만 줍니다.
    public static <T> ApiResponse<T> error(int status, String code, String message) {
        return new ApiResponse<>(status, code, message, null);
    }


}
