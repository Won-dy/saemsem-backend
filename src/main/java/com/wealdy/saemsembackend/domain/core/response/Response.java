package com.wealdy.saemsembackend.domain.core.response;

import lombok.Getter;

@Getter
public class Response<T> {

    private boolean success;
    private int code;
    private int httpStatusCode;
    private T result;

    public Response(T result) {
        this.success = true;
        this.code = ResponseCode.SUCCESS.getCode();
        this.httpStatusCode = 200;
        this.result = result;
    }

    public static <T> Response<T> of(T result) {
        return new Response<>(result);
    }
}
