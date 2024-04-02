package com.wealdy.saemsembackend.domain.core.interceptor;

import static com.wealdy.saemsembackend.domain.core.Constant.AUTHORIZATION;
import static com.wealdy.saemsembackend.domain.core.Constant.USER_ID_KEY;

import com.wealdy.saemsembackend.domain.core.dto.auth.JWTDto;
import com.wealdy.saemsembackend.domain.core.exception.InvalidTokenException;
import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import com.wealdy.saemsembackend.domain.core.util.JWTUtil;
import com.wealdy.saemsembackend.domain.core.util.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LogUtils.accessLog(request, "AuthInterceptor#preHandle");

        if (request.getRequestURI().startsWith("/user") && HttpMethod.POST.matches(request.getMethod())) {
            return true;
        }

        JWTUtil jwtUtil = new JWTUtil();
        JWTDto jwt = jwtUtil.parseToken(getTokenFromHeader(request));
        request.setAttribute(USER_ID_KEY, jwt.getUserId());

        return true;
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String tokenString = request.getHeader(AUTHORIZATION);

        if (StringUtils.hasText(tokenString)) {
            return tokenString;
        }

        throw new InvalidTokenException();
    }
}
