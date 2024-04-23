package com.wealdy.saemsembackend.domain.core.response;

import com.wealdy.saemsembackend.domain.core.exception.ExceptionBase;
import lombok.Getter;

@Getter
public class ErrorResponseDto {

    private final boolean error;
    private final int httpStatusCode;
    private final int errorCode;
    private final String errorMessage;

    public ErrorResponseDto(ExceptionBase exception) {
        this.error = true;
        this.httpStatusCode = exception.getStatusCode();
        this.errorCode = exception.getCode();
        this.errorMessage = exception.getMessage();
    }

    public ErrorResponseDto(int httpStatusCode, int errorCode, String errorMessage) {
        this.error = true;
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
