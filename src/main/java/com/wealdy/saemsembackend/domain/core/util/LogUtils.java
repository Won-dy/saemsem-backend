package com.wealdy.saemsembackend.domain.core.util;

import jakarta.servlet.http.HttpServletRequest;

public class LogUtils {

    public static void errorLog(HttpServletRequest request, Exception exception) {
        System.out.printf("[%s] %s - Exception Name = %s%n", request.getMethod(), request.getRequestURL(), exception.getClass().getName());
    }
}
