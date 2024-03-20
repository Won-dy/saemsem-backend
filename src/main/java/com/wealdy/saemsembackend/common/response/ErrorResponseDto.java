package com.wealdy.saemsembackend.common.response;

import com.wealdy.saemsembackend.common.exception.ExceptionBase;
import lombok.Getter;

@Getter
public class ErrorResponseDto {
    private boolean error;
    private int http_status_code;
    private int error_code;
    private String error_message;

    public ErrorResponseDto(ExceptionBase exception) {
        this.error = true;
        this.http_status_code = exception.getStatusCode();
        this.error_code = exception.getCode();
        this.error_message = exception.getMessage();
    }

    public ErrorResponseDto(int http_status_code, int error_code, String error_message) {
        this.error = true;
        this.http_status_code = http_status_code;
        this.error_code = error_code;
        this.error_message = error_message;
    }
}
