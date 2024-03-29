package com.wealdy.saemsembackend.domain.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS(0, "OK"),

    BAD_REQUEST(400, "Bad Request"),

    // Http Status Code 400
    INVALID_PARAMETER(1000, "Invalid Parameter"),
    ALREADY_EXIST_ID(2000, "이미 존재하는 아이디입니다."),
    ALREADY_EXIST_NICKNAME(2001, "이미 존재하는 닉네임입니다."),

    // Http Status Code 404
    NOT_FOUND_USER(4001, "존재하지 않는 회원입니다."),

    // HTTP_CODE 500
    UNKNOWN_ERROR(-1, "Unknown Error");

    private final int code;
    private final String message;
}
