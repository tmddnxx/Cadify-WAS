package com.cadify.cadifyWAS.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomLogicException.class)
    public ResponseEntity<?> handleCustomException(CustomLogicException exception){
        ExceptionCode code = exception.getExceptionCode();
        return ResponseEntity.status(code.getStatus()).body(code.getMessage());
    }
}
