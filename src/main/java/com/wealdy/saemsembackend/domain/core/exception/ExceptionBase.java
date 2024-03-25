package com.wealdy.saemsembackend.domain.core.exception;

import lombok.Getter;

@Getter
public abstract class ExceptionBase extends RuntimeException {

    public abstract int getStatusCode();

    protected int code;
    protected String message;
}
