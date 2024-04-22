package com.wealdy.saemsembackend.domain.core.exception;

import com.wealdy.saemsembackend.domain.core.response.ErrorResponseDto;
import com.wealdy.saemsembackend.domain.core.response.ResponseCode;
import com.wealdy.saemsembackend.domain.core.util.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        HandlerMethodValidationException.class
    })
    public ErrorResponseDto invalidParameterExceptionHandler(HttpServletRequest request, Exception exception) {
        String errorMessage = "";
        if (exception instanceof MethodArgumentNotValidException manve) {
            List<FieldError> fieldErrors = manve.getBindingResult().getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                System.out.println("[ExceptionControllerAdvice#invalidParameterExceptionHandler] fieldError = " + fieldError);
            }
            errorMessage = fieldErrors.get(0).getDefaultMessage();
        } else if (exception instanceof HandlerMethodValidationException hmve) {

            List<? extends MessageSourceResolvable> allErrors = hmve.getAllErrors();

            for (int i = 0; i < allErrors.size(); i++) {
                System.out.println("[ExceptionControllerAdvice#invalidParameterExceptionHandler] parameterError = " + allErrors.get(i));
                if (i == 0) {
                    errorMessage = allErrors.get(i).getDefaultMessage();
                }
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
