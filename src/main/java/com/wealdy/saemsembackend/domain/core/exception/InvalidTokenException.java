package com.wealdy.saemsembackend.domain.core.exception;

import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ExceptionBase {

    public InvalidTokenException(ResponseCode responseCode) {
        code = responseCode.getCode();
        message = responseCode.getMessage();
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.UNAUTHORIZED.value();
    }
}
