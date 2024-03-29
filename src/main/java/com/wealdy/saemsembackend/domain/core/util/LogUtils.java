package com.wealdy.saemsembackend.domain.core.util;

import jakarta.servlet.http.HttpServletRequest;

public class LogUtils {

    public static void accessLog(HttpServletRequest request, String title) {
        System.out.printf("ACCESS --- [%s] %s (%s)%n", title, request.getRequestURI(), request.getMethod());
    }

    public static void errorLog(HttpServletRequest request, Exception exception) {
        System.out.printf("ERROR --- [%s] %s - Exception Name = %s%n", request.getMethod(), request.getRequestURL(), exception.getClass().getName());
    }
}
