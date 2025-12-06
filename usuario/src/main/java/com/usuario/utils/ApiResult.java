package com.usuario.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {
    private T data;
    private String error;

    private ApiResult(T data, String error) {
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(data, null);
    }

    public static <T> ApiResult<T> error(String message) {
        return new ApiResult<>(null, message);
    }

    public boolean isSuccess() {
        return error == null;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}