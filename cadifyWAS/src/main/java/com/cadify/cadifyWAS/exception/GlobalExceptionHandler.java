package com.cadify.cadifyWAS.exception;

import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 로직 수행 중 발생 에러
    @ExceptionHandler(CustomLogicException.class)
    public ResponseEntity<ErrorResponse> handleCustomExceptions(CustomLogicException exception){
        ErrorResponse response = ErrorResponse.of(exception);

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.status()));
    }

    // 로그인시 ID,PWD 검증 에러(null, email&password 형식)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleLoginValidationExceptions(MethodArgumentNotValidException e){
        ErrorResponse response = ErrorResponse.of(
                new CustomLogicException(ExceptionCode.INVALID_LOGIN_INFO)
        );
        return new ResponseEntity<>(response, e.getStatusCode());
    }

    // 정의되지 않은 에러 발생
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnknownExceptions(Exception e){
        logger.error("Error: {}", e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
