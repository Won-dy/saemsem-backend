package com.wealdy.saemsembackend.domain.user.controller;

import com.wealdy.saemsembackend.domain.core.response.Response;
import com.wealdy.saemsembackend.domain.user.controller.request.JoinRequest;
import com.wealdy.saemsembackend.domain.user.controller.request.LoginRequest;
import com.wealdy.saemsembackend.domain.user.controller.response.LoginResponse;
import com.wealdy.saemsembackend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 API
     */
    @PostMapping
    public Response<Void> join(@Valid @RequestBody JoinRequest request) {
        userService.join(request.getLoginId(), request.getPassword(), request.getNickname());
        return Response.OK;
    }

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    public Response<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Response.of(LoginResponse.from(userService.login(request.getLoginId(), request.getPassword())));
    }
}
