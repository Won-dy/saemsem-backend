package com.wealdy.saemsembackend.domain.core.exception;

import com.wealdy.saemsembackend.domain.core.response.ErrorResponseDto;
import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import com.wealdy.saemsembackend.domain.core.util.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        ConstraintViolationException.class,
    })
    public ErrorResponseDto invalidParameterExceptionHandler(HttpServletRequest request, Exception exception) {
        String errorMessage = "";
        if (exception instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            List<FieldError> fieldErrors = methodArgumentNotValidException.getBindingResult().getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                System.out.println("[ExceptionControllerAdvice#invalidParameterExceptionHandler] fieldError = " + fieldError);
            }
            errorMessage = fieldErrors.get(0).getDefaultMessage();
        } else if (exception instanceof ConstraintViolationException constraintViolationException) {
            for (ConstraintViolation<?> constraintViolation : constraintViolationException.getConstraintViolations()) {
                System.out.println("[ExceptionControllerAdvice#invalidParameterExceptionHandler] constraintViolation = " + constraintViolation);
                errorMessage = constraintViolation.getMessage();
            }
        }
        LogUtils.errorLog(request, exception);
        return new ErrorResponseDto(ResponseCode.BAD_REQUEST.getCode(), ResponseCode.INVALID_PARAMETER.getCode(), errorMessage);
    }

    @ExceptionHandler(ExceptionBase.class)
    public ErrorResponseDto customExceptionHandler(HttpServletRequest request, ExceptionBase exception) {
        LogUtils.errorLog(request, exception);
        return new ErrorResponseDto(exception);
    }
}
