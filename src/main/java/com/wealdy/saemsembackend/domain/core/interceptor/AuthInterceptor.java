package com.wealdy.saemsembackend.domain.core.interceptor;

import static com.wealdy.saemsembackend.domain.core.Constant.USER_ID_KEY;

import com.wealdy.saemsembackend.domain.core.dto.auth.JWTDto;
import com.wealdy.saemsembackend.domain.core.exception.InvalidTokenException;
import com.wealdy.saemsembackend.domain.core.util.JwtUtil;
import com.wealdy.saemsembackend.domain.core.util.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final static String PREFIX_BEARER = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LogUtils.accessLog(request, "AuthInterceptor#preHandle");

        if (request.getRequestURI().startsWith("/user") && HttpMethod.POST.matches(request.getMethod())) {
            return true;
        }

        JwtUtil jwtUtil = JwtUtil.getInstance();
        String token = removePrefix(getTokenFromHeader(request));
        JWTDto jwt = jwtUtil.parseToken(token);
        request.setAttribute(USER_ID_KEY, jwt.getUserId());

        return true;
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String tokenString = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(tokenString)) {
            return tokenString;
        }

        throw new InvalidTokenException();
    }

    private String removePrefix(String token) {
        if (token.startsWith(PREFIX_BEARER)) {
            return token.substring(7);
        }
        return token;
    }
}
