package com.wealdy.saemsembackend.common.response;

import lombok.Getter;

@Getter
public class Response<T> {

    private boolean success;
    private int code;
    private int http_status_code;
    private T result;

    public Response(T result) {
        this.success = true;
        this.code = ResponseCode.SUCCESS.getCode();
        this.http_status_code = 200;
        this.result = result;
    }

    public static <T> Response<T> of(T result) {
        return new Response<>(result);
    }
}
