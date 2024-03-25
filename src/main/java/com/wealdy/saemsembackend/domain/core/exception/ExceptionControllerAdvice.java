package com.wealdy.saemsembackend.domain.core.exception;

import com.wealdy.saemsembackend.domain.core.response.ErrorResponseDto;
import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import com.wealdy.saemsembackend.domain.core.util.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDto invalidParameterExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            System.out.println("fieldError.toString() = " + fieldError.toString());
        }

        LogUtils.errorLog(request, exception);
        return new ErrorResponseDto(
            ResponseCode.BAD_REQUEST.getCode(), ResponseCode.INVALID_PARAMETER.getCode(), fieldErrors.get(0).getDefaultMessage());
    }

    @ExceptionHandler(ExceptionBase.class)
    public ErrorResponseDto customExceptionHandler(HttpServletRequest request, ExceptionBase exception) {
        LogUtils.errorLog(request, exception);
        return new ErrorResponseDto(exception);
    }
}
