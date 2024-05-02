package com.wealdy.saemsembackend.domain.core.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtils {

    public static void accessLog(HttpServletRequest request, String title) {
        log.debug("ACCESS --- [{}] {} ({})", title, request.getRequestURI(), request.getMethod());
    }

    public static void errorLog(HttpServletRequest request, Exception exception) {
        log.error("ERROR --- [{}] {} - Exception Name = {}", request.getMethod(), request.getRequestURL(), exception.getClass().getName());
    }
}
