package com.cadify.cadifyWAS.exception;

import lombok.RequiredArgsConstructor;

public class BusinessLogicException extends RuntimeException{
    private final ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode){
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
