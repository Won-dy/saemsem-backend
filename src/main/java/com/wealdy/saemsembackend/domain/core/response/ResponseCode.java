package com.wealdy.saemsembackend.domain.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    SUCCESS(0, "OK"),

    // Http Status Code 400
    INVALID_PARAMETER(1000, "Invalid Parameter"),
    ALREADY_EXIST_ID(2000, "이미 존재하는 아이디입니다."),
    ALREADY_EXIST_NICKNAME(2001, "이미 존재하는 닉네임입니다."),

    // Http Status Code 401
    INVALID_TOKEN(3000, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(3001, "만료된 토큰입니다."),
    INVALID_LOGIN_ID(3002, "존재하지 않는 아이디입니다."),
    INVALID_PASSWORD(3003, "비밀번호가 틀립니다."),

    // Http Status Code 404
    NOT_FOUND_USER(4001, "존재하지 않는 회원입니다."),
    NOT_FOUND_CATEGORY(4002, "존재하지 않는 카테고리입니다."),
    NOT_FOUND_SPENDING(4003, "존재하지 않는 지출입니다."),

    // HTTP_CODE 500
    UNKNOWN_ERROR(-1, "Unknown Error");

    private final int code;
    private final String message;
}
