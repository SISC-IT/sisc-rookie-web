package com.sisc_it.sisc_rookie_web.global.response;

public record ApiResponse<T>(
    int status,
    String message,
    T data
) {

    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(status, message, null);
    }
}
