package com.wealdy.saemsembackend.domain.user.controller;

import com.wealdy.saemsembackend.domain.core.response.Response;
import com.wealdy.saemsembackend.domain.user.dto.UserDto;
import com.wealdy.saemsembackend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 API
     */
    @PostMapping("/user")
    public Response<Void> join(@Valid @RequestBody UserDto.Create request) {
        userService.join(request);
        return Response.OK;
    }

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    public Response<UserDto.LoginResponse> login(@Valid @RequestBody UserDto.LoginRequest request) {
        return Response.of(userService.login(request));
    }
}
