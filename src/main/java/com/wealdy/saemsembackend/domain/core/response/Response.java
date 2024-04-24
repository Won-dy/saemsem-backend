package com.wealdy.saemsembackend.domain.core.response;

import lombok.Getter;

@Getter
public class Response<T> {

    public static final Response<Void> OK = new Response<>();
//    public static final Response<Void> OK2 = Response.of(null);

    private final boolean success = true;
    private final int code = ResponseCode.SUCCESS.getCode();
    private final int httpStatusCode = 200;
    private final T result;

    public Response(T result) {
        this.result = result;
    }

    public Response() {
        this.result = null;
    }

    public static <T> Response<T> of(T result) {
        return new Response<>(result);
    }
}
