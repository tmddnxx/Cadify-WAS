package com.cadify.cadifyWAS.exception;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ErrorResponse(
        String code,
        Integer status,
        String message,
        Instant occurredAt
) {
    public static ErrorResponse of(CustomLogicException exception){
        ExceptionCode code = exception.getExceptionCode();
        return ErrorResponse.builder()
                .code(code.name())
                .status(code.getStatus())
                .message(code.getMessage())
                .occurredAt(exception.getOccurredAt())
                .build();
    }
}