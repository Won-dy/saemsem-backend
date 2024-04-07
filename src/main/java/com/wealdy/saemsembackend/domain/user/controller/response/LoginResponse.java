package com.wealdy.saemsembackend.domain.user.controller.response;

import com.wealdy.saemsembackend.domain.user.service.dto.GetLoginDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;

    public static LoginResponse from(GetLoginDto getLoginDto) {
        return new LoginResponse(getLoginDto.getAccessToken());
    }
}
