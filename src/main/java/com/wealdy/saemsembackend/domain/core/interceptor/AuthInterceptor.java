package com.wealdy.saemsembackend.domain.core.interceptor;

import com.wealdy.saemsembackend.domain.core.util.JWTUtil;
import com.wealdy.saemsembackend.domain.core.util.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JWTUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LogUtils.accessLog(request, "AuthInterceptor#preHandle");

        if (request.getRequestURI().startsWith("/user") && HttpMethod.POST.matches(request.getMethod())) {
            return true;
        }

        jwtUtil.verifyToken(request);

        return true;
    }
}
