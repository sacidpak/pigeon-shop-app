package com.sacidpak.common.exception;

import com.sacidpak.common.dto.ErrorHandlerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BusinessException.class})
    protected ResponseEntity<ErrorHandlerDto> handleAllExceptions(BusinessException ex, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ex.printStackTrace();

        if (ex.getValidation() != null && ex.getValidation().getCode() != null) {
            String respErrCode = ex.getValidation().getCode();

            ErrorHandlerDto response = new ErrorHandlerDto(respErrCode, respErrCode);
            return new ResponseEntity<>(response, httpStatus);
        } else {
            ErrorHandlerDto response = new ErrorHandlerDto( String.valueOf(httpStatus.value()), ex.getMessage());
            return new ResponseEntity<>(response, httpStatus);
        }
    }
}
