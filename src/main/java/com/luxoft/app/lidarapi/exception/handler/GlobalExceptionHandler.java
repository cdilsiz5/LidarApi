package com.luxoft.app.lidarapi.exception.handler;


import com.luxoft.app.lidarapi.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice

public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationException>  handleValidationException (MethodArgumentNotValidException exception){
        Map<String,String> validationErrors = new HashMap<String, String>();
        for(FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ValidationException errorResponse= ValidationException.builder()
                .exceptionType(exception.getClass().getSimpleName())
                .validationErrors(validationErrors)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .errorTime(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }
}
