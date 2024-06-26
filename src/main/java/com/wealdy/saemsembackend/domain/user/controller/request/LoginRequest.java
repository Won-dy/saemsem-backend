package com.wealdy.saemsembackend.domain.user.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank(message = "아이디는 필수 값입니다.")
    @Pattern(regexp = "^[a-z0-9]{5,20}$", message = "아이디는 5~20자의 영문 소문자, 숫자만 사용가능합니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{8,16}$", message = "비밀번호는 8~16자의 영문 대/소문자, 숫자를 사용해야 합니다.")
    private String password;
}
