package com.wealdy.saemsembackend.domain.core.exception;

import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotSetException extends ExceptionBase {

    public NotSetException(ResponseCode responseCode) {
        code = responseCode.getCode();
        message = responseCode.getMessage();
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
