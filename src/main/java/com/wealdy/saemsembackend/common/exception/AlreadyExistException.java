package com.wealdy.saemsembackend.common.exception;

import com.wealdy.saemsembackend.common.response.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AlreadyExistException extends ExceptionBase {

    public AlreadyExistException(ResponseCode responseCode) {
        code = responseCode.getCode();
        message = responseCode.getMessage();
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
