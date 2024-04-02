package com.wealdy.saemsembackend.domain.core.exception;

import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ExceptionBase {

    public InvalidTokenException() {
        code = ResponseCode.INVALID_TOKEN.getCode();
        message = ResponseCode.INVALID_TOKEN.getMessage();
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}
