package com.wealdy.saemsembackend.domain.core.exception;

import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends ExceptionBase {

    public ExpiredTokenException() {
        code = ResponseCode.EXPIRED_TOKEN.getCode();
        message = ResponseCode.EXPIRED_TOKEN.getMessage();
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}
